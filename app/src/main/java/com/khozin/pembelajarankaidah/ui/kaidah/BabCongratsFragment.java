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

import java.util.List;

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
        setupData();
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

    private void setupData() {
        if (currentBab != null) {
            // Judul congratulations
            if (nextBab != null) {
                tvCongratsTitle.setText("Selamat! Bab Selesai");
                tvCongratsMessage.setText("Hebat! Anda telah menyelesaikan semua materi di " + currentBab.getNamaBab() + ". Siap untuk melanjutkan ke bab berikutnya?");
            } else {
                tvCongratsTitle.setText("Selamat! Semua Bab Selesai");
                tvCongratsMessage.setText("Luar biasa! Anda telah menyelesaikan semua materi di " + currentBab.getNamaBab() + ". Anda telah menyelesaikan semua pembelajaran!");
            }

            // Setup next bab button
            if (nextBab != null) {
                btnNextBab.setVisibility(View.VISIBLE);
                btnNextBab.setText("Lanjut ke " + nextBab.getNamaBab());
            } else {
                btnNextBab.setVisibility(View.GONE);
            }
        }
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

            
            // Fade in untuk buttons
            btnKembali.setAlpha(0f);
            btnKembali.setVisibility(View.VISIBLE);
            btnKembali.animate()
                    .alpha(1.0f)
                    .setDuration(600)
                    .setInterpolator(new AccelerateInterpolator())
                    .setStartDelay(800)
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
        btnNextBab.setOnClickListener(v -> {
            android.util.Log.d("BabCongrats", "Next bab button clicked");
            if (nextBab != null && getActivity() != null) {
                android.util.Log.d("BabCongrats", "Navigating to next bab: " + nextBab.getNamaBab());
                // Navigate to next bab list - membuka daftar materi dari bab berikutnya
                if (getActivity() instanceof KaidahNavigationListener) {
                    ((KaidahNavigationListener) getActivity()).navigateToNextBab(nextBab);
                } else {
                    android.util.Log.e("BabCongrats", "Activity does not implement KaidahNavigationListener");
                }
            } else {
                android.util.Log.e("BabCongrats", "Next bab is null or activity is null");
            }
        });

        btnKembali.setOnClickListener(v -> {
            android.util.Log.d("BabCongrats", "Kembali button clicked");
            // Kembali ke daftar bab
            if (getActivity() != null) {
                android.util.Log.d("BabCongrats", "Navigating back to bab list");
                if (getActivity() instanceof KaidahNavigationListener) {
                    ((KaidahNavigationListener) getActivity()).navigateToBabList();
                } else {
                    android.util.Log.e("BabCongrats", "Activity does not implement KaidahNavigationListener");
                }
            } else {
                android.util.Log.e("BabCongrats", "Activity is null");
            }
        });
    }

    
    /**
     * Interface untuk navigasi
     */
    public interface KaidahNavigationListener {
        void navigateToBabList();
        void navigateToNextBab(Bab nextBab);
    }
}