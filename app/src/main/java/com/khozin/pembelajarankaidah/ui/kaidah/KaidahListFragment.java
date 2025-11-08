package com.khozin.pembelajarankaidah.ui.kaidah;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;
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
import com.khozin.pembelajarankaidah.adapter.KaidahGroupAdapter;
import com.khozin.pembelajarankaidah.data.model.MateriKaidah;
import com.khozin.pembelajarankaidah.data.model.KaidahGroup;
import com.khozin.pembelajarankaidah.data.model.KaidahListResponse;
import com.khozin.pembelajarankaidah.data.model.KaidahGroupedResponse;
import com.khozin.pembelajarankaidah.database.AppDatabase;
import com.khozin.pembelajarankaidah.utils.SessionManager;
import com.khozin.pembelajarankaidah.data.remote.ApiService;
import com.khozin.pembelajarankaidah.network.RetrofitClient;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment untuk menampilkan daftar materi kaidah
 * Menampilkan daftar kaidah sederhana dengan search functionality
 */
public class KaidahListFragment extends Fragment {

    // UI Components
    private RecyclerView rvKaidah;
    private SearchView searchView;
    private MaterialCardView cardAll, cardBelum, cardSelesai;
    private CircularProgressIndicator progressBar;
    private View emptyStateLayout;
    private TextView tvEmptyState;

    // Data
    private AppDatabase database;
    private SessionManager sessionManager;
    private KaidahAdapter kaidahAdapter;
    private KaidahGroupAdapter kaidahGroupAdapter; // Keep for grouped display
    private KaidahViewModel viewModel;
    private ApiService apiService;

    // Filter states
    private String currentFilter = "all"; // all, belum, sedang, selesai
    private String currentSearch = "";

    // Data holders
    private List<MateriKaidah> allKaidahList = new ArrayList<>();
    private List<KaidahGroup> kaidahGroupList = new ArrayList<>();

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
        emptyStateLayout = view.findViewById(R.id.llEmptyState);
        tvEmptyState = emptyStateLayout.findViewById(R.id.tvEmptyTitle);

        // Filter cards
        cardAll = view.findViewById(R.id.cardAll);
        cardBelum = view.findViewById(R.id.cardBelum);
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
        // Setup grouped adapter (for simple list with bab headers and progress)
        kaidahGroupAdapter = new KaidahGroupAdapter();

        // Set adapter
        rvKaidah.setAdapter(kaidahGroupAdapter);

