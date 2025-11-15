package com.khozin.pembelajarankaidah.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import android.widget.ProgressBar;
import com.khozin.pembelajarankaidah.R;
import com.khozin.pembelajarankaidah.adapter.KaidahSmallAdapter;
import com.khozin.pembelajarankaidah.data.model.MateriKaidah;
import com.khozin.pembelajarankaidah.data.model.SesiLatihan;
import com.khozin.pembelajarankaidah.utils.SessionManager;
import com.khozin.pembelajarankaidah.data.remote.ApiService;
import com.khozin.pembelajarankaidah.data.model.ApiResponse;
import com.khozin.pembelajarankaidah.data.model.ChapterProgressResponse;
import com.khozin.pembelajarankaidah.network.RetrofitClient;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Home Fragment - Dashboard dengan statistics
 * Menampilkan welcome section, progress overview, quick stats, dan recent kaidah
 */
public class HomeFragment extends Fragment {

    // UI Components
    private TextView tvWelcome;
    private TextView tvUserName;
    private ProgressBar progressBar;
    private TextView tvProgressStats;
    private TextView tvKaidahCount;
    private TextView tvQuizCount;
    private TextView tvStatusProgress;
    private Button btnContinueLearning;
    private Button btnStartQuiz;
    private RecyclerView rvRecentKaidah;

    // Data
    private SessionManager sessionManager;
    private KaidahSmallAdapter recentKaidahAdapter;
    private ApiService apiService;

