package com.khozin.pembelajarankaidah.ui.quiz;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.khozin.pembelajarankaidah.R;
import com.khozin.pembelajarankaidah.adapter.JawabanAdapter;
import com.khozin.pembelajarankaidah.data.model.DetailJawabanSiswa;
import com.khozin.pembelajarankaidah.data.model.Jawaban;
import com.khozin.pembelajarankaidah.data.model.SesiLatihan;
import com.khozin.pembelajarankaidah.data.model.Soal;
import com.khozin.pembelajarankaidah.database.AppDatabase;
import com.khozin.pembelajarankaidah.database.dao.DetailJawabanSiswaDao;
import com.khozin.pembelajarankaidah.utils.LinearCongruentMethod;
import com.khozin.pembelajarankaidah.utils.SessionManager;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Activity untuk mengerjakan kuis/latihan soal
 * Menggunakan algoritma LCM untuk mengacak soal dan jawaban
 */
public class QuizActivity extends AppCompatActivity {

    // UI Components
    private TextView tvQuestionNumber;
    private TextView tvQuestionText;
    private LinearProgressIndicator progressBar;
    private RecyclerView rvJawaban;
    private Button btnNext;
    private TextView tvTimer;
    private LinearLayout llTimer;

    // Data
    private AppDatabase database;
    private SessionManager sessionManager;
    private QuizViewModel viewModel;
    private JawabanAdapter jawabanAdapter;
    private LinearCongruentMethod lcm;

    // Quiz State
    private int currentQuestionIndex = 0;
    private int totalQuestions = 10;
    private List<Soal> soalList;
    private List<Jawaban> currentJawabanList;
    private SesiLatihan currentSesi;
    private long sessionId;
    private long lcmSeed;

    // Timer
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 600000; // 10 menit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Get data from intent
        int kaidahId = getIntent().getIntExtra("kaidah_id", 0);
        totalQuestions = getIntent().getIntExtra("total_questions", 10);

