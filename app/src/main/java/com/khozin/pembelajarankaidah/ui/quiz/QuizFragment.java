package com.khozin.pembelajarankaidah.ui.quiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.content.ContextCompat;

import com.khozin.pembelajarankaidah.R;
import com.khozin.pembelajarankaidah.data.model.RiwayatBelajar;
import com.khozin.pembelajarankaidah.data.model.MateriKaidah;
import com.khozin.pembelajarankaidah.data.model.Bab;
import com.khozin.pembelajarankaidah.database.AppDatabase;
import com.khozin.pembelajarankaidah.utils.SessionManager;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * Quiz Fragment - Halaman quiz/latihan dengan prerequisite checking
 * Siswa harus menyelesaikan semua kaidah sebelum bisa mengakses quiz
 */
public class QuizFragment extends Fragment {

    private TextView tvTitle, tvSubtitle, tvStatusMessage;
    private ProgressBar progressBar;
    private LinearLayout llQuizContainer, llPrerequisiteContainer;
    private RecyclerView rvQuizList;
    private Button btnRefresh;

    private SessionManager sessionManager;
    private AppDatabase database;
    private View rootView;
    private QuizAdapter quizAdapter;
    private List<Bab> babList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_quiz, container, false);

        initViews();
        setupData();

        return rootView;
    }

    private void initViews() {
        sessionManager = new SessionManager(requireContext());
        database = AppDatabase.getDatabase(requireContext());

        // Initialize views
        tvTitle = rootView.findViewById(R.id.tv_quiz_title);
        tvSubtitle = rootView.findViewById(R.id.tv_quiz_subtitle);
        tvStatusMessage = rootView.findViewById(R.id.tv_status_message);
        progressBar = rootView.findViewById(R.id.progress_bar);
        llQuizContainer = rootView.findViewById(R.id.ll_quiz_container);
        llPrerequisiteContainer = rootView.findViewById(R.id.ll_prerequisite_container);
        rvQuizList = rootView.findViewById(R.id.rv_quiz_list);
        btnRefresh = rootView.findViewById(R.id.btn_refresh);

        // Setup RecyclerView
        setupRecyclerView();

        // Setup click listeners
        btnRefresh.setOnClickListener(v -> checkPrerequisites());
    }

    private void setupRecyclerView() {
        babList = new java.util.ArrayList<>();
        quizAdapter = new QuizAdapter(requireContext(), babList, new QuizAdapter.OnQuizClickListener() {
            @Override
            public void onQuizClick(Bab bab) {
                if (bab.isUnlocked()) {
                    startQuiz(bab);
                } else {
                    // Show message that quiz is locked
                    android.widget.Toast.makeText(requireContext(),
                        "Quiz " + bab.getNamaBab() + " terkunci. Selesaikan semua materi " + bab.getNamaBab() + " terlebih dahulu.",
                        android.widget.Toast.LENGTH_SHORT).show();
                }
            }
        });

        rvQuizList.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvQuizList.setAdapter(quizAdapter);
    }

    private void setupData() {
        checkPrerequisites();
    }

    private void checkPrerequisites() {
        // Show loading state
        progressBar.setVisibility(View.VISIBLE);
        tvStatusMessage.setText("Memeriksa prerequisite...");
        llQuizContainer.setVisibility(View.GONE);
        llPrerequisiteContainer.setVisibility(View.GONE);
        btnRefresh.setVisibility(View.GONE);

        // Run database check in background thread
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    int currentUserId = sessionManager.getUserId();

                    // Get all bab data from database
                    List<Bab> babList = database.babDao().getAllBabsSync();

                    // For each bab, calculate if it's available for quiz (progress >= 100%)
                    for (Bab bab : babList) {
                        // Check if all kaidah in this bab are completed
                        boolean babQuizAvailable = isQuizAvailableForBab(bab.getIdBab(), currentUserId);
                        bab.setUnlocked(babQuizAvailable);
                    }

                    // Count available quizzes
                    int availableQuizzes = 0;
                    int totalQuizzes = babList.size();
                    for (Bab bab : babList) {
                        if (bab.isUnlocked()) {
                            availableQuizzes++;
                        }
                    }

                    final int finalAvailableQuizzes = availableQuizzes;
                    final int finalTotalQuizzes = totalQuizzes;
                    final List<Bab> finalBabList = babList;

                    // Update UI on main thread
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);

                            if (finalBabList.isEmpty()) {
                                // No bab available
                                showErrorState("Tidak ada data bab tersedia. Silakan sinkronkan data terlebih dahulu.");
                            } else if (finalAvailableQuizzes > 0) {
                                // Some quizzes available - show dynamic quiz options
                                showDynamicQuizOptions(finalBabList, finalAvailableQuizzes, finalTotalQuizzes);
                            } else {
                                // No quizzes available - show prerequisite message
                                showPrerequisiteMessage(finalTotalQuizzes);
                            }
                        }
                    });

                } catch (Exception e) {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            showErrorState("Terjadi kesalahan saat memeriksa data: " + e.getMessage());
                        }
                    });
                }
            }
        });
    }

    /**
     * Check if quiz is available for specific bab
     */
    private boolean isQuizAvailableForBab(int babId, int userId) {
        try {
            // Get all materi for this bab
            List<MateriKaidah> materiList = database.materiKaidahDao().getMateriByBabSync(babId);

            if (materiList.isEmpty()) {
                return false;
            }

            // Get completed materi for this bab
            int completedCount = 0;
            for (MateriKaidah materi : materiList) {
                RiwayatBelajar riwayat = database.riwayatBelajarDao()
                    .getBySiswaAndMateriSync(userId, materi.getIdMateri());

                if (riwayat != null && "selesai".equals(riwayat.getStatus())) {
                    completedCount++;
                }
            }

            // Quiz available if all materi in bab are completed
            return completedCount >= materiList.size();

        } catch (Exception e) {
            android.util.Log.e("QuizFragment", "Error checking quiz availability for bab " + babId, e);
            return false;
        }
    }

    private void showDynamicQuizOptions(List<Bab> babData, int availableQuizzes, int totalQuizzes) {
        tvTitle.setText("Quiz Siap Dimulai!");
        tvSubtitle.setText("Quiz tersedia: " + availableQuizzes + " dari " + totalQuizzes + " bab");
        tvStatusMessage.setText("Klik quiz untuk memulai latihan");
        tvStatusMessage.setTextColor(ContextCompat.getColor(requireContext(), R.color.success_green));

        // Update adapter data
        babList.clear();
        babList.addAll(babData);
        quizAdapter.updateData(babList);

        llQuizContainer.setVisibility(View.VISIBLE);
        llPrerequisiteContainer.setVisibility(View.GONE);
        btnRefresh.setVisibility(View.GONE);
    }

    private void showPrerequisiteMessage(int totalQuizzes) {
        tvTitle.setText("Quiz Terkunci");
        tvSubtitle.setText("Anda perlu menyelesaikan semua materi di setiap bab untuk membuka quiz.");

        String message = "Progress: 0 dari " + totalQuizzes + " bab tersedia untuk quiz";
        tvStatusMessage.setText(message);
        tvStatusMessage.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary));

        llQuizContainer.setVisibility(View.GONE);
        llPrerequisiteContainer.setVisibility(View.VISIBLE);
        btnRefresh.setVisibility(View.VISIBLE);

        // Setup redirect to kaidah button
        Button btnToKaidah = rootView.findViewById(R.id.btn_to_kaidah);
        btnToKaidah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to kaidah tab
                requireActivity().findViewById(R.id.navigation_kaidah).performClick();
            }
        });
    }

    private void showErrorState(String errorMessage) {
        tvTitle.setText("Error");
        tvSubtitle.setText("Terjadi kesalahan saat memuat data quiz");
        tvStatusMessage.setText(errorMessage);
        tvStatusMessage.setTextColor(ContextCompat.getColor(requireContext(), R.color.error_red));

        llQuizContainer.setVisibility(View.GONE);
        llPrerequisiteContainer.setVisibility(View.GONE);
        btnRefresh.setVisibility(View.VISIBLE);
    }

    private void startQuiz(Bab bab) {
        android.util.Log.d("QuizFragment", "Starting quiz for bab: " + bab.getNamaBab());
        android.util.Log.d("QuizFragment", "Bab ID: " + bab.getIdBab());
        android.util.Log.d("QuizFragment", "Bab Urutan: " + bab.getUrutan());

        // Navigate to QuizActivity with selected bab data
        try {
            android.content.Intent intent = new android.content.Intent(requireContext(),
                com.khozin.pembelajarankaidah.ui.quiz.QuizActivity.class);
            intent.putExtra("bab_id", bab.getIdBab());
            intent.putExtra("bab_name", bab.getNamaBab());
            intent.putExtra("bab_urutan", bab.getUrutan());
            startActivity(intent);
        } catch (Exception e) {
            android.util.Log.e("QuizFragment", "Error starting quiz: " + e.getMessage(), e);
            android.widget.Toast.makeText(requireContext(),
                "Gagal memulai quiz: " + e.getMessage(),
                android.widget.Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when fragment becomes visible
        if (rootView != null) {
            checkPrerequisites();
        }
    }
}