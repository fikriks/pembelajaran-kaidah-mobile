package com.khozin.pembelajarankaidah.ui.quiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.khozin.pembelajarankaidah.R;
import com.khozin.pembelajarankaidah.adapter.JawabanAdapter;
import com.khozin.pembelajarankaidah.data.model.Soal;
import com.khozin.pembelajarankaidah.data.model.Jawaban;
import com.khozin.pembelajarankaidah.ui.quiz.QuizActivity;
import com.khozin.pembelajarankaidah.ui.quiz.QuizViewModel;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import android.widget.TextView;

/**
 * Fragment untuk menampilkan soal-soal quiz
 */
public class QuizQuestionsFragment extends Fragment {

    private RecyclerView rvJawaban;
    private MaterialButton btnNext;
    private TextView tvQuestionText;

    private QuizActivity quizActivity;
    private JawabanAdapter jawabanAdapter;
    private QuizViewModel viewModel;

    // Quiz state
    private int currentQuestionIndex = 0;
    private List<Soal> soalList;
    private List<Jawaban> currentJawabanList;
    private Map<Integer, List<Jawaban>> jawabanMap;

    public QuizQuestionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quizActivity = (QuizActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz_questions, container, false);

        initViews(view);
        setupRecyclerView();
        setupClickListeners();

        // Load first question if data is already set
        if (soalList != null && jawabanMap != null && soalList.size() > 0) {
            android.util.Log.d("QuizQuestionsFragment", "Data already available in onCreateView, loading first question");
            currentQuestionIndex = 0;
            loadQuestion();
        }

