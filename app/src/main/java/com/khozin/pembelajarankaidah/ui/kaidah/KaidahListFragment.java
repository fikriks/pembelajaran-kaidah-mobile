package com.khozin.pembelajarankaidah.ui.kaidah;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
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
import com.khozin.pembelajarankaidah.data.model.ApiResponse;
import com.khozin.pembelajarankaidah.data.model.KaidahListResponse;
import com.khozin.pembelajarankaidah.database.AppDatabase;
import com.khozin.pembelajarankaidah.utils.SessionManager;
import com.khozin.pembelajarankaidah.data.remote.ApiService;
import com.khozin.pembelajarankaidah.network.RetrofitClient;
import java.util.List;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private ApiService apiService;

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
        apiService = RetrofitClient.getInstance().getRetrofit().create(ApiService.class);
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
        Log.d("KAIDAH_DEBUG", "Loading kaidah data...");
        showLoading();

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                int siswaId = sessionManager.getUserId();
                Log.d("KAIDAH_DEBUG", "Student ID: " + siswaId);

                // Check if database is empty, if so sync from API first
                int localKaidahCount = database.materiKaidahDao().getCount();
                Log.d("KAIDAH_DEBUG", "Local kaidah count: " + localKaidahCount);

                if (localKaidahCount == 0) {
                    Log.d("KAIDAH_DEBUG", "Database empty, syncing from API...");
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            syncKaidahFromAPI();
                        });
                    }
                    return; // loadKaidahData will be called again after sync completes
                }

                List<MateriKaidah> kaidahList;

                switch (currentFilter) {
                    case "belum":
                        Log.d("KAIDAH_DEBUG", "Filter: belum dimulai");
                        kaidahList = database.materiKaidahDao().getMateriBelumDimulaiSync(siswaId);
                        break;
                    case "sedang":
                        Log.d("KAIDAH_DEBUG", "Filter: sedang belajar");
                        kaidahList = database.materiKaidahDao().getMateriSedangBelajarSync(siswaId);
                        break;
                    case "selesai":
                        Log.d("KAIDAH_DEBUG", "Filter: selesai");
                        kaidahList = database.materiKaidahDao().getMateriSelesaiSync(siswaId);
                        break;
                    default: // all
                        Log.d("KAIDAH_DEBUG", "Filter: all");
                        kaidahList = database.materiKaidahDao().getMateriWithProgressSync(siswaId);
                        break;
                }

                Log.d("KAIDAH_DEBUG", "Total kaidah loaded: " + (kaidahList != null ? kaidahList.size() : 0));

                // Apply search filter
                if (!currentSearch.isEmpty()) {
                    kaidahList = filterBySearch(kaidahList, currentSearch);
                    Log.d("KAIDAH_DEBUG", "After search filter: " + kaidahList.size());
                }

                // Update UI on main thread
                List<MateriKaidah> finalKaidahList = kaidahList;
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        kaidahAdapter.updateData(finalKaidahList);
                        hideLoading();
                        Log.d("KAIDAH_DEBUG", "UI updated successfully");
                    });
                }

            } catch (Exception e) {
                Log.e("KAIDAH_DEBUG", "Error loading kaidah data", e);
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Error loading data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        hideLoading();
                    });
                }
            }
        });
    }

    /**
     * Sync kaidah data from API
     */
    private void syncKaidahFromAPI() {
        Log.d("KAIDAH_DEBUG", "Syncing kaidah from API...");

        String sessionToken = sessionManager.getAuthToken();
        if (sessionToken == null || sessionToken.isEmpty()) {
            Log.e("KAIDAH_DEBUG", "No session token found");
            return;
        }

        // Gunakan API method dengan wrapper yang sesuai struktur response
        apiService.getKaidahListWithWrapper().enqueue(new Callback<KaidahListResponse>() {
            @Override
            public void onResponse(Call<KaidahListResponse> call, Response<KaidahListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    KaidahListResponse kaidahResponse = response.body();
                    if (kaidahResponse.isSuccess() && kaidahResponse.getKaidahList() != null) {
                        List<MateriKaidah> kaidahList = kaidahResponse.getKaidahList();
                        Log.d("KAIDAH_DEBUG", "API returned " + kaidahList.size() + " kaidah items");

                        // Save to database on background thread
                        Executors.newSingleThreadExecutor().execute(() -> {
                            try {
                                // Clear existing data
                                database.materiKaidahDao().deleteAll();

                                // Insert new data
                                for (MateriKaidah kaidah : kaidahList) {
                                    // Convert API response to database entity
                                    MateriKaidah entity = new MateriKaidah();

                                    entity.setIdMateri(kaidah.getIdMateri());
                                    entity.setJudulKaidah(kaidah.getJudulKaidah());
                                    entity.setDeskripsi(kaidah.getDeskripsi());
                                    entity.setPenjelasan(kaidah.getPenjelasan());
                                    entity.setContoh(kaidah.getContoh());
                                    entity.setUrutan(kaidah.getUrutan());
                                    entity.setDibuatOleh(kaidah.getDibuatOleh());

                                    database.materiKaidahDao().insert(entity);
                                }

                                Log.d("KAIDAH_DEBUG", "Successfully saved " + kaidahList.size() + " kaidah to database");

                                // Reload data from local database
                                if (isAdded() && getActivity() != null) {
                                    getActivity().runOnUiThread(() -> {
                                        loadKaidahData();
                                    });
                                }

                            } catch (Exception e) {
                                Log.e("KAIDAH_DEBUG", "Error saving kaidah to database", e);
                                if (isAdded() && getActivity() != null) {
                                    getActivity().runOnUiThread(() -> {
                                        Toast.makeText(getContext(), "Error saving data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                                }
                            }
                        });

                    } else {
                        Log.e("KAIDAH_DEBUG", "API response not successful: " + kaidahResponse.getMessage());
                        if (isAdded() && getActivity() != null) {
                            Toast.makeText(getContext(), kaidahResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Log.e("KAIDAH_DEBUG", "API call failed: " + response.code());
                    if (isAdded() && getActivity() != null) {
                        Toast.makeText(getContext(), "API Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<KaidahListResponse> call, Throwable t) {
                Log.e("KAIDAH_DEBUG", "API call failed", t);
                if (isAdded() && getActivity() != null) {
                    Toast.makeText(getContext(), "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
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