        if (kaidahId == 0) {
            Toast.makeText(this, "Error: Materi tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupDatabase();
        setupRecyclerView();
        setupViewModel();
        setupLCM();

        // Start quiz session
        startQuizSession(kaidahId);
    }

    /**
     * Initialize views
     */
    private void initViews() {
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvQuestionText = findViewById(R.id.tvQuestionText);
        progressBar = findViewById(R.id.progressBar);
        rvJawaban = findViewById(R.id.rvJawaban);
        btnNext = findViewById(R.id.btnNext);
        tvTimer = findViewById(R.id.tvTimer);
        llTimer = findViewById(R.id.llTimer);

        // Set click listeners
        btnNext.setOnClickListener(v -> onNextQuestion());
        findViewById(R.id.btnClose).setOnClickListener(v -> showExitConfirmation());
    }

    /**
     * Setup database
     */
    private void setupDatabase() {
        sessionManager = new SessionManager(this);
        database = AppDatabase.getDatabase(this);
    }

    /**
     * Setup RecyclerView untuk jawaban
     */
    private void setupRecyclerView() {
        jawabanAdapter = new JawabanAdapter();
        rvJawaban.setLayoutManager(new LinearLayoutManager(this));
        rvJawaban.setAdapter(jawabanAdapter);

        // Set click listener untuk jawaban
        jawabanAdapter.setOnJawabanClickListener(this::onJawabanSelected);
    }

    /**
     * Setup ViewModel
     */
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        // Observe jawaban terpilih
        viewModel.getSelectedJawabanId().observe(this, jawabanId -> {
            jawabanAdapter.setSelectedJawaban(jawabanId);
            btnNext.setEnabled(jawabanId != -1);
        });

        // Observe loading state
        viewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                showLoading();
            } else {
                hideLoading();
            }
        });

        // Observe errors
        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Setup LCM algorithm
     */
    private void setupLCM() {
        // Generate seed dari timestamp + user_id
        long timestamp = System.currentTimeMillis();
        int userId = sessionManager.getUserId();
        lcmSeed = timestamp + userId;

        // Initialize LCM dengan parameter skripsi
        lcm = new LinearCongruentMethod(lcmSeed,
                SesiLatihan.LCM_A,
                SesiLatihan.LCM_C,
                SesiLatihan.LCM_M);
    }

    /**
     * Start quiz session
     */
    private void startQuizSession(int kaidahId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                // Get all soal untuk kaidah ini
                List<Soal> allSoal = database.soalDao().getByMateriIdSync(kaidahId);

                if (allSoal.isEmpty()) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Belum ada soal untuk materi ini", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                    return;
                }

                // Acak soal menggunakan LCM
                int[] shuffledIndices = lcm.shuffleIndices(allSoal.size());
                soalList = getShuffledList(allSoal, shuffledIndices);

                // Batasi jumlah soal
                if (soalList.size() > totalQuestions) {
                    soalList = soalList.subList(0, totalQuestions);
                }

                // Create sesi latihan
                currentSesi = new SesiLatihan();
                currentSesi.setIdSiswa(userId);
                currentSesi.setIdMateri(kaidahId);
                currentSesi.setSeedDigunakan(lcmSeed);
                currentSesi.setTotalSoal(soalList.size());
                currentSesi.setSoalBenar(0);
                currentSesi.setSkor(0.0f);
                currentSesi.setWaktuMulai(System.currentTimeMillis());
                currentSesi.setStatus("sedang_berjalan");

                // Save sesi to database
                sessionId = database.sesiLatihanDao().insert(currentSesi);

                // Start timer
                runOnUiThread(this::startTimer);

                // Load first question
                runOnUiThread(this::loadQuestion);

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error starting quiz: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }

    /**
     * Get shuffled list dari indices
     */
    private List<Soal> getShuffledList(List<Soal> originalList, int[] indices) {
        List<Soal> shuffled = new java.util.ArrayList<>();
        for (int index : indices) {
            if (index < originalList.size()) {
                shuffled.add(originalList.get(index));
            }
        }
        return shuffled;
    }

    /**
     * Start countdown timer
     */
    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                finishQuiz();
            }
        }.start();
    }

    /**
     * Update timer text
     */
    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        tvTimer.setText(String.format("%02d:%02d", minutes, seconds));

        // Change color when < 2 minutes
        if (timeLeftInMillis < 120000) {
            tvTimer.setTextColor(getResources().getColor(R.color.error));
        }
    }

    /**
     * Load current question
     */
    private void loadQuestion() {
        if (currentQuestionIndex >= soalList.size()) {
            finishQuiz();
            return;
        }

        Soal currentSoal = soalList.get(currentQuestionIndex);

        // Update UI
        tvQuestionNumber.setText("Soal " + (currentQuestionIndex + 1) + " dari " + soalList.size());
        tvQuestionText.setText(currentSoal.getPertanyaan());
        progressBar.setProgress((int) ((currentQuestionIndex + 1) * 100.0 / soalList.size()));

        // Load jawaban untuk soal ini
        loadJawaban(currentSoal);

        // Reset selection
        viewModel.setSelectedJawabanId(-1);
        btnNext.setEnabled(false);
    }

    /**
     * Load jawaban untuk soal tertentu
     */
    private void loadJawaban(Soal soal) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                List<Jawaban> allJawaban = database.jawabanDao().getBySoalIdSync(soal.getIdSoal());

                // Acak jawaban menggunakan LCM
                int[] shuffledIndices = lcm.shuffleIndices(allJawaban.size());
                currentJawabanList = getShuffledJawabanList(allJawaban, shuffledIndices);

                // Update adapter
                runOnUiThread(() -> {
                    jawabanAdapter.updateData(currentJawabanList);
                    hideLoading();
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error loading jawaban: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    /**
     * Get shuffled jawaban list
     */
    private List<Jawaban> getShuffledJawabanList(List<Jawaban> originalList, int[] indices) {
        List<Jawaban> shuffled = new java.util.ArrayList<>();
        for (int index : indices) {
            if (index < originalList.size()) {
                shuffled.add(originalList.get(index));
            }
        }
        return shuffled;
    }

    /**
     * Handle jawaban selection
     */
    private void onJawabanSelected(Jawaban jawaban) {
        viewModel.setSelectedJawabanId(jawaban.getIdPilihan());
    }

    /**
     * Handle next question
     */
    private void onNextQuestion() {
        Integer selectedJawabanId = viewModel.getSelectedJawabanId().getValue();
        if (selectedJawabanId == null || selectedJawabanId == -1) {
            Toast.makeText(this, "Pilih jawaban terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save jawaban ke database
        saveJawaban(selectedJawabanId);

        // Move to next question
        currentQuestionIndex++;
        if (currentQuestionIndex < soalList.size()) {
            loadQuestion();
        } else {
            finishQuiz();
        }
    }

    /**
     * Save jawaban to database
     */
    private void saveJawaban(int jawabanId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Soal currentSoal = soalList.get(currentQuestionIndex);

                // Check if jawaban is correct
                Jawaban jawaban = database.jawabanDao().getById(jawabanId);
                boolean isCorrect = (jawaban != null && jawaban.getIsBenar());

                // Save to detail_jawaban_siswa
                DetailJawabanSiswa detail = new DetailJawabanSiswa();
                detail.setIdSesi((int) sessionId);
                detail.setIdSoal(currentSoal.getIdSoal());
                detail.setIdPilihan(jawabanId);
                detail.setUrutanSoal(currentQuestionIndex + 1);
                detail.setIsBenar(isCorrect ? 1 : 0);
                detail.setWaktuJawab(System.currentTimeMillis());

                database.detailJawabanSiswaDao().insert(detail);

                // Update sesi progress
                if (isCorrect) {
                    currentSesi.setSoalBenar(currentSesi.getSoalBenar() + 1);
                }

                // Update skor
                float skor = (currentSesi.getSoalBenar() * 100.0f) / (currentQuestionIndex + 1);
                currentSesi.setSkor(skor);

                database.sesiLatihanDao().update(currentSesi);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Finish quiz
     */
    private void finishQuiz() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Update sesi
        currentSesi.setWaktuSelesai(System.currentTimeMillis());
        currentSesi.setDurasiDetik((int) ((currentSesi.getWaktuSelesai() - currentSesi.getWaktuMulai()) / 1000));
        currentSesi.setStatus("selesai");

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                database.sesiLatihanDao().update(currentSesi);

                // Navigate to result
                runOnUiThread(() -> {
                    // TODO: Navigate to QuizResultActivity
                    Toast.makeText(this, "Quiz selesai! Skor: " + currentSesi.getSkor(), Toast.LENGTH_LONG).show();
                    finish();
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error finishing quiz: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }

    /**
     * Show exit confirmation
     */
    private void showExitConfirmation() {
        // TODO: Show confirmation dialog
        finish();
    }

    /**
     * Show loading state
     */
    private void showLoading() {
        // TODO: Show loading indicator
    }

    /**
     * Hide loading state
     */
    private void hideLoading() {
        // TODO: Hide loading indicator
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Cancel timer
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Clear resources
        if (jawabanAdapter != null) {
            jawabanAdapter = null;
        }
    }

    @Override
    public void onBackPressed() {
        showExitConfirmation();
    }
}