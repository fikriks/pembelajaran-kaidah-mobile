package com.khozin.pembelajarankaidah.ui.quiz;

import android.annotation.SuppressLint;
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
import com.khozin.pembelajarankaidah.data.model.Jawaban;
import com.khozin.pembelajarankaidah.data.model.SesiLatihan;
import com.khozin.pembelajarankaidah.data.model.Soal;
import com.khozin.pembelajarankaidah.data.model.Siswa;
import com.khozin.pembelajarankaidah.data.model.ApiResponse;
import com.khozin.pembelajarankaidah.data.model.StartSesiResponse;
import com.khozin.pembelajarankaidah.data.model.FinishSesiResponse;
import com.khozin.pembelajarankaidah.network.RetrofitClient;
import com.khozin.pembelajarankaidah.network.ApiConstants;
import com.khozin.pembelajarankaidah.data.remote.ApiService;
import com.khozin.pembelajarankaidah.utils.LinearCongruentMethod;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.khozin.pembelajarankaidah.utils.SessionManager;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Activity untuk mengerjakan kuis/latihan soal
 * Menggunakan algoritma LCM untuk mengacak soal dan jawaban
 */
public class QuizActivity extends AppCompatActivity {

    // UI Components
    private TextView tvQuestionNumber;
    private TextView tvTimer;
    private LinearLayout llTimer;
    private LinearProgressIndicator progressBar;

    // Fragment components
    private QuizQuestionsFragment quizQuestionsFragment;

    // Data
        private SessionManager sessionManager;
    private QuizViewModel viewModel;
    // JawabanAdapter is now managed by QuizQuestionsFragment
    private LinearCongruentMethod lcm;

    // Quiz State
    private int currentQuestionIndex = 0;
    private int totalQuestions = 10;
    private int babId; // Store bab ID for API calls
    private List<Soal> soalList;
    private List<Jawaban> currentJawabanList;
    private SesiLatihan currentSesi;
    private int sessionId; // API session ID from startSesi
    private boolean sessionStarted = false; // Track if session has been started
    private long lcmSeed;

    // API Data
    private java.util.Map<Integer, java.util.List<Jawaban>> jawabanMap = new java.util.HashMap<>();

    // Timer
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 600000; // 10 menit

