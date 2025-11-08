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
import com.khozin.pembelajarankaidah.database.AppDatabase;
import com.khozin.pembelajarankaidah.utils.SessionManager;
import com.khozin.pembelajarankaidah.database.entity.SesiLatihanStatistics;

import java.util.List;
import java.util.concurrent.Executors;

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
    private AppDatabase database;
    private KaidahSmallAdapter recentKaidahAdapter;

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
        database = AppDatabase.getDatabase(requireContext());
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
     * Load data from database
     */
    private void loadData() {
        loadStatistics();
        loadRecentKaidah();
        updateUI();
    }

    /**
     * Load statistics data
     */
    private void loadStatistics() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                // Get total kaidah
                totalKaidah = database.materiKaidahDao().getCount();

                // Get completed kaidah for current user
                int siswaId = sessionManager.getUserId();

                // Validate siswaId
                if (siswaId == -1) {
                    // User not logged in or invalid session
                    kaidahSelesai = 0;
                    totalQuiz = 0;
                } else {
                    // Count completed materi for current user only
                    kaidahSelesai = database.riwayatBelajarDao().countBySiswaAndStatus(siswaId, "selesai");

                    // Get total quiz sessions
                    totalQuiz = database.sesiLatihanDao().getTotalSesiSelesai(siswaId);
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

                // Get average score
                SesiLatihanStatistics stats = database.sesiLatihanDao().getSesiStatistics(siswaId);
                float averageScore = stats != null ? stats.getRataRataSkor() : 0.0f;

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
                List<MateriKaidah> recentKaidah = database.materiKaidahDao()
                        .getMateriWithProgressSync(siswaId);

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