package com.khozin.pembelajarankaidah.ui.kaidah;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.khozin.pembelajarankaidah.R;
import com.khozin.pembelajarankaidah.data.model.Bab;
import com.khozin.pembelajarankaidah.data.model.RiwayatBelajar;
import com.khozin.pembelajarankaidah.data.remote.ApiService;
import com.khozin.pembelajarankaidah.data.model.BabListResponse;
import com.khozin.pembelajarankaidah.utils.SessionManager;
import com.khozin.pembelajarankaidah.network.RetrofitClient;

import java.util.List;
import java.util.ArrayList;

/**
 * Fragment untuk menampilkan congratulations ketika bab selesai dipelajari
 */
public class BabCongratsFragment extends Fragment {

    private static final String ARG_CURRENT_BAB = "current_bab";
    private static final String ARG_NEXT_BAB = "next_bab";
    private static final String ARG_COMPLETED_MATERI = "completed_materi";
    private static final String ARG_TOTAL_MATERI = "total_materi";

    private TextView tvCongratsTitle;
    private TextView tvCongratsMessage;
    private TextView ivCongratsIcon;
    private Button btnNextBab;
    private Button btnKembali;

    private Bab currentBab;
    private Bab nextBab;

    // API dependencies untuk independent bab data loading
    private ApiService apiService;
    private SessionManager sessionManager;
    private List<Bab> cachedBabList;