    // Exit state
    private boolean isExiting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_quiz);

            // Add small delay to prevent window focus issues
            new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                initializeQuizActivity();
            }, 100);

        } catch (Exception e) {
            android.util.Log.e("QuizActivity", "Error in onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "Gagal memuat quiz: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initializeQuizActivity() {
        try {
            // Get data from intent - support both old and new format
            android.content.Intent intent = getIntent();
            if (intent == null) {
                showErrorAndFinish("Intent tidak valid");
                return;
            }

            int kaidahId = intent.getIntExtra("kaidah_id", 0);
            this.babId = intent.getIntExtra("bab_id", 0);
            String babName = intent.getStringExtra("bab_name");
            int babUrutan = intent.getIntExtra("bab_urutan", 0);
            totalQuestions = intent.getIntExtra("total_questions", 10);

            // Prioritize bab_id over kaidah_id (new format)
            if (this.babId != 0) {
                kaidahId = this.babId;
                android.util.Log.d("QuizActivity", "Starting quiz for bab: " + babName + " (ID: " + this.babId + ")");
            } else {
                android.util.Log.d("QuizActivity", "Starting quiz for kaidah ID: " + kaidahId);
            }

            if (kaidahId == 0) {
                showErrorAndFinish("Error: Bab/Materi tidak ditemukan");
                return;
            }

            // Initialize components
            initViews();
            // setupDatabase(); // REMOVED - API only approach
            sessionManager = new SessionManager(this); // Initialize SessionManager
            setupViewModel();
            setupLCM();

            // Load quiz questions (session will start when user answers first question)
            loadQuizQuestions(kaidahId);

        } catch (Exception e) {
            android.util.Log.e("QuizActivity", "Error initializing quiz activity: " + e.getMessage(), e);
            showErrorAndFinish("Gagal menginisialisasi quiz: " + e.getMessage());
        }
    }

    private void showErrorAndFinish(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        // Add delay before finish to prevent rapid transition issues
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            finish();
        }, 500);
    }

    /**
     * Initialize views
     */
    private void initViews() {
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvTimer = findViewById(R.id.tvTimer);
        llTimer = findViewById(R.id.llTimer);
        progressBar = findViewById(R.id.progressBar);

        // Set click listeners
        findViewById(R.id.btnClose).setOnClickListener(v -> showExitConfirmation());
    }

    // REMOVED: setupDatabase method - API only approach
    // SessionManager is now initialized in initSessionManager() method

    
    /**
     * Setup ViewModel
     */
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        // Observe jawaban terpilih
        viewModel.getSelectedJawabanId().observe(this, jawabanId -> {
            if (quizQuestionsFragment != null && jawabanId != null) {
                quizQuestionsFragment.setSelectedJawaban(jawabanId);
                quizQuestionsFragment.updateButtonEnabled(jawabanId != -1);
            }
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
        lcm = new LinearCongruentMethod(lcmSeed);
    }

    /**
     * Load quiz questions from API (session will start when user answers first question)
     */
    private void loadQuizQuestions(int kaidahId) {
        // Show loading
        runOnUiThread(() -> {
            // Loading toast dihilangkan
        });

        // Get random soal from API
        Map<String, Object> request = new java.util.HashMap<>();
        request.put("id_bab", kaidahId);
        request.put("jumlah_soal", totalQuestions);
        request.put("user_id", sessionManager.getUserId());

        RetrofitClient.getInstance()
                .getApiService()
                .getRandomSoal(request)
                .enqueue(new retrofit2.Callback<ApiResponse<Map<String, Object>>>() {
                    @Override
                    public void onResponse(retrofit2.Call<ApiResponse<Map<String, Object>>> call, retrofit2.Response<ApiResponse<Map<String, Object>>> response) {
                        try {
                            if (response.isSuccessful() && response.body() != null) {
                                ApiResponse<Map<String, Object>> apiResponse = response.body();

                                if ("success".equals(apiResponse.getStatus())) {
                                    java.util.Map<String, Object> data = apiResponse.getData();
                                    java.util.List<java.util.Map<String, Object>> soalDataList =
                                        (java.util.List<java.util.Map<String, Object>>) data.get("soal");

                                    if (soalDataList != null && !soalDataList.isEmpty()) {
                                        // Convert API response to Soal objects
                                        soalList = convertApiSoalToSoalObjects(soalDataList);

                                        // Create sesi latihan (session will be started on first answer)
                                        currentSesi = new SesiLatihan();
                                        currentSesi.setIdSiswa(sessionManager.getUserId());
                                        currentSesi.setIdBab(kaidahId);
                                        currentSesi.setSeedDigunakan(lcmSeed);
                                        currentSesi.setTotalSoal(soalList.size());
                                        currentSesi.setSoalBenar(0);
                                        currentSesi.setSkor(0.0f);
                                        currentSesi.setWaktuMulai(String.valueOf(System.currentTimeMillis()));
                                        currentSesi.setStatus("menunggu"); // Status: waiting for session to start

                                        // Start timer and show quiz questions fragment (session will start on first answer)
                                        startTimer();
                                        showQuizQuestionsFragment();
                                    } else {
                                        runOnUiThread(() -> {
                                            Toast.makeText(QuizActivity.this, "Tidak ada soal tersedia untuk bab ini", Toast.LENGTH_SHORT).show();
                                            finish();
                                        });
                                    }
                                } else {
                                    runOnUiThread(() -> {
                                        Toast.makeText(QuizActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                        finish();
                                    });
                                }
                            } else {
                                runOnUiThread(() -> {
                                    Toast.makeText(QuizActivity.this, "Gagal mengambil data soal", Toast.LENGTH_SHORT).show();
                                    finish();
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(() -> {
                                Toast.makeText(QuizActivity.this, "Error processing quiz data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                finish();
                            });
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<ApiResponse<Map<String, Object>>> call, Throwable t) {
                        runOnUiThread(() -> {
                            Toast.makeText(QuizActivity.this, "Koneksi gagal: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    }
                });
    }

    /**
     * Convert API soal response to Soal objects
     */
    private java.util.List<Soal> convertApiSoalToSoalObjects(java.util.List<java.util.Map<String, Object>> soalDataList) {
        java.util.List<Soal> soalList = new java.util.ArrayList<>();

        for (java.util.Map<String, Object> soalData : soalDataList) {
            try {
                Soal soal = new Soal();
                soal.setIdSoal(Integer.parseInt(soalData.get("id_soal").toString()));
                soal.setIdBab(Integer.parseInt(soalData.get("id_bab").toString()));
                soal.setPertanyaan(soalData.get("pertanyaan").toString());
                soal.setTipeSoal(soalData.get("tipe_soal").toString());
                soal.setTingkatKesulitan(soalData.get("tingkat_kesulitan").toString());
                soal.setPoin(Integer.parseInt(soalData.get("poin").toString()));

                // Extract jawaban data from API response
                java.util.List<java.util.Map<String, Object>> jawabanDataList =
                        (java.util.List<java.util.Map<String, Object>>) soalData.get("pilihan_jawaban");

                if (jawabanDataList != null && !jawabanDataList.isEmpty()) {
                    java.util.List<Jawaban> jawabanList = new java.util.ArrayList<>();

                    android.util.Log.d("QuizActivity", "Found " + jawabanDataList.size() + " jawaban for soal ID: " + soal.getIdSoal());

                    for (java.util.Map<String, Object> jawabanData : jawabanDataList) {
                        try {
                            Jawaban jawaban = new Jawaban();
                            jawaban.setIdPilihan(Integer.parseInt(jawabanData.get("id_pilihan").toString()));
                            jawaban.setIdSoal(Integer.parseInt(jawabanData.get("id_soal").toString()));
                            jawaban.setJawabanText(jawabanData.get("teks_jawaban").toString());
                            jawaban.setBenar("1".equals(jawabanData.get("is_benar").toString()));
                            jawaban.setUrutan(String.valueOf(Integer.parseInt(jawabanData.get("urutan").toString())));

                            android.util.Log.d("QuizActivity", "Added jawaban: " + jawaban.getJawaban() + " (benar: " + jawaban.isBenar() + ")");

                            jawabanList.add(jawaban);
                        } catch (Exception e) {
                            e.printStackTrace();
                            android.util.Log.e("QuizActivity", "Error converting jawaban data: " + e.getMessage());
                            android.util.Log.e("QuizActivity", "Jawaban data: " + jawabanData.toString());
                        }
                    }

                    // Store jawaban list in map
                    jawabanMap.put(soal.getIdSoal(), jawabanList);
                    android.util.Log.d("QuizActivity", "Stored " + jawabanList.size() + " jawaban in map for soal ID: " + soal.getIdSoal());
                } else {
                    android.util.Log.w("QuizActivity", "No jawaban data found for soal ID: " + soal.getIdSoal());
                }

                soalList.add(soal);
            } catch (Exception e) {
                e.printStackTrace();
                android.util.Log.e("QuizActivity", "Error converting soal data: " + e.getMessage());
            }
        }

        return soalList;
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
     * Load jawaban untuk soal tertentu
     */
    private void loadJawaban(Soal soal) {
        try {
            // Get jawaban from API data (already stored in jawabanMap)
            List<Jawaban> allJawaban = jawabanMap.get(soal.getIdSoal());

            if (allJawaban == null || allJawaban.isEmpty()) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Tidak ada jawaban tersedia untuk soal ini", Toast.LENGTH_SHORT).show();
                });
                return;
            }

            // Acak jawaban menggunakan LCM
            int[] shuffledIndices = lcm.shuffleIndices(allJawaban.size());
            currentJawabanList = getShuffledJawabanList(allJawaban, shuffledIndices);

            // Update adapter on main thread
            runOnUiThread(() -> {
                if (quizQuestionsFragment != null) {
                    quizQuestionsFragment.updateJawabanAdapter(currentJawabanList);
                }
                hideLoading();
            });

        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(() -> {
                Toast.makeText(this, "Error loading jawaban: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
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
        Integer idPilihan = jawaban.getIdPilihan();
        viewModel.setSelectedJawabanId(idPilihan != null ? idPilihan : -1);
    }

    /**
     * Handle next question
     */
    public void onNextQuestion() {
        if (quizQuestionsFragment == null) {
            return;
        }

        int selectedJawabanId = quizQuestionsFragment.getSelectedJawabanId();
        if (selectedJawabanId == -1) {
            Toast.makeText(this, "Pilih jawaban terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        // Start session on first answer (sesi start ketika siswa klik soalnya)
        if (!sessionStarted) {
            startQuizSession();
            sessionStarted = true;
        }

        // Jawaban processed via API
        saveJawaban(selectedJawabanId);

        // Move to next question
        currentQuestionIndex++;
        if (currentQuestionIndex < soalList.size()) {
            // Sync question index with fragment
            quizQuestionsFragment.setCurrentQuestionIndex(currentQuestionIndex);
            quizQuestionsFragment.loadQuestion();
        } else {
            finishQuiz();
        }
    }

    /**
     * Track jawaban via API with backend submission
     */
    private void saveJawaban(int jawabanId) {
        try {
            Soal currentSoal = soalList.get(currentQuestionIndex);
            android.util.Log.d("QuizActivity", "Processing answer for question " + currentSoal.getIdSoal() + ", selected answer: " + jawabanId);

            // Check if jawaban is correct from API data
            List<Jawaban> jawabanList = quizQuestionsFragment.getCurrentJawabanList();
            boolean isCorrect = false;

            if (jawabanList != null) {
                android.util.Log.d("QuizActivity", "Found " + jawabanList.size() + " answer choices");
                for (Jawaban jawaban : jawabanList) {
                    Integer idPilihan = jawaban.getIdPilihan();
                    int idPilihanValue = idPilihan != null ? idPilihan : -1;
                    android.util.Log.d("QuizActivity", "Answer choice ID: " + idPilihanValue + ", isBenar: " + jawaban.isBenar() + ", text: " + jawaban.getJawaban());
                    if (idPilihanValue == jawabanId) {
                        isCorrect = jawaban.isBenar();
                        android.util.Log.d("QuizActivity", "Selected answer isCorrect: " + isCorrect);
                        break;
                    }
                }
            } else {
                android.util.Log.e("QuizActivity", "jawabanList is null!");
            }

            // Update local progress counter for UI navigation only (not for scoring)
            currentSesi.setJumlahSoalDijawab(currentQuestionIndex + 1);
            android.util.Log.d("QuizActivity", "Answer processed: " + (isCorrect ? "Correct" : "Incorrect") + ", Total answered: " + currentSesi.getJumlahSoalDijawab());

            // Submit answer to backend API
            submitJawabanToBackend(currentSoal.getIdSoal(), jawabanId);

        } catch (Exception e) {
            android.util.Log.e("QuizActivity", "Error in saveJawaban: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Submit individual answer to backend via API
     */
    private void submitJawabanToBackend(int idSoal, int idPilihan) {
        if (sessionManager == null || !sessionManager.isLoggedIn()) {
            android.util.Log.e("QuizActivity", "Cannot submit answer: Not logged in");
            return;
        }

        String authToken = sessionManager.getAuthToken();
        int sessionId = currentSesi.getIdSesi();
        android.util.Log.d("QuizActivity", "Submitting answer to backend: sessionId=" + sessionId + ", idSoal=" + idSoal + ", idPilihan=" + idPilihan);

        if (sessionId <= 0) {
            android.util.Log.e("QuizActivity", "Cannot submit answer: sessionId is invalid (" + sessionId + ")");
            return;
        }

        try {
            Map<String, Object> jawabanRequest = new java.util.HashMap<>();
            jawabanRequest.put("id_soal", idSoal);
            jawabanRequest.put("id_pilihan", idPilihan);
            android.util.Log.d("QuizActivity", "Request payload: " + jawabanRequest.toString());

            // Use custom OkHttpClient with Authorization header for answer submission
            OkHttpClient clientWithAuth = RetrofitClient.getInstance()
                    .getOkHttpClient()
                    .newBuilder()
                    .addInterceptor(chain -> {
                        okhttp3.Request originalRequest = chain.request();
                        okhttp3.Request.Builder requestBuilder = originalRequest.newBuilder()
                                .header("Authorization", authToken)
                                .method(originalRequest.method(), originalRequest.body());
                        return chain.proceed(requestBuilder.build());
                    })
                    .build();

            // Create new Retrofit instance with auth client
            retrofit2.Retrofit retrofitWithAuth = new retrofit2.Retrofit.Builder()
                    .baseUrl(ApiConstants.BASE_URL)
                    .client(clientWithAuth)
                    .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                    .build();

            ApiService apiServiceWithAuth = retrofitWithAuth.create(ApiService.class);
            apiServiceWithAuth.submitJawaban(sessionId, jawabanRequest)
                    .enqueue(new retrofit2.Callback<ApiResponse<Map<String, Object>>>() {
                        @Override
                        public void onResponse(retrofit2.Call<ApiResponse<Map<String, Object>>> call,
                                              retrofit2.Response<ApiResponse<Map<String, Object>>> response) {
                            try {
                                if (response.isSuccessful() && response.body() != null) {
                                    ApiResponse<Map<String, Object>> apiResponse = response.body();
                                    android.util.Log.d("QuizActivity", "Answer submitted successfully: " + apiResponse.getMessage());
                                } else {
                                    android.util.Log.w("QuizActivity", "Failed to submit answer. HTTP: " + response.code());
                                    if (response.errorBody() != null) {
                                        try {
                                            String errorBody = response.errorBody().string();
                                            android.util.Log.e("QuizActivity", "Error response: " + errorBody);
                                        } catch (Exception e) {
                                            android.util.Log.e("QuizActivity", "Error reading error body", e);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                android.util.Log.e("QuizActivity", "Error processing answer submission response", e);
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<ApiResponse<Map<String, Object>>> call,
                                              Throwable t) {
                            android.util.Log.e("QuizActivity", "Network error when submitting answer", t);
                        }
                    });

        } catch (Exception e) {
            android.util.Log.e("QuizActivity", "Error preparing answer submission", e);
        }
    }

    /**
     * Finish quiz
     */
    private void finishQuiz() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Update sesi
        long waktuSelesai = System.currentTimeMillis();
        currentSesi.setWaktuSelesai(String.valueOf(waktuSelesai));

        // Calculate duration in seconds
        try {
            long waktuMulai = Long.parseLong(currentSesi.getWaktuMulai());
            int durasi = (int) ((waktuSelesai - waktuMulai) / 1000);
            currentSesi.setDurasiDetik(durasi);
        } catch (NumberFormatException e) {
            // If parsing fails, set duration to 0
            currentSesi.setDurasiDetik(0);
        }
        currentSesi.setStatus("selesai");

        // Save quiz result to server via API
        saveQuizResultToServer();
    }

    /**
     * Show exit confirmation
     */
    private void showExitConfirmation() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Keluar dari Quiz");
        builder.setMessage("Apakah Anda yakin ingin keluar dari quiz? Progress Anda akan hilang.");
        builder.setPositiveButton("Ya", (dialog, which) -> {
            isExiting = true;
            finish();
        });
        builder.setNegativeButton("Tidak", (dialog, which) -> {
            dialog.dismiss();
            // Handle the negative case to ensure proper flow
            isExiting = false;
        });
        builder.setOnCancelListener(dialog -> {
            // Handle dialog cancellation
            isExiting = false;
        });
        builder.show();
    }

    /**
     * Show loading state
     */
    private void showLoading() {
        // TODO: Show loading indicator
    }

    /**
     * Start quiz session via API
     */
    private void startQuizSession() {
        if (sessionManager == null) {
            Toast.makeText(this, "Session tidak tersedia", Toast.LENGTH_SHORT).show();
            return;
        }

        String authToken = sessionManager.getAuthToken();
        if (authToken == null) {
            Toast.makeText(this, "Anda belum login", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create session request to start a new quiz session
        java.util.Map<String, Object> sesiRequest = new java.util.HashMap<>();
        sesiRequest.put("id_bab", babId); // API expects id_bab
        sesiRequest.put("jumlah_soal", currentSesi.getTotalSoal()); // Changed from total_soal to jumlah_soal
        sesiRequest.put("waktu_mulai", currentSesi.getWaktuMulai());
        sesiRequest.put("status", "sedang_berjalan");

        android.util.Log.d("QuizActivity", "Session request data: " + sesiRequest.toString());

        android.util.Log.d("QuizActivity", "Starting quiz session API call...");

        // First try to finish any existing active session
        RetrofitClient.getInstance()
                .getApiService()
                .finishActiveSession()
                .enqueue(new retrofit2.Callback<ApiResponse<FinishSesiResponse>>() {
                    @Override
                    public void onResponse(retrofit2.Call<ApiResponse<FinishSesiResponse>> call,
                                           retrofit2.Response<ApiResponse<FinishSesiResponse>> response) {
                        android.util.Log.d("QuizActivity", "Finished existing session. HTTP: " + response.code());

                        // Now create new session regardless of whether finish succeeded
                        createNewSession(sesiRequest);
                    }

                    @Override
                    public void onFailure(retrofit2.Call<ApiResponse<FinishSesiResponse>> call,
                                          Throwable t) {
                        android.util.Log.e("QuizActivity", "Failed to finish existing session", t);
                        // Continue with creating new session anyway
                        createNewSession(sesiRequest);
                    }
                });
    }

    private void createNewSession(java.util.Map<String, Object> sesiRequest) {
        android.util.Log.d("QuizActivity", "Creating new session with data: " + sesiRequest.toString());

        RetrofitClient.getInstance()
                .getApiService()
                .startSesi(sesiRequest)
                .enqueue(new retrofit2.Callback<ApiResponse<StartSesiResponse>>() {
                    @Override
                    public void onResponse(retrofit2.Call<ApiResponse<StartSesiResponse>> call,
                                           retrofit2.Response<ApiResponse<StartSesiResponse>> response) {
                        android.util.Log.d("QuizActivity", "Session API response received. Successful: " + response.isSuccessful() + ", HTTP: " + response.code());
                        if (response.isSuccessful() && response.body() != null) {
                            ApiResponse<StartSesiResponse> apiResponse = response.body();
                            android.util.Log.d("QuizActivity", "API Response status: " + apiResponse.getStatus());
                            if ("success".equals(apiResponse.getStatus())) {
                                StartSesiResponse startResponse = apiResponse.getData();
                                SesiLatihan startedSesi = startResponse.getSesi();
                                if (startedSesi != null) {
                                    sessionId = startedSesi.getIdSesi();
                                    android.util.Log.d("QuizActivity", "Quiz session started with ID: " + sessionId);

                                    // Update local session with server data
                                    currentSesi.setIdSesi(sessionId);
                                    currentSesi.setIdSiswa(startedSesi.getIdSiswa());
                                    currentSesi.setSeedDigunakan(startedSesi.getSeedDigunakan());
                                    currentSesi.setStatus("sedang_berjalan"); // Now session is actually started
                                }
                            } else {
                                android.util.Log.e("QuizActivity", "Failed to start session: " + apiResponse.getMessage());
                                // For development, continue with local session
                                sessionId = -1; // Indicate API session not started
                            }
                        } else {
                            android.util.Log.e("QuizActivity", "Failed to start session. HTTP: " + response.code());
                            // Try to get error body
                            try {
                                if (response.errorBody() != null) {
                                    String errorBody = response.errorBody().string();
                                    android.util.Log.e("QuizActivity", "Error response body: " + errorBody);
                                }
                            } catch (Exception e) {
                                android.util.Log.e("QuizActivity", "Could not read error body", e);
                            }
                            // For development, continue with local session
                            sessionId = -1; // Indicate API session not started
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<ApiResponse<StartSesiResponse>> call,
                                          Throwable t) {
                        android.util.Log.e("QuizActivity", "Network error when starting session", t);
                        // For development, continue with local session
                        sessionId = -1; // Indicate API session not started
                    }
                });
    }

    /**
     * Save quiz result to server via API using finishActiveSession API
     */
    private void saveQuizResultToServer() {
        // Only save if session was started
        if (!sessionStarted) {
            android.util.Log.d("QuizActivity", "No session was started, skipping server save");
            // Create a fallback session result with local data if no session was started
            if (currentSesi != null) {
                android.util.Log.d("QuizActivity", "Using local session data for results");
                navigateToResult(currentSesi);
            } else {
                android.util.Log.w("QuizActivity", "No session data available, finishing activity");
                finish();
            }
            return;
        }

        if (sessionManager == null) {
            Toast.makeText(this, "Session tidak tersedia", Toast.LENGTH_SHORT).show();
            navigateToResult();
            return;
        }

        String authToken = sessionManager.getAuthToken();
        if (authToken == null) {
            Toast.makeText(this, "Anda belum login", Toast.LENGTH_SHORT).show();
            navigateToResult();
            return;
        }

        // Show loading
        showLoading();

        // Use finishActiveSession API to complete the active session (no ID needed)

        // Create a custom OkHttpClient with Authorization header for this request
        OkHttpClient clientWithAuth = RetrofitClient.getInstance()
                .getOkHttpClient()
                .newBuilder()
                .addInterceptor(chain -> {
                    okhttp3.Request originalRequest = chain.request();
                    okhttp3.Request.Builder requestBuilder = originalRequest.newBuilder()
                            .header("Authorization", authToken)
                            .method(originalRequest.method(), originalRequest.body());
                    return chain.proceed(requestBuilder.build());
                })
                .build();

        // Create a temporary Retrofit instance with the authenticated client
        Retrofit retrofitWithAuth = new Retrofit.Builder()
                .baseUrl(com.khozin.pembelajarankaidah.network.ApiConstants.BASE_URL)
                .client(clientWithAuth)
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build();

        com.khozin.pembelajarankaidah.data.remote.ApiService apiServiceWithAuth =
                retrofitWithAuth.create(com.khozin.pembelajarankaidah.data.remote.ApiService.class);

        apiServiceWithAuth.finishActiveSession()
                .enqueue(new retrofit2.Callback<ApiResponse<FinishSesiResponse>>() {
                    @Override
                    public void onResponse(retrofit2.Call<ApiResponse<FinishSesiResponse>> call,
                                           retrofit2.Response<ApiResponse<FinishSesiResponse>> response) {
                        hideLoading();

                        if (response.isSuccessful() && response.body() != null) {
                            ApiResponse<FinishSesiResponse> apiResponse = response.body();
                            android.util.Log.d("QuizActivity", "Finish session API response status: " + apiResponse.getStatus());
                            if ("success".equals(apiResponse.getStatus())) {
                                FinishSesiResponse finishResponse = apiResponse.getData();
                                    SesiLatihan finishedSesi = finishResponse.getSesi();
                                android.util.Log.d("QuizActivity", "Quiz session finished successfully: " + finishedSesi.getIdSesi());
                                android.util.Log.d("QuizActivity", "Final score from API: " + finishedSesi.getSkor() + " (float)");
                                android.util.Log.d("QuizActivity", "Soal benar from API: " + finishedSesi.getSoalBenar() + "/" + finishedSesi.getTotalSoal());
                                android.util.Log.d("QuizActivity", "Rounded score: " + Math.round(finishedSesi.getSkor()) + " (Integer)");

                                // Show success message with integer score
                                // Toast completion dihilangkan

                                // Navigate to results or home with API data
                                navigateToResult(finishedSesi);
                            } else {
                                android.util.Log.e("QuizActivity", "API returned error: " + apiResponse.getMessage());
                                Toast.makeText(QuizActivity.this, "Gagal menyimpan hasil: " + apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                // Still navigate to result even if save fails
                                navigateToResult();
                            }
                        } else {
                            android.util.Log.e("QuizActivity", "Failed to finish quiz session. HTTP: " + response.code());
                            Toast.makeText(QuizActivity.this, "Gagal menyimpan hasil ke server (Kode: " + response.code() + ")", Toast.LENGTH_SHORT).show();
                            // Still navigate to result even if save fails
                            navigateToResult();
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<ApiResponse<FinishSesiResponse>> call,
                                          Throwable t) {
                        hideLoading();
                        android.util.Log.e("QuizActivity", "Network error when finishing quiz session", t);
                        Toast.makeText(QuizActivity.this, "Tidak dapat terhubung ke server untuk menyimpan hasil", Toast.LENGTH_SHORT).show();
                        // Still navigate to result even if save fails
                        navigateToResult();
                    }
                });
    }

    /**
     * Navigate to result screen
     */
    private void navigateToResult() {
        navigateToResult(currentSesi);
    }

    /**
     * Navigate to result screen with specific session data
     */
    private void navigateToResult(SesiLatihan sesiData) {
        // Get bab name for display
        String babName = "BAB " + babId; // Simple bab name, can be enhanced

        // Create and show QuizResultFragment
        QuizResultFragment resultFragment = QuizResultFragment.newInstance(sesiData, babName);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.quizContainer, resultFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Restart quiz with same bab
     */
    public void restartQuiz() {
        // Reset quiz state
        currentQuestionIndex = 0;
        sessionStarted = false;
        sessionId = 0;
        currentSesi = null;
        soalList = null;
        currentJawabanList = null;

        // Clear jawaban map
        jawabanMap.clear();

        // Reset timer
        timeLeftInMillis = 600000; // 10 menit

        // Load questions again
        loadQuizQuestions(babId);
    }

    /**
     * Update question number and progress bar
     */
    public void updateQuestionNumber(int current, int total) {
        if (tvQuestionNumber != null) {
            tvQuestionNumber.setText("Soal " + current + " dari " + total);
        }
        if (progressBar != null) {
            progressBar.setProgress((int) (current * 100.0 / total));
        }
    }

    /**
     * Show quiz questions fragment
     */
    private void showQuizQuestionsFragment() {
        android.util.Log.d("QuizActivity", "Showing QuizQuestionsFragment");
        android.util.Log.d("QuizActivity", "soalList size: " + (soalList != null ? soalList.size() : "null"));
        android.util.Log.d("QuizActivity", "jawabanMap size: " + (jawabanMap != null ? jawabanMap.size() : "null"));

        if (jawabanMap != null) {
            android.util.Log.d("QuizActivity", "Available soal IDs in jawabanMap: " + jawabanMap.keySet().toString());
            for (Map.Entry<Integer, List<Jawaban>> entry : jawabanMap.entrySet()) {
                android.util.Log.d("QuizActivity", "Soal ID " + entry.getKey() + " has " + entry.getValue().size() + " jawaban");
            }
        }

        quizQuestionsFragment = new QuizQuestionsFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.quizContainer, quizQuestionsFragment)
                .commit();

        // Set data to fragment and load first question
        quizQuestionsFragment.setQuizData(soalList, jawabanMap, viewModel);
        quizQuestionsFragment.loadQuestion();
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
        // JawabanAdapter is now managed by QuizQuestionsFragment
    }

    /**
     * Handle back button press with modern approach
     */
    private void handleOnBackPressed() {
        if (!isExiting) {
            showExitConfirmation();
        } else {
            finish();
        }
    }
}