    // Stats
    private int totalKaidah = 0;
    private int kaidahSelesai = 0;
    private int totalQuiz = 0;
    private float progressPercentage = 0.0f;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupDatabase();
        setupListeners();
        loadUserData();
        loadData();
    }

    /**
     * Initialize views
     */
    private void initViews(View view) {
        tvWelcome = view.findViewById(R.id.tvWelcome);
        tvUserName = view.findViewById(R.id.tvUserName);
        progressBar = view.findViewById(R.id.progressBar);
        tvProgressStats = view.findViewById(R.id.tvProgressStats);
        tvKaidahCount = view.findViewById(R.id.tvKaidahCount);
        tvQuizCount = view.findViewById(R.id.tvQuizCount);
        tvStatusProgress = view.findViewById(R.id.tvStatusProgress);
        btnContinueLearning = view.findViewById(R.id.btnContinueLearning);
        btnStartQuiz = view.findViewById(R.id.btnStartQuiz);
        rvRecentKaidah = view.findViewById(R.id.rvRecentKaidah);

        // Setup RecyclerView
        setupRecyclerView();
    }

    /**
     * Setup RecyclerView
     */
    private void setupRecyclerView() {
        recentKaidahAdapter = new KaidahSmallAdapter();
        rvRecentKaidah.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRecentKaidah.setAdapter(recentKaidahAdapter);
        rvRecentKaidah.setNestedScrollingEnabled(false);
    }

    /**
     * Setup database
     */
    private void setupDatabase() {
        sessionManager = new SessionManager(requireContext());
        apiService = RetrofitClient.getInstance().getApiService();
    }

    /**
     * Setup listeners
     */
    private void setupListeners() {
        btnContinueLearning.setOnClickListener(v -> {
            // Navigate to kaidah list with continue learning
            navigateToKaidahList();
        });

        btnStartQuiz.setOnClickListener(v -> {
            // Navigate to quiz selection
            navigateToQuizSelection();
        });

        // Swipe refresh setup
        // TODO: Implement swipe refresh
    }

    /**
     * Load user data
     */
    private void loadUserData() {
        if (sessionManager.isLoggedIn()) {
            String userName = sessionManager.getUserName();
            if (userName != null) {
                tvUserName.setText(userName);
                tvWelcome.setText("Selamat datang, " + sessionManager.getUserName() + "!");
            }
        }
    }

    /**
     * Load data from API
     */
    private void loadData() {
        loadProgressFromAPI();
    }

    /**
     * Public method to refresh data from API
     * This can be called from other fragments when data changes
     */
    public void refreshData() {
        android.util.Log.d("HomeFragment", "Refreshing data from API...");
        loadProgressFromAPI();
    }

    /**
     * Load statistics data
     */
    private void loadStatistics() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                // Get total kaidah
                totalKaidah = 0; // TODO: Get from API;

                // Get completed kaidah for current user
                int siswaId = sessionManager.getUserId();

                // Validate siswaId
                if (siswaId == -1) {
                    // User not logged in or invalid session
                    kaidahSelesai = 0;
                    totalQuiz = 0;
                } else {
                    // Count completed materi for current user only
                    // kaidahSelesai = database.riwayatBelajarDao().countBySiswaAndStatus(siswaId, "selesai"); // REMOVED - API only approach
                    kaidahSelesai = 0; // TODO: Get from API

                    // Get total quiz sessions
                    // totalQuiz = database.sesiLatihanDao().getTotalSesiSelesai(siswaId); // REMOVED - API only approach
                    totalQuiz = 0; // TODO: Get from API
                }

                // Calculate progress percentage
                if (totalKaidah > 0) {
                    progressPercentage = ((float) kaidahSelesai / totalKaidah) * 100;
                }

                // Debug logging
                android.util.Log.d("HomeFragment", String.format(
                    "Statistics Debug:\n" +
                    "Siswa ID: %d\n" +
                    "Total Kaidah: %d\n" +
                    "Kaidah Selesai: %d\n" +
                    "Total Quiz: %d\n" +
                    "Progress Percentage: %.2f",
                    siswaId, totalKaidah, kaidahSelesai, totalQuiz, progressPercentage
                ));

                // Get average score from API only
                float averageScore = 0.0f; // Will be loaded from API later

                // Update UI on main thread
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> updateProgressUI(averageScore));
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Handle error
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(this::showErrorState);
                }
            }
        });
    }

    /**
     * Load recent kaidah
     */
    private void loadRecentKaidah() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                int siswaId = sessionManager.getUserId();
                List<MateriKaidah> recentKaidah = new ArrayList<>(); // TODO: Get from API

                // Limit to 5 most recent
                if (recentKaidah.size() > 5) {
                    recentKaidah = recentKaidah.subList(0, 5);
                }

                // Update adapter on main thread
                List<MateriKaidah> finalRecentKaidah = recentKaidah;
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        if (finalRecentKaidah != null && !finalRecentKaidah.isEmpty()) {
                            recentKaidahAdapter.updateData(finalRecentKaidah);
                        } else {
                            showEmptyState();
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(this::showErrorState);
                }
            }
        });
    }

    /**
     * Update progress UI
     */
    private void updateProgressUI(float averageScore) {
        // Update progress bar
        progressBar.setProgress((int) progressPercentage);

        // Update stats text
        tvKaidahCount.setText(String.valueOf(totalKaidah));
        tvQuizCount.setText(String.valueOf(totalQuiz));
        tvProgressStats.setText(String.format("%.0f%% selesai", progressPercentage));

        // Update status text
        if (progressPercentage == 0) {
            tvStatusProgress.setText("Mulai belajar");
        } else if (progressPercentage < 100) {
            tvStatusProgress.setText("Lanjutkan belajar");
        } else {
            tvStatusProgress.setText("Luar biasa! 🎉");
        }

        // Update continue learning button
        if (progressPercentage < 100) {
            btnContinueLearning.setVisibility(View.VISIBLE);
            btnContinueLearning.setClickable(true);
        } else {
            btnContinueLearning.setVisibility(View.GONE);
        }

        // Show/hide start quiz based on available kaidah
        btnStartQuiz.setVisibility(totalKaidah > 0 ? View.VISIBLE : View.GONE);
    }

    /**
     * Update main UI
     */
    private void updateUI() {
        // Update stats cards
        tvKaidahCount.setText(String.valueOf(totalKaidah));
        tvQuizCount.setText(String.valueOf(totalQuiz));

        // Update progress
        progressBar.setProgress((int) progressPercentage);
        tvProgressStats.setText(String.format("%.0f%% selesai", progressPercentage));

        // Update status
        if (progressPercentage == 0) {
            tvStatusProgress.setText("Mulai belajar");
            btnContinueLearning.setVisibility(View.GONE);
        } else if (progressPercentage < 100) {
            tvStatusProgress.setText("Lanjutkan belajar");
            btnContinueLearning.setVisibility(View.VISIBLE);
        } else {
            tvStatusProgress.setText("Luar biasa! 🎉");
            btnContinueLearning.setVisibility(View.GONE);
        }
    }

    /**
     * Show empty state
     */
    private void showEmptyState() {
        // Show empty state message
        // TODO: Implement empty state UI
    }

    /**
     * Show error state
     */
    private void showErrorState() {
        // Show error message
        // TODO: Implement error state UI
    }

    /**
     * Navigate to kaidah list
     */
    private void navigateToKaidahList() {
        // TODO: Implement navigation to KaidahListFragment
    }

    /**
     * Navigate to quiz selection
     */
    private void navigateToQuizSelection() {
        // TODO: Implement navigation to QuizSelectionFragment
    }

    /**
     * Load progress data from API
     */
    private void loadProgressFromAPI() {
        if (!sessionManager.isLoggedIn()) {
            return;
        }

        String sessionToken = sessionManager.getAuthToken();
        if (sessionToken == null || sessionToken.isEmpty()) {
            android.util.Log.e("HomeFragment", "No session token found");
            return;
        }

        android.util.Log.d("HomeFragment", "Loading progress from API...");

        // Load progress statistics from API with authorization
        apiService.getProgress("Bearer " + sessionToken).enqueue(new Callback<ApiResponse<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<ApiResponse<Map<String, Object>>> call, Response<ApiResponse<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Map<String, Object>> apiResponse = response.body();
                    // Update UI with API data on main thread
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            android.util.Log.d("HomeFragment", "Progress API response received successfully");
                            android.util.Log.d("HomeFragment", "Progress response received");

                            // Parse and update UI with actual API response data
                            updateUIWithAPIData(apiResponse);
                        });
                    }
                } else {
                    android.util.Log.e("HomeFragment", "Progress API returned null or invalid response");
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            android.util.Log.d("HomeFragment", "Falling back to local database due to API error");
                            loadStatistics(); // Fallback to local data
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Map<String, Object>>> call, Throwable t) {
                android.util.Log.e("HomeFragment", "Error loading progress from API", t);
                // Fallback to local database if API fails
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        android.util.Log.d("HomeFragment", "Falling back to local database");
                        loadStatistics(); // Fallback to local data
                    });
                }
            }
        });
    }

    /**
     * Update UI with API data
     */
    private void updateUIWithAPIData(com.khozin.pembelajarankaidah.data.model.ApiResponse apiResponse) {
        if (apiResponse == null || apiResponse.getData() == null) {
            android.util.Log.e("HomeFragment", "API response is null or empty");
            showErrorState();
            return;
        }

        try {
            // Parse the API response data based on the actual API structure
            Object data = apiResponse.getData();

            // Expected structure from API documentation:
            // {
            //   "status": "success",
            //   "data": {
            //     "overview": {
            //       "total_kaidah": 40,
            //       "kaidah_selesai": 0,
            //       "kaidah_sedang_belajar": 0,
            //       "kaidah_belum_dimulai": 40,
            //       "total_sesi": 0,
            //       "rata_rata_skor": 0,
            //       "total_soal_dijawab": 0,
            //       "total_jawaban_benar": 0,
            //       "persentase_benar_keseluruhan": 0,
            //       "persentase_kemajuan": 0
            //     },
            //     "kaidah_progress": [...]
            //   }
            // }

            if (data instanceof com.google.gson.JsonObject) {
                processJsonResponse((com.google.gson.JsonObject) data);
            } else if (data instanceof java.util.Map) {
                processMapResponse((java.util.Map<String, Object>) data);
            } else {
                android.util.Log.e("HomeFragment", "Unexpected data format in API response: " + data.getClass().getSimpleName());
                showErrorState();
            }

        } catch (Exception e) {
            android.util.Log.e("HomeFragment", "Error parsing API response data", e);
            if (isAdded() && getActivity() != null) {
                getActivity().runOnUiThread(this::showErrorState);
            }
        }
    }

    /**
     * Process JSON response data
     */
    private void processJsonResponse(com.google.gson.JsonObject jsonData) {
        // Extract overview data
        if (jsonData.has("overview")) {
            com.google.gson.JsonObject overview = jsonData.getAsJsonObject("overview");

            // Update statistics
            totalKaidah = overview.has("total_kaidah") ? overview.get("total_kaidah").getAsInt() : 0;
            kaidahSelesai = overview.has("kaidah_selesai") ? overview.get("kaidah_selesai").getAsInt() : 0;
            totalQuiz = overview.has("total_sesi") ? overview.get("total_sesi").getAsInt() : 0;

            // Calculate progress percentage
            if (totalKaidah > 0) {
                progressPercentage = overview.has("persentase_kemajuan") ?
                    overview.get("persentase_kemajuan").getAsFloat() :
                    ((float) kaidahSelesai / totalKaidah) * 100;
            }

            android.util.Log.d("HomeFragment", String.format(
                "API Data parsed (JSON):\n" +
                "Total Kaidah: %d\n" +
                "Kaidah Selesai: %d\n" +
                "Total Quiz: %d\n" +
                "Progress: %.2f%%",
                totalKaidah, kaidahSelesai, totalQuiz, progressPercentage
            ));

            // Extract and process kaidah progress data
            java.util.List<com.khozin.pembelajarankaidah.data.model.MateriKaidah> recentKaidahList = new java.util.ArrayList<>();
            if (jsonData.has("kaidah_progress")) {
                com.google.gson.JsonArray kaidahProgress = jsonData.getAsJsonArray("kaidah_progress");

                // Convert to MateriKaidah objects
                com.google.gson.Gson gson = new com.google.gson.Gson();
                for (int i = 0; i < Math.min(kaidahProgress.size(), 5); i++) {
                    com.google.gson.JsonObject kaidahJson = kaidahProgress.get(i).getAsJsonObject();
                    com.khozin.pembelajarankaidah.data.model.MateriKaidah kaidah = new com.khozin.pembelajarankaidah.data.model.MateriKaidah();
                    kaidah.setIdMateri(kaidahJson.get("id_materi").getAsInt());
                    kaidah.setJudulMateri(kaidahJson.get("judul_kaidah").getAsString());
                    kaidah.setDeskripsi(kaidahJson.get("deskripsi").getAsString());
                    kaidah.setProgressPercentage((int) kaidahJson.get("completion_percentage").getAsFloat());
                    recentKaidahList.add(kaidah);
                }
            }

            // Update UI on main thread
            updateUIWithData(overview.has("rata_rata_skor") ?
                overview.get("rata_rata_skor").getAsFloat() : 0.0f, recentKaidahList);

        } else {
            android.util.Log.w("HomeFragment", "No overview data in JSON response");
            showErrorState();
        }
    }

    /**
     * Process Map response data (LinkedTreeMap from Gson)
     */
    private void processMapResponse(java.util.Map<String, Object> dataMap) {
        // Extract overview data
        if (dataMap.containsKey("overview")) {
            java.util.Map<String, Object> overview = (java.util.Map<String, Object>) dataMap.get("overview");

            // Update statistics with safe casting (handle both String and Number types)
            totalKaidah = safeParseInt(overview.get("total_kaidah"));
            kaidahSelesai = safeParseInt(overview.get("kaidah_selesai"));
            totalQuiz = safeParseInt(overview.get("total_sesi"));

            // Calculate progress percentage
            if (totalKaidah > 0) {
                progressPercentage = overview.containsKey("persentase_kemajuan") ?
                    safeParseFloat(overview.get("persentase_kemajuan")) :
                    ((float) kaidahSelesai / totalKaidah) * 100;
            }

            android.util.Log.d("HomeFragment", String.format(
                "API Data parsed (Map):\n" +
                "Total Kaidah: %d\n" +
                "Kaidah Selesai: %d\n" +
                "Total Quiz: %d\n" +
                "Progress: %.2f%%",
                totalKaidah, kaidahSelesai, totalQuiz, progressPercentage
            ));

            // Extract and process kaidah progress data
            java.util.List<com.khozin.pembelajarankaidah.data.model.MateriKaidah> recentKaidahList = new java.util.ArrayList<>();
            if (dataMap.containsKey("kaidah_progress")) {
                java.util.List<Object> kaidahProgress = (java.util.List<Object>) dataMap.get("kaidah_progress");

                // Convert to MateriKaidah objects
                for (int i = 0; i < Math.min(kaidahProgress.size(), 5); i++) {
                    try {
                        java.util.Map<String, Object> kaidahMap = (java.util.Map<String, Object>) kaidahProgress.get(i);
                        com.khozin.pembelajarankaidah.data.model.MateriKaidah kaidah = new com.khozin.pembelajarankaidah.data.model.MateriKaidah();
                        kaidah.setIdMateri(safeParseInt(kaidahMap.get("id_materi")));
                        kaidah.setJudulMateri((String) kaidahMap.get("judul_kaidah"));
                        kaidah.setDeskripsi((String) kaidahMap.get("deskripsi"));

                        // Handle completion_percentage - could be Double, String, or other Number type
                        if (kaidahMap.containsKey("completion_percentage")) {
                            kaidah.setProgressPercentage(safeParseInt(kaidahMap.get("completion_percentage")));
                        } else {
                            kaidah.setProgressPercentage(0);
                        }

                        recentKaidahList.add(kaidah);
                    } catch (Exception e) {
                        android.util.Log.e("HomeFragment", "Error processing kaidah item at index " + i, e);
                    }
                }
            }

            // Update UI on main thread
            updateUIWithData(overview.containsKey("rata_rata_skor") ?
                safeParseFloat(overview.get("rata_rata_skor")) : 0.0f, recentKaidahList);

        } else {
            android.util.Log.w("HomeFragment", "No overview data in Map response");
            showErrorState();
        }
    }

    /**
     * Safely parse int from various types (String, Number, etc.)
     */
    private int safeParseInt(Object value) {
        if (value == null) return 0;
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                android.util.Log.w("HomeFragment", "Cannot parse int from string: " + value);
                return 0;
            }
        }
        return 0;
    }

    /**
     * Safely parse float from various types (String, Number, etc.)
     */
    private float safeParseFloat(Object value) {
        if (value == null) return 0.0f;
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }
        if (value instanceof String) {
            try {
                return Float.parseFloat((String) value);
            } catch (NumberFormatException e) {
                android.util.Log.w("HomeFragment", "Cannot parse float from string: " + value);
                return 0.0f;
            }
        }
        return 0.0f;
    }

    /**
     * Update UI with processed data
     */
    private void updateUIWithData(float averageScore, java.util.List<com.khozin.pembelajarankaidah.data.model.MateriKaidah> recentKaidahList) {
        // Update UI on main thread
        if (isAdded() && getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                updateProgressUI(averageScore);

                // Update recent kaidah list
                if (!recentKaidahList.isEmpty()) {
                    recentKaidahAdapter.updateData(recentKaidahList);
                } else {
                    showEmptyState();
                }

                // Show success message
                android.util.Log.d("HomeFragment", "Progress data updated from API successfully");
                android.util.Log.d("HomeFragment", "Recent kaidah count: " + recentKaidahList.size());
            });
        }
    }

    /**
     * Update UI with progress data from API
     */
    private void updateUIWithProgressData(ChapterProgressResponse progressResponse) {
        if (progressResponse == null) {
            android.util.Log.e("HomeFragment", "Progress response is null");
            showErrorState();
            return;
        }

        try {
            // Update UI with progress data
            // For now, just log that we received the data
            android.util.Log.d("HomeFragment", "Progress data received: " + progressResponse.toString());

            // TODO: Update UI elements with actual progress data
            // For example, update statistics cards, progress bars, etc.

        } catch (Exception e) {
            android.util.Log.e("HomeFragment", "Error processing progress data: " + e.getMessage());
            showErrorState();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Refresh data when fragment resumes
        if (sessionManager.isLoggedIn()) {
            loadData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Clear resources
        if (recentKaidahAdapter != null) {
            recentKaidahAdapter = null;
        }
    }
}