    public BabCongratsFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method untuk membuat instance baru
     */
    public static BabCongratsFragment newInstance(Bab currentBab, Bab nextBab) {
        BabCongratsFragment fragment = new BabCongratsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CURRENT_BAB, currentBab);
        args.putSerializable(ARG_NEXT_BAB, nextBab);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentBab = (Bab) getArguments().getSerializable(ARG_CURRENT_BAB);
            nextBab = (Bab) getArguments().getSerializable(ARG_NEXT_BAB);
        }

        // Initialize API service and session manager for independent bab data loading
        initializeApiServices();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bab_congrats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        loadBabDataAndSetup(); // Load bab data independently before setting up UI
        setupAnimations();
        setupClickListeners();
    }

    private void initViews(View view) {
        tvCongratsTitle = view.findViewById(R.id.tv_congrats_title);
        tvCongratsMessage = view.findViewById(R.id.tv_congrats_message);
        ivCongratsIcon = view.findViewById(R.id.iv_congrats_icon);
        btnNextBab = view.findViewById(R.id.btn_next_bab);
        btnKembali = view.findViewById(R.id.btn_kembali);
    }

    /**
     * Initialize API service and session manager for independent bab data loading
     */
    private void initializeApiServices() {
        try {
            apiService = RetrofitClient.getInstance().getRetrofit().create(ApiService.class);
            sessionManager = new SessionManager(requireContext());
            android.util.Log.d("BabCongrats", "✓ API services initialized successfully");
        } catch (Exception e) {
            android.util.Log.e("BabCongrats", "✗ Failed to initialize API services: " + e.getMessage());
            apiService = null;
            sessionManager = null;
        }
    }

    /**
     * Load bab data independently from API, then setup UI
     */
    private void loadBabDataAndSetup() {
        android.util.Log.d("BabCongrats", "=== LOAD BAB DATA AND SETUP STARTED ===");

        // If currentBab is already provided, we can work with it
        if (currentBab != null) {
            android.util.Log.d("BabCongrats", "✓ currentBab already available: " + currentBab.getNamaBab());
            // Load next bab from API first, then setup UI
            loadNextBabFromApi();
            // Don't call setupData() here - let the API callback handle it
        } else {
            android.util.Log.w("BabCongrats", "⚠️ currentBab is null - cannot proceed without current bab data");
            setupData(); // Setup anyway to show error state
        }
    }

    /**
     * Load bab data from API to find next bab accurately
     */
    private void loadNextBabFromApi() {
        if (apiService == null || sessionManager == null) {
            android.util.Log.e("BabCongrats", "✗ Cannot load bab data - API services not initialized");
            return;
        }

        // Get auth token
        String authToken = sessionManager.getAuthToken();
        if (authToken == null) {
            android.util.Log.e("BabCongrats", "✗ Cannot load bab data - no auth token");
            return;
        }

        android.util.Log.d("BabCongrats", "🔄 Loading bab chapters from API...");

        apiService.getChapters("Bearer " + authToken).enqueue(new retrofit2.Callback<BabListResponse>() {
            @Override
            public void onResponse(retrofit2.Call<BabListResponse> call, retrofit2.Response<BabListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BabListResponse babResponse = response.body();
                    android.util.Log.d("BabCongrats", "📊 API Response Analysis:");
                    android.util.Log.d("BabCongrats", "   Status: " + babResponse.getStatus());
                    android.util.Log.d("BabCongrats", "   Message: " + babResponse.getMessage());
                    android.util.Log.d("BabCongrats", "   Code: " + babResponse.getCode());
                    android.util.Log.d("BabCongrats", "   isSuccess(): " + babResponse.isSuccess());
                    android.util.Log.d("BabCongrats", "   Data null: " + (babResponse.getData() == null));

                    if (babResponse.isSuccess() && babResponse.getData() != null) {
                        cachedBabList = babResponse.getData().getChapters();
                        android.util.Log.d("BabCongrats", "✓ Loaded " + (cachedBabList != null ? cachedBabList.size() : 0) + " bab chapters from API");

                        // Find next bab based on current bab's urutan
                        findAndSetNextBab();

                        // Update UI with next bab data
                        if (isAdded() && getActivity() != null) {
                            getActivity().runOnUiThread(() -> setupData());
                        }
                    } else {
                        android.util.Log.e("BabCongrats", "✗ API response validation failed:");
                        android.util.Log.e("BabCongrats", "   Status: " + babResponse.getStatus());
                        android.util.Log.e("BabCongrats", "   Message: " + babResponse.getMessage());
                        android.util.Log.e("BabCongrats", "   Code: " + babResponse.getCode());
                        android.util.Log.e("BabCongrats", "   isSuccess(): " + babResponse.isSuccess());
                        android.util.Log.e("BabCongrats", "   Data null: " + (babResponse.getData() == null));
                    }
                } else {
                    android.util.Log.e("BabCongrats", "✗ API call failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<BabListResponse> call, Throwable t) {
                android.util.Log.e("BabCongrats", "✗ API call failed: " + t.getMessage());
                // Setup with existing data even if API call fails
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> setupData());
                }
            }
        });
    }

    /**
     * Find next bab based on current bab's urutan value
     */
    private void findAndSetNextBab() {
        if (currentBab == null || cachedBabList == null || cachedBabList.isEmpty()) {
            android.util.Log.w("BabCongrats", "⚠️ Cannot find next bab - missing data");
            return;
        }

        int currentUrutan = currentBab.getUrutan();
        android.util.Log.d("BabCongrats", "🔍 Looking for next bab after urutan: " + currentUrutan);

        // Find bab with urutan = currentUrutan + 1
        for (Bab bab : cachedBabList) {
            if (bab.getUrutan() == currentUrutan + 1) {
                nextBab = bab;
                android.util.Log.d("BabCongrats", "✓ Found next bab: " + nextBab.getNamaBab() + " (urutan: " + nextBab.getUrutan() + ")");
                return;
            }
        }

        android.util.Log.d("BabCongrats", "ℹ️ No next bab found - current bab might be the last one");
        nextBab = null;
    }

    private void setupData() {
        // Comprehensive logging untuk debugging
        android.util.Log.d("BabCongrats", "=== SETUP DATA CALLED ===");
        android.util.Log.d("BabCongrats", "currentBab: " + (currentBab != null ?
            "ID=" + currentBab.getIdBab() + ", Name=" + currentBab.getNamaBab() + ", Urutan=" + currentBab.getUrutan() : "NULL"));
        android.util.Log.d("BabCongrats", "nextBab: " + (nextBab != null ?
            "ID=" + nextBab.getIdBab() + ", Name=" + nextBab.getNamaBab() + ", Urutan=" + nextBab.getUrutan() : "NULL"));

        if (currentBab != null) {
            // Judul congratulations
            if (nextBab != null) {
                android.util.Log.d("BabCongrats", "✓ Next bab found - showing next bab button");
                tvCongratsTitle.setText("Selamat! Bab Selesai");
                tvCongratsMessage.setText("Hebat! Anda telah menyelesaikan semua materi di " + currentBab.getNamaBab() + ". Siap untuk melanjutkan ke bab berikutnya?");
            } else {
                android.util.Log.d("BabCongrats", "✗ Next bab is null - hiding next bab button");
                tvCongratsTitle.setText("Selamat! Semua Bab Selesai");
                tvCongratsMessage.setText("Luar biasa! Anda telah menyelesaikan semua materi di " + currentBab.getNamaBab() + ". Anda telah menyelesaikan semua pembelajaran!");
            }

            // Setup next bab button - selalu tampilkan, tidak disembunyikan
            if (nextBab != null) {
                android.util.Log.d("BabCongrats", "✓ Setting next bab button VISIBLE with text: 'Lanjut ke " + nextBab.getNamaBab() + "'");
                btnNextBab.setVisibility(View.VISIBLE);
                btnNextBab.setText("Lanjut ke " + nextBab.getNamaBab());
            } else {
                android.util.Log.d("BabCongrats", "✓ Setting next bab button VISIBLE - Kembali ke Daftar Bab");
                btnNextBab.setVisibility(View.VISIBLE);
                btnNextBab.setText("Kembali ke Daftar Bab");
            }
        } else {
            android.util.Log.e("BabCongrats", "✗ currentBab is NULL - cannot setup data");
            // Set error state
            tvCongratsTitle.setText("Error");
            tvCongratsMessage.setText("Terjadi kesalahan saat memuat data bab.");
            btnNextBab.setVisibility(View.VISIBLE);
            btnNextBab.setText("Kembali ke Daftar Bab");
        }
        android.util.Log.d("BabCongrats", "=== SETUP DATA COMPLETED ===");
    }

    private void setupAnimations() {
        // Scale animation untuk icon congratulations
        ScaleAnimation scaleAnimation = new ScaleAnimation(
            0.0f, 1.0f, 0.0f, 1.0f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(800);
        scaleAnimation.setInterpolator(new AccelerateInterpolator());
        ivCongratsIcon.startAnimation(scaleAnimation);

        // Delayed animations untuk text elements
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Fade in untuk judul
            tvCongratsTitle.setAlpha(0f);
            tvCongratsTitle.setVisibility(View.VISIBLE);
            tvCongratsTitle.animate()
                    .alpha(1.0f)
                    .setDuration(600)
                    .setInterpolator(new AccelerateInterpolator())
                    .start();

            // Fade in untuk message
            tvCongratsMessage.setAlpha(0f);
            tvCongratsMessage.setVisibility(View.VISIBLE);
            tvCongratsMessage.animate()
                    .alpha(1.0f)
                    .setDuration(600)
                    .setInterpolator(new AccelerateInterpolator())
                    .setStartDelay(200)
                    .start();

  
            if (btnNextBab.getVisibility() == View.VISIBLE) {
                btnNextBab.setAlpha(0f);
                btnNextBab.setVisibility(View.VISIBLE);
                btnNextBab.animate()
                        .alpha(1.0f)
                        .setDuration(600)
                        .setInterpolator(new AccelerateInterpolator())
                        .setStartDelay(800)
                        .start();
            }
        }, 300);
    }

    private void setupClickListeners() {
        // Next bab button click listener
        btnNextBab.setOnClickListener(v -> {
            android.util.Log.d("BabCongrats", "=== BAB CONGRATS BUTTON CLICKED ===");
            android.util.Log.d("BabCongrats", "🎯 Next bab button clicked");
            android.util.Log.d("BabCongrats", "📱 Button text: " + btnNextBab.getText().toString());
            android.util.Log.d("BabCongrats", "📋 Current bab: " + (currentBab != null ? currentBab.getNamaBab() : "null"));
            android.util.Log.d("BabCongrats", "📋 Next bab: " + (nextBab != null ? nextBab.getNamaBab() : "null"));

            if (nextBab != null && getActivity() != null) {
                android.util.Log.d("BabCongrats", "✅ Next bab available: " + nextBab.getNamaBab() + " (ID: " + nextBab.getIdBab() + ")");
                android.util.Log.d("BabCongrats", "📋 Activity class: " + getActivity().getClass().getSimpleName());

                // Check if activity implements the required interface
                boolean isNavigationListener = getActivity() instanceof KaidahNavigationListener;
                android.util.Log.d("BabCongrats", "🔍 Activity implements KaidahNavigationListener: " + isNavigationListener);

                if (isNavigationListener) {
                    android.util.Log.d("BabCongrats", "🚀 Calling navigateToFirstMateriOfBab() with nextBab");
                    ((KaidahNavigationListener) getActivity()).navigateToFirstMateriOfBab(nextBab);
                    android.util.Log.d("BabCongrats", "✅ navigateToFirstMateriOfBab() call completed");
                } else {
                    android.util.Log.e("BabCongrats", "❌ Activity does not implement KaidahNavigationListener");
                }
            } else {
                // If nextBab is null, this button becomes "Kembali ke Daftar Bab"
                android.util.Log.d("BabCongrats", "📋 No next bab - navigating back to bab list");
                if (getActivity() instanceof KaidahNavigationListener) {
                    android.util.Log.d("BabCongrats", "🚀 Calling navigateToBabList()");
                    ((KaidahNavigationListener) getActivity()).navigateToBabList();
                } else {
                    android.util.Log.e("BabCongrats", "❌ Activity does not implement KaidahNavigationListener");
                }
            }
        });

        // Kembali button click listener - always navigates to bab list
        btnKembali.setOnClickListener(v -> {
            android.util.Log.d("BabCongrats", "=== KEMBALI BUTTON CLICKED ===");
            android.util.Log.d("BabCongrats", "🔙 Kembali button clicked - navigating to bab list");

            if (getActivity() instanceof KaidahNavigationListener) {
                android.util.Log.d("BabCongrats", "🚀 Calling navigateToBabList()");
                ((KaidahNavigationListener) getActivity()).navigateToBabList();
            } else {
                android.util.Log.e("BabCongrats", "❌ Activity does not implement KaidahNavigationListener");
            }
        });
    }

    /**
     * Interface untuk navigasi
     */
    public interface KaidahNavigationListener {
        void navigateToBabList();
        void navigateToNextBab(Bab nextBab);
        void navigateToFirstMateriOfBab(Bab bab);
    }
}