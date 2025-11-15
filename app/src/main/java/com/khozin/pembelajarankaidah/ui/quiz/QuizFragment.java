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
import com.khozin.pembelajarankaidah.data.model.Bab;
import com.khozin.pembelajarankaidah.data.model.BabListResponse;
import com.khozin.pembelajarankaidah.data.model.ApiResponse;
import com.khozin.pembelajarankaidah.utils.SessionManager;
import com.khozin.pembelajarankaidah.network.RetrofitClient;

import java.util.List;
import java.util.ArrayList;
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
        tvStatusMessage.setText("Mengambil data dari server...");
        llQuizContainer.setVisibility(View.GONE);
        llPrerequisiteContainer.setVisibility(View.GONE);
        btnRefresh.setVisibility(View.GONE);

        // Get chapters from API
        String authToken = sessionManager.getAuthToken();

        // First get progress data from riwayat_belajar to determine unlock status
        RetrofitClient.getInstance()
                .getApiService()
                .getProgress(authToken)
                .enqueue(new retrofit2.Callback<ApiResponse<java.util.Map<String, Object>>>() {
                    @Override
                    public void onResponse(retrofit2.Call<ApiResponse<java.util.Map<String, Object>>> call,
                                           retrofit2.Response<ApiResponse<java.util.Map<String, Object>>> progressResponse) {

                        // Then get chapters list
                        RetrofitClient.getInstance()
                                .getApiService()
                                .getChapters(authToken)
                                .enqueue(new retrofit2.Callback<BabListResponse>() {
                                    @Override
                                    public void onResponse(retrofit2.Call<BabListResponse> call,
                                                           retrofit2.Response<BabListResponse> response) {
                                        progressBar.setVisibility(View.GONE);

                                        if (response.isSuccessful() && response.body() != null) {
                                            BabListResponse.ChapterData chapterData = response.body().getData();
                                            List<Bab> babList = chapterData != null ? chapterData.getChapters() : new java.util.ArrayList<>();

                                            // Set unlock status based on riwayat_belajar progress API data
                                            if (progressResponse.isSuccessful() && progressResponse.body() != null) {
                                                ApiResponse<java.util.Map<String, Object>> progressApi = progressResponse.body();
                                                if (progressApi != null && progressApi.getData() != null) {
                                                    java.util.Map<String, Object> progressData = progressApi.getData();
                                                    java.util.List<java.util.Map<String, Object>> kaidahProgress =
                                                        (java.util.List<java.util.Map<String, Object>>) progressData.get("kaidah_progress");

                                                    if (kaidahProgress != null) {
                                                        for (Bab bab : babList) {
                                                            // Find progress for this bab from riwayat_belajar
                                                            boolean isCompleted = false;
                                                            for (java.util.Map<String, Object> kaidah : kaidahProgress) {
                                                                String idMateri = String.valueOf(kaidah.get("id_materi"));
                                                                String status = (String) kaidah.get("status");
                                                                if (String.valueOf(bab.getIdBab()).equals(idMateri) &&
                                                                    "selesai".equals(status)) {
                                                                    isCompleted = true;
                                                                    break;
                                                                }
                                                            }
                                                            bab.setUnlocked(isCompleted);
                                                        }
                                                    }
                                                }
                                            } else {
                                                // If progress API fails, lock all quizzes
                                                for (Bab bab : babList) {
                                                    bab.setUnlocked(false);
                                                }
                                            }

                                            if (babList.isEmpty()) {
                                                showErrorState("Tidak ada data bab tersedia. Silakan sinkronkan data terlebih dahulu.");
                                            } else {
                                                showDynamicQuizOptions(babList, babList.size(), babList.size());
                                            }
                                        } else {
                                            showErrorState("Gagal mengambil data dari server. Kode: " + response.code());
                                        }
                                    }

                                    @Override
                                    public void onFailure(retrofit2.Call<BabListResponse> call,
                                                          Throwable t) {
                                        progressBar.setVisibility(View.GONE);
                                        showErrorState("Tidak dapat terhubung ke server: " + t.getMessage());
                                    }
                                });
                    }

                    @Override
                    public void onFailure(retrofit2.Call<ApiResponse<java.util.Map<String, Object>>> call,
                                          Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        showErrorState("Tidak dapat mengambil data progress: " + t.getMessage());
                    }
                });
    }

  
    private void showDynamicQuizOptions(List<Bab> babData, int availableQuizzes, int totalQuizzes) {
        // Check if fragment is still attached to context
        if (!isAdded() || getContext() == null) {
            android.util.Log.w("QuizFragment", "Fragment not attached to context, skipping UI update");
            return;
        }

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
        // Check if fragment is still attached to context
        if (!isAdded() || getContext() == null) {
            android.util.Log.w("QuizFragment", "Fragment not attached to context, skipping UI update");
            return;
        }

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
        // Check if fragment is still attached to context
        if (!isAdded() || getContext() == null) {
            android.util.Log.w("QuizFragment", "Fragment not attached to context, skipping error UI update");
            return;
        }

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

        // Prevent multiple rapid clicks
        if (rvQuizList != null) {
            rvQuizList.setEnabled(false);
        }

        // Navigate to QuizActivity with selected bab data
        try {
            // Add small delay to prevent rapid transition issues
            new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                try {
                    android.content.Intent intent = new android.content.Intent(requireContext(),
                        com.khozin.pembelajarankaidah.ui.quiz.QuizActivity.class);
                    intent.putExtra("bab_id", bab.getIdBab());
                    intent.putExtra("bab_name", bab.getNamaBab());
                    intent.putExtra("bab_urutan", bab.getUrutan());
                    intent.putExtra("total_questions", 10);
                    intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    // Add smooth transition animation
                    if (getActivity() != null) {
                        getActivity().overridePendingTransition(
                            R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                } catch (Exception e) {
                    android.util.Log.e("QuizFragment", "Error starting quiz in handler: " + e.getMessage(), e);
                    if (getContext() != null) {
                        android.widget.Toast.makeText(getContext(),
                            "Gagal memulai quiz: " + e.getMessage(),
                            android.widget.Toast.LENGTH_SHORT).show();
                    }
                    // Re-enable view if start fails
                    if (rvQuizList != null) {
                        rvQuizList.setEnabled(true);
                    }
                }
            }, 200); // 200ms delay for smoother transition

        } catch (Exception e) {
            android.util.Log.e("QuizFragment", "Error starting quiz: " + e.getMessage(), e);
            if (requireContext() != null) {
                android.widget.Toast.makeText(requireContext(),
                    "Gagal memulai quiz: " + e.getMessage(),
                    android.widget.Toast.LENGTH_SHORT).show();
            }
            // Re-enable view if handler setup fails
            if (rvQuizList != null) {
                rvQuizList.setEnabled(true);
            }
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