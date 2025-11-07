package com.khozin.pembelajarankaidah.ui.kaidah;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.khozin.pembelajarankaidah.R;
import com.khozin.pembelajarankaidah.adapter.KaidahAdapter;
import com.khozin.pembelajarankaidah.data.model.MateriKaidah;
import com.khozin.pembelajarankaidah.database.AppDatabase;
import com.khozin.pembelajarankaidah.utils.SessionManager;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Fragment untuk menampilkan daftar materi kaidah
 * Dengan filter berdasarkan status dan search functionality
 */
public class KaidahListFragment extends Fragment {

    // UI Components
    private RecyclerView rvKaidah;
    private SearchView searchView;
    private MaterialCardView cardAll, cardBelum, cardSedang, cardSelesai;
    private CircularProgressIndicator progressBar;

    // Data
    private AppDatabase database;
    private SessionManager sessionManager;
    private KaidahAdapter kaidahAdapter;
    private KaidahViewModel viewModel;

    // Filter states
    private String currentFilter = "all"; // all, belum, sedang, selesai
    private String currentSearch = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kaidah_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupDatabase();
        setupRecyclerView();
        setupListeners();
        setupViewModel();

        // Load initial data
        loadKaidahData();
    }

    /**
     * Initialize views
     */
    private void initViews(View view) {
        rvKaidah = view.findViewById(R.id.rvKaidah);
        searchView = view.findViewById(R.id.searchView);
        progressBar = view.findViewById(R.id.progressBar);

        // Filter cards
        cardAll = view.findViewById(R.id.cardAll);
        cardBelum = view.findViewById(R.id.cardBelum);
        cardSedang = view.findViewById(R.id.cardSedang);
        cardSelesai = view.findViewById(R.id.cardSelesai);
    }

    /**
     * Setup database
     */
    private void setupDatabase() {
        sessionManager = new SessionManager(requireContext());
        database = AppDatabase.getDatabase(requireContext());
    }

    /**
     * Setup RecyclerView
     */
    private void setupRecyclerView() {
        kaidahAdapter = new KaidahAdapter();
        rvKaidah.setLayoutManager(new LinearLayoutManager(getContext()));
        rvKaidah.setAdapter(kaidahAdapter);

        // Set click listener
        kaidahAdapter.setOnKaidahClickListener(this::onKaidahClick);
    }

    /**
     * Setup listeners
     */
    private void setupListeners() {
        // Search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentSearch = query;
                loadKaidahData();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentSearch = newText;
                loadKaidahData();
                return true;
            }
        });

        // Filter cards
        cardAll.setOnClickListener(v -> {
            currentFilter = "all";
            updateFilterUI();
            loadKaidahData();
        });

        cardBelum.setOnClickListener(v -> {
            currentFilter = "belum";
            updateFilterUI();
            loadKaidahData();
        });

        cardSedang.setOnClickListener(v -> {
            currentFilter = "sedang";
            updateFilterUI();
            loadKaidahData();
        });

        cardSelesai.setOnClickListener(v -> {
            currentFilter = "selesai";
            updateFilterUI();
            loadKaidahData();
        });
    }

    /**
     * Setup ViewModel
     */
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(KaidahViewModel.class);

        // Observe data changes
        viewModel.getKaidahList().observe(getViewLifecycleOwner(), kaidahList -> {
            kaidahAdapter.updateData(kaidahList);
            hideLoading();
        });

        // Observe loading state
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                showLoading();
            } else {
                hideLoading();
            }
        });

        // Observe errors
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Load kaidah data from database
     */
    private void loadKaidahData() {
        showLoading();

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                int siswaId = sessionManager.getUserId();
                List<MateriKaidah> kaidahList;

                switch (currentFilter) {
                    case "belum":
                        kaidahList = database.materiKaidahDao().getMateriBelumDimulai(siswaId);
                        break;
                    case "sedang":
                        kaidahList = database.materiKaidahDao().getMateriSedangBelajar(siswaId);
                        break;
                    case "selesai":
                        kaidahList = database.materiKaidahDao().getMateriSelesai(siswaId);
                        break;
                    default: // all
                        kaidahList = database.materiKaidahDao().getMateriWithProgress(siswaId);
                        break;
                }

                // Apply search filter
                if (!currentSearch.isEmpty()) {
                    kaidahList = filterBySearch(kaidahList, currentSearch);
                }

                // Update UI on main thread
                List<MateriKaidah> finalKaidahList = kaidahList;
                requireActivity().runOnUiThread(() -> {
                    kaidahAdapter.updateData(finalKaidahList);
                    hideLoading();
                });

            } catch (Exception e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Error loading data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    hideLoading();
                });
            }
        });
    }

    /**
     * Filter kaidah by search query
     */
    private List<MateriKaidah> filterBySearch(List<MateriKaidah> kaidahList, String query) {
        if (query == null || query.trim().isEmpty()) {
            return kaidahList;
        }

        String searchQuery = query.toLowerCase().trim();
        return kaidahList.stream()
                .filter(kaidah ->
                    kaidah.getJudulKaidah().toLowerCase().contains(searchQuery) ||
                    kaidah.getDeskripsi().toLowerCase().contains(searchQuery))
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Update filter UI to reflect current selection
     */
    private void updateFilterUI() {
        // Reset all cards
        cardAll.setStrokeWidth(0);
        cardBelum.setStrokeWidth(0);
        cardSedang.setStrokeWidth(0);
        cardSelesai.setStrokeWidth(0);

        // Highlight selected filter
        switch (currentFilter) {
            case "belum":
                cardBelum.setStrokeWidth(3);
                break;
            case "sedang":
                cardSedang.setStrokeWidth(3);
                break;
            case "selesai":
                cardSelesai.setStrokeWidth(3);
                break;
            default: // all
                cardAll.setStrokeWidth(3);
                break;
        }
    }

    /**
     * Handle kaidah click
     */
    private void onKaidahClick(MateriKaidah kaidah) {
        // Navigate to detail or start learning
        if (kaidah.getStatus() == null || kaidah.getStatus().equals("belum_dimulai")) {
            // Start new learning session
            navigateToDetail(kaidah);
        } else {
            // Continue or review
            navigateToDetail(kaidah);
        }
    }

    /**
     * Navigate to kaidah detail
     */
    private void navigateToDetail(MateriKaidah kaidah) {
        // TODO: Navigate to KaidahDetailActivity
        // Intent intent = new Intent(getContext(), KaidahDetailActivity.class);
        // intent.putExtra("kaidah_id", kaidah.getIdMateri());
        // startActivity(intent);

        Toast.makeText(getContext(), "Navigate to: " + kaidah.getJudulKaidah(), Toast.LENGTH_SHORT).show();
    }

    /**
     * Show loading state
     */
    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        rvKaidah.setVisibility(View.GONE);
    }

    /**
     * Hide loading state
     */
    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
        rvKaidah.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when fragment resumes
        if (sessionManager.isLoggedIn()) {
            loadKaidahData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Clear resources
        if (kaidahAdapter != null) {
            kaidahAdapter = null;
        }
    }
}