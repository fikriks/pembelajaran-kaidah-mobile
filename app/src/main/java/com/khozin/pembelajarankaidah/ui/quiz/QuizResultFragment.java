package com.khozin.pembelajarankaidah.ui.quiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.khozin.pembelajarankaidah.R;
import com.khozin.pembelajarankaidah.data.model.SesiLatihan;

/**
 * Fragment untuk menampilkan hasil quiz setelah selesai
 */
public class QuizResultFragment extends Fragment {

    private static final String ARG_SESI_RESULT = "sesi_result";
    private static final String ARG_BAB_NAME = "bab_name";

    private TextView tvTitle;
    private TextView tvScore;
    private TextView tvCorrectAnswers;
    private TextView tvTotalQuestions;
    private TextView tvTimeTaken;
    private TextView tvPerformanceMessage;
    private Button btnBackToKaidah;
    private Button btnRetakeQuiz;

    private SesiLatihan sesiResult;
    private String babName;

    public QuizResultFragment() {
        // Required empty public constructor
    }

    public static QuizResultFragment newInstance(SesiLatihan sesiResult, String babName) {
        QuizResultFragment fragment = new QuizResultFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SESI_RESULT, sesiResult);
        args.putString(ARG_BAB_NAME, babName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sesiResult = (SesiLatihan) getArguments().getSerializable(ARG_SESI_RESULT);
            babName = getArguments().getString(ARG_BAB_NAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz_result, container, false);

        initViews(view);
        setupData();
        setupClickListeners();

        return view;
    }

    private void initViews(View view) {
        tvTitle = view.findViewById(R.id.tvTitle);
        tvScore = view.findViewById(R.id.tvScore);
        tvCorrectAnswers = view.findViewById(R.id.tvCorrectAnswers);
        tvTotalQuestions = view.findViewById(R.id.tvTotalQuestions);
        tvTimeTaken = view.findViewById(R.id.tvTimeTaken);
        tvPerformanceMessage = view.findViewById(R.id.tvPerformanceMessage);
        btnBackToKaidah = view.findViewById(R.id.btnBackToKaidah);
        btnRetakeQuiz = view.findViewById(R.id.btnRetakeQuiz);
    }

    private void setupData() {
        if (sesiResult != null) {
            // Set title dengan nama bab
            tvTitle.setText("Hasil Quiz " + (babName != null ? babName : ""));

            // Hitung dan tampilkan skor (dibulatkan menjadi integer)
            int score = Math.round(sesiResult.getSkor());
            tvScore.setText(score + "%");

            // Tampilkan jumlah jawaban benar dan total soal
            tvCorrectAnswers.setText(String.valueOf(sesiResult.getSoalBenar()));
            tvTotalQuestions.setText("/" + String.valueOf(sesiResult.getTotalSoal()));

            // Tampilkan waktu yang diperlukan
            Integer timeTakenObj = sesiResult.getDurasiDetik();
            int timeTaken = timeTakenObj != null ? timeTakenObj : 0;
            int minutes = timeTaken / 60;
            int seconds = timeTaken % 60;
            tvTimeTaken.setText(String.format("%02d:%02d", minutes, seconds));

            // Set pesan performa berdasarkan skor
            tvPerformanceMessage.setText(getPerformanceMessage(score));
        }
    }

    private void setupClickListeners() {
        btnBackToKaidah.setOnClickListener(v -> {
            // Kembali ke layar kaidah
            if (getActivity() != null) {
                // Start MainActivity with kaidah tab selected
                android.content.Intent intent = new android.content.Intent(getActivity(), com.khozin.pembelajarankaidah.MainActivity.class);
                intent.setFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP | android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("navigate_to_kaidah", true);
                startActivity(intent);

                // Finish current QuizActivity
                getActivity().finish();
            }
        });

        btnRetakeQuiz.setOnClickListener(v -> {
            // Mulai quiz lagi
            if (getActivity() != null) {
                // Tutup fragment hasil dan restart quiz
                getActivity().getSupportFragmentManager().popBackStack();

                // Restart quiz dengan bab yang sama
                if (sesiResult != null) {
                    QuizActivity quizActivity = (QuizActivity) getActivity();
                    quizActivity.restartQuiz();
                }
            }
        });
    }

    private String getPerformanceMessage(int score) {
        if (score >= 90) {
            return "Luar biasa! Anda telah menguasai materi ini dengan sangat baik.";
        } else if (score >= 80) {
            return "Bagus! Anda telah memahami materi dengan baik.";
        } else if (score >= 70) {
            return "Cukup baik. Anda dapat memperbaiki pemahaman dengan mencoba kembali.";
        } else if (score >= 60) {
            return "Perlu lebih banyak latihan untuk menguasai materi ini.";
        } else {
            return "Jangan menyerah! Coba lagi untuk hasil yang lebih baik.";
        }
    }
}