        // Set LayoutManager
        rvKaidah.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set click listeners
        kaidahGroupAdapter.setOnKaidahClickListener(this::onKaidahClick);
        kaidahGroupAdapter.setOnKaidahGroupClickListener(new KaidahGroupAdapter.OnKaidahGroupClickListener() {
            @Override
            public void onGroupClick(KaidahGroup group) {
                KaidahListFragment.this.onGroupClick(group);
            }

            @Override
            public void onGroupExpand(KaidahGroup group, boolean expand) {
                // Handle expand/collapse if needed
            }
        });
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
                updateDisplayedData();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentSearch = newText;
                updateDisplayedData();
                return true;
            }
        });

        // Filter cards
        cardAll.setOnClickListener(v -> {
            currentFilter = "all";
            updateFilterUI();
            updateDisplayedData();
        });

        cardBelum.setOnClickListener(v -> {
            currentFilter = "belum";
            updateFilterUI();
            updateDisplayedData();
        });

        
        cardSelesai.setOnClickListener(v -> {
            currentFilter = "selesai";
            updateFilterUI();
            updateDisplayedData();
        });
    }

    /**
     * Setup ViewModel
     */
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(KaidahViewModel.class);

        // Observe data changes
        viewModel.getKaidahList().observe(getViewLifecycleOwner(), kaidahList -> {
            allKaidahList = kaidahList;
            updateDisplayedData();
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
     * Update displayed data (using grouped view only)
     */
    private void updateDisplayedData() {
        updateGroupedData();
    }

    /**
     * Update grouped view data
     */
    private void updateGroupedData() {
        // Apply filter to kaidah groups
        List<KaidahGroup> filteredGroups = filterGroups(kaidahGroupList, currentFilter, currentSearch);
        kaidahGroupAdapter.updateData(filteredGroups);

        // Update empty state
        updateEmptyState(filteredGroups.isEmpty(), "grouped");
    }

    /**
     * Filter groups based on current filter and search
     */
    private List<KaidahGroup> filterGroups(List<KaidahGroup> groups, String filter, String search) {
        if (groups == null || groups.isEmpty()) {
            return new ArrayList<>();
        }

        List<KaidahGroup> filteredGroups = new ArrayList<>();

        for (KaidahGroup group : groups) {
            // Apply search filter to group name first
            if (!search.isEmpty()) {
                String searchQuery = search.toLowerCase().trim();
                if (!group.getJudulBab().toLowerCase().contains(searchQuery) &&
                    !group.getDeskripsiBab().toLowerCase().contains(searchQuery)) {
                    // If group doesn't match search, check individual kaidah
                    List<MateriKaidah> matchingKaidah = new ArrayList<>();
                    for (MateriKaidah kaidah : group.getKaidahList()) {
                        if (kaidah.getJudulKaidah().toLowerCase().contains(searchQuery) ||
                            kaidah.getDeskripsi().toLowerCase().contains(searchQuery)) {
                            matchingKaidah.add(kaidah);
                        }
                    }

                    if (matchingKaidah.isEmpty()) {
                        continue; // Skip this group entirely
                    }

                    // Create new group with filtered kaidah
                    KaidahGroup filteredGroup = new KaidahGroup(
                        group.getBab(),
                        matchingKaidah,
                        matchingKaidah.size(),
                        group.getTotalSoal()
                    );
                    filteredGroups.add(filteredGroup);
                } else {
                    filteredGroups.add(group);
                }
            } else {
                filteredGroups.add(group);
            }
        }

        // Apply status filter
        if (!filter.equals("all")) {
            List<KaidahGroup> statusFilteredGroups = new ArrayList<>();
            for (KaidahGroup group : filteredGroups) {
                List<MateriKaidah> statusFilteredKaidah = filterKaidah(group.getKaidahList(), filter, "");
                if (!statusFilteredKaidah.isEmpty()) {
                    KaidahGroup statusFilteredGroup = new KaidahGroup(
                        group.getBab(),
                        statusFilteredKaidah,
                        statusFilteredKaidah.size(),
                        group.getTotalSoal()
                    );
                    statusFilteredGroups.add(statusFilteredGroup);
                }
            }
            filteredGroups = statusFilteredGroups;
        }

        return filteredGroups;
    }

    /**
     * Filter kaidah list based on current filter and search
     */
    private List<MateriKaidah> filterKaidah(List<MateriKaidah> kaidahList, String filter, String search) {
        if (kaidahList == null || kaidahList.isEmpty()) {
            return new ArrayList<>();
        }

        List<MateriKaidah> filteredList = new ArrayList<>(kaidahList);

        // Apply search filter
        if (!search.isEmpty()) {
            String searchQuery = search.toLowerCase().trim();
            filteredList = filteredList.stream()
                    .filter(kaidah ->
                        kaidah.getJudulKaidah().toLowerCase().contains(searchQuery) ||
                        kaidah.getDeskripsi().toLowerCase().contains(searchQuery))
                    .collect(java.util.stream.Collectors.toList());
        }

        // Apply status filter
        switch (filter) {
            case "belum":
                return filteredList.stream()
                        .filter(kaidah -> kaidah.getStatus() == null || kaidah.getStatus().equals("belum_dimulai"))
                        .collect(java.util.stream.Collectors.toList());
            case "sedang":
                return filteredList.stream()
                        .filter(kaidah -> "sedang_belajar".equals(kaidah.getStatus()))
                        .collect(java.util.stream.Collectors.toList());
            case "selesai":
                return filteredList.stream()
                        .filter(kaidah -> "selesai".equals(kaidah.getStatus()))
                        .collect(java.util.stream.Collectors.toList());
            default: // all
                return filteredList;
        }
    }

    /**
     * Update empty state visibility
     */
    private void updateEmptyState(boolean isEmpty, String viewType) {
        if (isEmpty) {
            emptyStateLayout.setVisibility(View.VISIBLE);
            rvKaidah.setVisibility(View.GONE);

            String message;
            if (!currentSearch.isEmpty()) {
                message = "Tidak ada kaidah yang cocok dengan pencarian \"" + currentSearch + "\"";
            } else {
                switch (currentFilter) {
                    case "belum":
                        message = "Tidak ada kaidah yang belum dimulai";
                        break;
                    case "sedang":
                        message = "Tidak ada kaidah yang sedang dipelajari";
                        break;
                    case "selesai":
                        message = "Tidak ada kaidah yang sudah selesai";
                        break;
                    default:
                        message = viewType.equals("grouped") ? "Tidak ada data bab" : "Tidak ada data kaidah";
                        break;
                }
            }
            tvEmptyState.setText(message);
        } else {
            emptyStateLayout.setVisibility(View.GONE);
            rvKaidah.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Load kaidah data from API
     */
    private void loadKaidahData() {
        Log.d("KAIDAH_DEBUG", "Loading kaidah data...");
        showLoading();

        try {
            int siswaId = sessionManager.getUserId();
            Log.d("KAIDAH_DEBUG", "Student ID: " + siswaId);

            // Load grouped data from API
            loadGroupedKaidahFromAPI(siswaId);

        } catch (Exception e) {
            Log.e("KAIDAH_DEBUG", "Error loading kaidah data", e);
            if (isAdded() && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Error loading data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    hideLoading();
                });
            }
        }
    }

    /**
     * Load grouped kaidah data from API
     */
    private void loadGroupedKaidahFromAPI(int siswaId) {
        String sessionToken = sessionManager.getAuthToken();
        if (sessionToken == null || sessionToken.isEmpty()) {
            Log.e("KAIDAH_DEBUG", "No session token found");
            return;
        }

        Log.d("KAIDAH_DEBUG", "Loading grouped kaidah from API...");

        // Call grouped API
        apiService.getKaidahGrouped("Bearer " + sessionToken).enqueue(new Callback<KaidahGroupedResponse>() {
            @Override
            public void onResponse(Call<KaidahGroupedResponse> call, Response<KaidahGroupedResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    KaidahGroupedResponse groupedResponse = response.body();
                    if (groupedResponse.isSuccess() && groupedResponse.getData() != null) {
                        kaidahGroupList = groupedResponse.getData().getGroups();
                        Log.d("KAIDAH_DEBUG", "API returned " + kaidahGroupList.size() + " kaidah groups");

                        // Update progress from local database on background thread
                        Executors.newSingleThreadExecutor().execute(() -> {
                            updateProgressFromLocalDatabase();

                            // Update UI on main thread after progress is updated
                            if (isAdded() && getActivity() != null) {
                                getActivity().runOnUiThread(() -> {
                                    updateDisplayedData();
                                    hideLoading();
                                    Log.d("KAIDAH_DEBUG", "Grouped UI updated successfully with local progress");
                                });
                            }
                        });
                    } else {
                        Log.e("KAIDAH_DEBUG", "API response not successful: " + groupedResponse.getMessage());
                        if (isAdded() && getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                Toast.makeText(getContext(), groupedResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                hideLoading();
                            });
                        }
                    }
                } else {
                    Log.e("KAIDAH_DEBUG", "API call failed: " + response.code());
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "API Error: " + response.code(), Toast.LENGTH_SHORT).show();
                            hideLoading();
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<KaidahGroupedResponse> call, Throwable t) {
                Log.e("KAIDAH_DEBUG", "API call failed", t);
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        hideLoading();
                    });
                }
            }
        });
    }

    
    /**
     * Update filter UI to reflect current selection
     */
    private void updateFilterUI() {
        // Reset all cards
        cardAll.setStrokeWidth(0);
        cardBelum.setStrokeWidth(0);
        cardSelesai.setStrokeWidth(0);

        // Highlight selected filter
        switch (currentFilter) {
            case "belum":
                cardBelum.setStrokeWidth(3);
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
     * Handle group click
     */
    private void onGroupClick(KaidahGroup group) {
        // Navigate to group detail or expand/collapse
        Log.d("KAIDAH_DEBUG", "Group clicked: " + group.getJudulBab());
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
        // Debug logging
        Log.d("KAIDAH_NAVIGATION", String.format(
            "Navigating to kaidah detail:\n" +
            "ID Materi: %d\n" +
            "Judul: %s\n" +
            "Urutan: %d",
            kaidah.getIdMateri(),
            kaidah.getJudulKaidah(),
            kaidah.getUrutan()
        ));

        // Create fragment instance with kaidah ID
        KaidahDetailFragment detailFragment = KaidahDetailFragment.newInstance(kaidah.getIdMateri());

        // Navigate to detail fragment
        if (getParentFragmentManager() != null) {
            getParentFragmentManager().beginTransaction()
                    .setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                    )
                    .replace(R.id.fragment_container, detailFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * Show loading state
     */
    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        rvKaidah.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.GONE);
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

    /**
     * Update progress information from local database
     * This merges API data with local progress data synchronously
     */
    private void updateProgressFromLocalDatabase() {
        if (kaidahGroupList == null || kaidahGroupList.isEmpty()) {
            return;
        }

        Log.d("KAIDAH_DEBUG", "Updating progress from local database...");

        try {
            int siswaId = sessionManager.getUserId();
            List<MateriKaidah> localMateriList = database.materiKaidahDao().getMateriWithProgressSync(siswaId);

            Log.d("KAIDAH_DEBUG", "Local database has " + localMateriList.size() + " materi with progress");

            // Create a map for quick lookup
            java.util.Map<Integer, MateriKaidah> localMateriMap = new java.util.HashMap<>();
            for (MateriKaidah localMateri : localMateriList) {
                localMateriMap.put(localMateri.getIdMateri(), localMateri);
            }

            // Update each kaidah group with local progress data
            int updatedCount = 0;
            for (KaidahGroup group : kaidahGroupList) {
                if (group.getKaidahList() != null) {
                    for (MateriKaidah apiMateri : group.getKaidahList()) {
                        MateriKaidah localMateri = localMateriMap.get(apiMateri.getIdMateri());
                        if (localMateri != null) {
                            // Update API materi with local progress data
                            apiMateri.setProgressPercentage(localMateri.getProgressPercentage());
                            apiMateri.setCompleted(localMateri.isCompleted());

                            Log.d("KAIDAH_DEBUG", "Updated progress for materi " + apiMateri.getJudulKaidah() +
                                  ": " + localMateri.getProgressPercentage() + "%");
                            updatedCount++;
                        }
                    }
                }
            }

            Log.d("KAIDAH_DEBUG", "Updated progress for " + updatedCount + " materi items");

        } catch (Exception e) {
            Log.e("KAIDAH_DEBUG", "Error updating progress from local database", e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Clear resources
        if (kaidahGroupAdapter != null) {
            kaidahGroupAdapter = null;
        }
    }
}