        return view;
    }

    private void initViews(View view) {
        rvJawaban = view.findViewById(R.id.rvJawaban);
        btnNext = view.findViewById(R.id.btnNext);
        tvQuestionText = view.findViewById(R.id.tvQuestionText);
    }

    private void setupRecyclerView() {
        jawabanAdapter = new JawabanAdapter();
        rvJawaban.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvJawaban.setAdapter(jawabanAdapter);

        jawabanAdapter.setOnJawabanClickListener(jawaban -> {
            // Handle null safety for getIdPilihan()
            Integer idPilihan = jawaban.getIdPilihan();
            onJawabanSelected(idPilihan != null ? idPilihan : -1);
        });
    }

    private void setupClickListeners() {
        btnNext.setOnClickListener(v -> {
            if (quizActivity != null) {
                quizActivity.onNextQuestion();
            }
        });
    }

    public void setQuizData(List<Soal> soalList, Map<Integer, List<Jawaban>> jawabanMap, QuizViewModel viewModel) {
        android.util.Log.d("QuizQuestionsFragment", "setQuizData called");
        android.util.Log.d("QuizQuestionsFragment", "soalList: " + (soalList != null ? soalList.size() : "null"));
        android.util.Log.d("QuizQuestionsFragment", "jawabanMap: " + (jawabanMap != null ? jawabanMap.size() : "null"));

        this.soalList = soalList;
        this.jawabanMap = jawabanMap;
        this.viewModel = viewModel;

        android.util.Log.d("QuizQuestionsFragment", "Data assigned successfully");

        // Load first question after data is set and views are ready
        if (getView() != null) {
            currentQuestionIndex = 0;
            loadQuestion();
        } else {
            android.util.Log.d("QuizQuestionsFragment", "View not ready yet, will load question in onCreateView");
        }
    }

    public void loadQuestion() {
        android.util.Log.d("QuizQuestionsFragment", "loadQuestion() called for index: " + currentQuestionIndex);

        if (soalList == null) {
            android.util.Log.e("QuizQuestionsFragment", "soalList is null in loadQuestion()");
            return;
        }

        if (currentQuestionIndex >= soalList.size()) {
            android.util.Log.e("QuizQuestionsFragment", "currentQuestionIndex (" + currentQuestionIndex + ") >= soalList.size() (" + soalList.size() + ")");
            return;
        }

        Soal currentSoal = soalList.get(currentQuestionIndex);
        android.util.Log.d("QuizQuestionsFragment", "Current soal: " + currentSoal.getIdSoal() + " - " + currentSoal.getPertanyaan());

        // Check if views are initialized first
        if (tvQuestionText == null || rvJawaban == null || btnNext == null) {
            android.util.Log.e("QuizQuestionsFragment", "Views not initialized - tvQuestionText: " +
                (tvQuestionText == null ? "null" : "ok") +
                ", rvJawaban: " + (rvJawaban == null ? "null" : "ok") +
                ", btnNext: " + (btnNext == null ? "null" : "ok"));
            return;
        }

        // Update UI
        if (quizActivity != null) {
            quizActivity.updateQuestionNumber(currentQuestionIndex + 1, soalList.size());
        }

        tvQuestionText.setText(currentSoal.getPertanyaan());

        // Load jawaban untuk soal ini
        loadJawaban(currentSoal);

        // Reset selection
        if (viewModel != null) {
            viewModel.setSelectedJawabanId(-1);
        }

        btnNext.setEnabled(false);

        android.util.Log.d("QuizQuestionsFragment", "Question loaded successfully");
    }

    private void loadJawaban(Soal soal) {
        android.util.Log.d("QuizQuestionsFragment", "Loading jawaban for soal ID: " + soal.getIdSoal());

        if (jawabanMap == null) {
            android.util.Log.e("QuizQuestionsFragment", "jawabanMap is null");
            return;
        }

        if (jawabanAdapter == null) {
            android.util.Log.e("QuizQuestionsFragment", "jawabanAdapter is null");
            return;
        }

        currentJawabanList = jawabanMap.get(soal.getIdSoal());

        if (currentJawabanList == null) {
            android.util.Log.e("QuizQuestionsFragment", "No jawaban list found for soal ID: " + soal.getIdSoal());
            android.util.Log.e("QuizQuestionsFragment", "Available soal IDs in jawabanMap: " + jawabanMap.keySet().toString());
            return;
        }

        android.util.Log.d("QuizQuestionsFragment", "Found " + currentJawabanList.size() + " jawaban items");

        // Log each jawaban
        for (int i = 0; i < currentJawabanList.size(); i++) {
            Jawaban j = currentJawabanList.get(i);
            android.util.Log.d("QuizQuestionsFragment", "Jawaban " + i + ": " + j.getJawaban() + " (ID: " + j.getIdPilihan() + ", benar: " + j.isBenar() + ")");
        }

        jawabanAdapter.updateData(currentJawabanList);
        android.util.Log.d("QuizQuestionsFragment", "Updated jawaban adapter, new item count: " + jawabanAdapter.getItemCount());
    }

    public void onJawabanSelected(int jawabanId) {
        if (viewModel != null) {
            viewModel.setSelectedJawabanId(jawabanId);
        }
        if (btnNext != null) {
            btnNext.setEnabled(true);
        }
    }

    public void setSelectedJawaban(int jawabanId) {
        android.util.Log.d("QuizQuestionsFragment", "setSelectedJawaban called with: " + jawabanId);
        if (jawabanAdapter != null) {
            jawabanAdapter.setSelectedJawaban(jawabanId);
            android.util.Log.d("QuizQuestionsFragment", "setSelectedJawaban completed successfully");
        } else {
            android.util.Log.e("QuizQuestionsFragment", "jawabanAdapter is null in setSelectedJawaban");
        }
    }

    public void updateJawabanAdapter(List<Jawaban> jawabanList) {
        if (jawabanAdapter != null) {
            jawabanAdapter.updateData(jawabanList);
        }
    }

    public void setCurrentQuestionIndex(int index) {
        this.currentQuestionIndex = index;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void updateButtonEnabled(boolean enabled) {
        if (btnNext != null) {
            btnNext.setEnabled(enabled);
        }
    }

    public void updateButtonText(String text) {
        if (btnNext != null) {
            btnNext.setText(text);
        }
    }

    public int getSelectedJawabanId() {
        return viewModel != null ? viewModel.getSelectedJawabanId().getValue() : -1;
    }

    public List<Jawaban> getCurrentJawabanList() {
        return currentJawabanList;
    }
}