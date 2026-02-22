package com.khozin.pembelajarankaidah.ui.kaidah;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.khozin.pembelajarankaidah.R;
import com.khozin.pembelajarankaidah.data.model.MateriKaidah;
import com.khozin.pembelajarankaidah.data.model.Bab;
import com.khozin.pembelajarankaidah.data.model.RiwayatBelajar;
import com.khozin.pembelajarankaidah.data.model.KaidahListResponse;
import com.khozin.pembelajarankaidah.utils.SessionManager;
import com.khozin.pembelajarankaidah.data.remote.ApiService;
import com.khozin.pembelajarankaidah.network.RetrofitClient;
import com.khozin.pembelajarankaidah.utils.BabProgressHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment untuk menampilkan detail materi kaidah
 * Menampilkan penjelasan, contoh, dan tombol untuk memulai latihan
 */
public class KaidahDetailFragment extends Fragment implements BabCongratsFragment.KaidahNavigationListener {

    // UI Components
    private TextView tvJudulKaidah;
    private TextView tvDeskripsi;
    private TextView tvPenjelasan;
    private TextView tvContoh;
    private MaterialCardView cardPenjelasan;
    private MaterialCardView cardContoh;
    private ImageView btnBack;
    private Button btnPreviousMateri;
    private Button btnNextMateri;
    private TextView tvCurrentMateriInfo;

    // Data
    private SessionManager sessionManager;
    private ApiService apiService;
    private BabProgressHelper babProgressHelper;
    private MateriKaidah currentKaidah;
    private List<MateriKaidah> allKaidahList;
    private List<Bab> allBabList; // For bab navigation
    private int currentKaidahIndex = 0;
    private int currentKaidahId = 0;

    // Arguments
    private static final String ARG_KAIDAH_ID = "kaidah_id";

    public static KaidahDetailFragment newInstance(int kaidahId) {
        KaidahDetailFragment fragment = new KaidahDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_KAIDAH_ID, kaidahId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int kaidahId = getArguments().getInt(ARG_KAIDAH_ID, 0);
            // Store kaidah ID to load later after database is ready
            currentKaidahId = kaidahId;

                    } else {
            android.util.Log.e("KaidahDetail", "getArguments() is null!");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kaidah_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupDatabase();
        setupListeners();

        // Load kaidah data based on ID from arguments
        loadKaidahData();
    }

    /**
     * Initialize views
     */
    private void initViews(View view) {
        tvJudulKaidah = view.findViewById(R.id.tvJudulKaidah);
        tvDeskripsi = view.findViewById(R.id.tvDeskripsi);
        tvPenjelasan = view.findViewById(R.id.tvPenjelasan);
        tvContoh = view.findViewById(R.id.tvContoh);
        cardPenjelasan = view.findViewById(R.id.cardPenjelasan);
        cardContoh = view.findViewById(R.id.cardContoh);
        btnBack = view.findViewById(R.id.btnBack);
        btnPreviousMateri = view.findViewById(R.id.btnPreviousMateri);
        btnNextMateri = view.findViewById(R.id.btnNextMateri);
        tvCurrentMateriInfo = view.findViewById(R.id.tvCurrentMateriInfo);
    }

    /**
     * Setup database
     */
    private void setupDatabase() {
        sessionManager = new SessionManager(requireContext());
        apiService = RetrofitClient.getInstance().getRetrofit().create(ApiService.class);
        babProgressHelper = new BabProgressHelper(requireContext());
    }

    /**
     * Setup listeners
     */
    private void setupListeners() {
        btnBack.setOnClickListener(v -> {
            // Go back to kaidah list
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        btnPreviousMateri.setOnClickListener(v -> {
            navigateToPreviousMateri();
        });

        // Toggle penjelasan card
        cardPenjelasan.setOnClickListener(v -> {
            // Toggle penjelasan visibility if needed
        });

        // Toggle contoh card
        cardContoh.setOnClickListener(v -> {
            // Toggle contoh visibility if needed
        });
    }

    /**
     * Load kaidah data from API (always fetch fresh data)
     */
    private void loadKaidahData() {
        android.util.Log.d("KaidahDetail", String.format(
            "Loading kaidah data from API with ID: %d", currentKaidahId
        ));

        if (currentKaidahId == 0) {
            android.util.Log.e("KaidahDetail", "Invalid kaidah ID: 0");
            Toast.makeText(getContext(), "ID Kaidah tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        // Always fetch from API to get fresh data (no local database caching)
        android.util.Log.d("KaidahDetail", "Fetching fresh data from API (no local database)");

        // First, synchronize bab data to ensure getNextBab() works correctly
        synchronizeBabData();

        // Then load kaidah data
        loadAllKaidahFromAPI(currentKaidahId);
    }

    /**
     * Load all kaidah for navigation (now uses API instead of database)
     */
    private void loadAllKaidah() {
        android.util.Log.d("KaidahDetail", "Loading all kaidah from API for navigation");
        loadAllKaidahFromAPI(currentKaidahId);
    }

    /**
     * Bind data to views
     */
    private void bindData(MateriKaidah kaidah) {
        currentKaidah = kaidah;

        
        // Set judul
        tvJudulKaidah.setText(kaidah.getJudulMateri());

        // Set deskripsi
        String deskripsi = kaidah.getDeskripsi();
        if (deskripsi != null && !deskripsi.trim().isEmpty()) {
            tvDeskripsi.setText(deskripsi);
            tvDeskripsi.setVisibility(View.VISIBLE);
        } else {
            tvDeskripsi.setVisibility(View.GONE);
        }

        // Set penjelasan
        String penjelasan = kaidah.getPenjelasan();
        if (penjelasan != null && !penjelasan.trim().isEmpty()) {
            tvPenjelasan.setText(penjelasan);
            cardPenjelasan.setVisibility(View.VISIBLE);
        } else {
            cardPenjelasan.setVisibility(View.GONE);
        }

        // Set contoh
        String contoh = kaidah.getContoh();
        if (contoh != null && !contoh.trim().isEmpty()) {
            tvContoh.setText(contoh);
            cardContoh.setVisibility(View.VISIBLE);
        } else {
            cardContoh.setVisibility(View.GONE);
        }

        
        // Update navigation info
        updateNavigationInfo();
    }

    
    /**
     * Update navigation info
     */
    private void updateNavigationInfo() {
        android.util.Log.d("KaidahDetail", "=== updateNavigationInfo() START ===");

        if (allKaidahList != null && allKaidahList.size() > 0 && currentKaidah != null) {
            android.util.Log.d("KaidahDetail", "allKaidahList size: " + allKaidahList.size());
            android.util.Log.d("KaidahDetail", "currentKaidah ID: " + currentKaidah.getIdMateri());
            android.util.Log.d("KaidahDetail", "currentKaidah Bab ID: " + currentKaidah.getIdBab());
            android.util.Log.d("KaidahDetail", "currentKaidah Judul: " + currentKaidah.getJudulMateri());
            android.util.Log.d("KaidahDetail", "currentKaidahIndex: " + currentKaidahIndex);

            // Log all items in allKaidahList for debugging
            android.util.Log.d("KaidahDetail", "=== allKaidahList contents ===");
            for (int i = 0; i < allKaidahList.size(); i++) {
                MateriKaidah materi = allKaidahList.get(i);
                android.util.Log.d("KaidahDetail", "allKaidahList[" + i + "]: ID=" + materi.getIdMateri() +
                        ", Bab=" + materi.getIdBab() + ", Urutan=" + materi.getUrutan() +
                        ", Judul=" + materi.getJudulMateri());
            }

            // Get all materi in current bab
            List<MateriKaidah> materiInCurrentBab = new ArrayList<>();
            int currentMateriPositionInBab = 0;

            android.util.Log.d("KaidahDetail", "=== Filtering materi for Bab " + currentKaidah.getIdBab() + " ===");
            for (int i = 0; i < allKaidahList.size(); i++) {
                MateriKaidah materi = allKaidahList.get(i);
                android.util.Log.d("KaidahDetail", "Checking materi ID=" + materi.getIdMateri() +
                        ", Bab=" + materi.getIdBab() + " against current Bab=" + currentKaidah.getIdBab());

                if (materi.getIdBab() == currentKaidah.getIdBab()) {
                    materiInCurrentBab.add(materi);
                    android.util.Log.d("KaidahDetail", "✓ Added to materiInCurrentBab - New size: " + materiInCurrentBab.size());

                    // Find position of current materi in this bab
                    if (materi.getIdMateri() == currentKaidah.getIdMateri()) {
                        currentMateriPositionInBab = materiInCurrentBab.size();
                        android.util.Log.d("KaidahDetail", "✓ Found current materi! Position in bab: " + currentMateriPositionInBab);
                    }
                }
            }

            android.util.Log.d("KaidahDetail", "materiInCurrentBab final size: " + materiInCurrentBab.size());
            android.util.Log.d("KaidahDetail", "currentMateriPositionInBab: " + currentMateriPositionInBab);

            // Log materi in current bab
            android.util.Log.d("KaidahDetail", "=== materiInCurrentBab contents ===");
            for (int i = 0; i < materiInCurrentBab.size(); i++) {
                MateriKaidah materi = materiInCurrentBab.get(i);
                android.util.Log.d("KaidahDetail", "materiInCurrentBab[" + i + "]: ID=" + materi.getIdMateri() +
                        ", Bab=" + materi.getIdBab() + ", Urutan=" + materi.getUrutan() +
                        ", Judul=" + materi.getJudulMateri());
            }

            // Update current materi info based on bab
            if (!materiInCurrentBab.isEmpty()) {
                int currentPosition = currentMateriPositionInBab;
                int totalMateriInBab = materiInCurrentBab.size();
                String displayText = "Materi " + currentPosition + " dari " + totalMateriInBab + " (Bab " + currentKaidah.getIdBab() + ")";
                tvCurrentMateriInfo.setText(displayText);
                android.util.Log.d("KaidahDetail", "✓ Display text set to: " + displayText);
            } else {
                // Fallback to original logic if no materi found in bab
                android.util.Log.w("KaidahDetail", "⚠ materiInCurrentBab is empty! Using fallback logic");
                int currentPosition = currentKaidahIndex + 1;
                int totalMateri = allKaidahList.size();
                String fallbackText = "Materi " + currentPosition + " dari " + totalMateri;
                tvCurrentMateriInfo.setText(fallbackText);
                android.util.Log.d("KaidahDetail", "✓ Fallback display text set to: " + fallbackText);
            }

            // Update previous button state
            MateriKaidah previousMateri = getPreviousMateriInSameBab();
            android.util.Log.d("KaidahDetail", "Getting previous materi in same bab...");
            if (previousMateri == null) {
                // First materi in bab, disable previous button
                android.util.Log.d("KaidahDetail", "✓ No previous materi found - disabling previous button");
                btnPreviousMateri.setEnabled(false);
                btnPreviousMateri.setAlpha(0.5f);
                btnPreviousMateri.setOnClickListener(null);
            } else {
                // Not first materi, enable previous button
                android.util.Log.d("KaidahDetail", "✓ Previous materi found: ID=" + previousMateri.getIdMateri() + ", enabling previous button");
                btnPreviousMateri.setEnabled(true);
                btnPreviousMateri.setAlpha(1.0f);
                btnPreviousMateri.setOnClickListener(v -> {
                    navigateToPreviousMateri();
                });
            }

            // Update next button state
            btnNextMateri.setOnClickListener(v -> {
                // Always call navigateToNextMateri - it will handle the logic internally
                android.util.Log.d("KaidahDetail", "Next button clicked - calling navigateToNextMateri()");
                navigateToNextMateri();
            });

            // Check if this is the last materi in the current bab
            MateriKaidah nextMateriInBab = getNextMateriInSameBab();
            if (nextMateriInBab == null) {
                // Last materi in current bab, change text to "Selesai"
                android.util.Log.d("KaidahDetail", "✓ Last materi in current bab - setting next button text to 'Selesai'");
                btnNextMateri.setText("Selesai");
            } else {
                // Not last materi in bab, set text to "Materi Selanjutnya"
                android.util.Log.d("KaidahDetail", "✓ Not last materi in current bab - setting next button text to 'Materi Selanjutnya'");
                btnNextMateri.setText("Materi Selanjutnya");
            }
        } else {
            android.util.Log.w("KaidahDetail", "⚠ Cannot update navigation info - missing required data");
            android.util.Log.w("KaidahDetail", "allKaidahList: " + (allKaidahList == null ? "null" : allKaidahList.size() + " items"));
            android.util.Log.w("KaidahDetail", "currentKaidah: " + (currentKaidah == null ? "null" : "OK"));
        }

        android.util.Log.d("KaidahDetail", "=== updateNavigationInfo() END ===");
    }

    /**
     * Navigate to next materi
     */
    private void navigateToNextMateri() {
        // Get next materi in the same bab
        MateriKaidah nextKaidah = getNextMateriInSameBab();

        if (nextKaidah != null) {
            // Mark current materi as completed and navigate to next
            markCurrentMateriAsCompleted();
            checkBabCompletionAndNavigate(nextKaidah);
        } else {
            // Last materi - mark as completed and show congratulations
            markCurrentMateriAsCompleted();

            // Show congratulations fragment for last materi
            if (isAdded() && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    // Find next bab that has materi
                    List<MateriKaidah> nextBabMateri = new ArrayList<>();
                    for (MateriKaidah materi : allKaidahList) {
                        if (materi.getIdBab() > currentKaidah.getIdBab()) {
                            nextBabMateri.add(materi);
                        }
                    }

                    MateriKaidah nextBab = !nextBabMateri.isEmpty() ? nextBabMateri.get(0) : null;

                    // Get materi count for current bab
                    List<MateriKaidah> materiInBab = new ArrayList<>();
                    for (MateriKaidah materi : allKaidahList) {
                        if (materi.getIdBab() == currentKaidah.getIdBab()) {
                            materiInBab.add(materi);
                        }
                    }

                    showCongratulationsFragment(currentKaidah, nextBab, materiInBab.size(), materiInBab.size());
                });
            }
        }
    }

    /**
     * Navigate to previous materi
     */
    private void navigateToPreviousMateri() {
        MateriKaidah previousKaidah = getPreviousMateriInSameBab();
        if (previousKaidah != null) {
            // Update current index and kaidah
            for (int i = 0; i < allKaidahList.size(); i++) {
                if (allKaidahList.get(i).getIdMateri() == previousKaidah.getIdMateri()) {
                    currentKaidahIndex = i;
                    currentKaidah = previousKaidah;
                    break;
                }
            }

            // Load previous kaidah data
            bindData(previousKaidah);

            // Scroll to top
            if (getView() != null) {
                getView().post(() -> {
                    ((androidx.core.widget.NestedScrollView) getView().findViewById(R.id.scrollView))
                            .smoothScrollTo(0, 0);
                });
            }
        }
    }

    /**
     * Get previous materi in the same bab
     */
    private MateriKaidah getPreviousMateriInSameBab() {
        if (currentKaidah == null || allKaidahList == null) {
            return null;
        }

        // Find current position in bab
        List<MateriKaidah> materiInBab = getMateriInBab(currentKaidah.getIdBab());
        int currentPosition = -1;

        for (int i = 0; i < materiInBab.size(); i++) {
            if (materiInBab.get(i).getIdMateri() == currentKaidah.getIdMateri()) {
                currentPosition = i;
                break;
            }
        }

        // Return previous materi if exists
        if (currentPosition > 0) {
            return materiInBab.get(currentPosition - 1);
        }

        return null;
    }

    /**
     * Get next materi in the same bab
     */
    private MateriKaidah getNextMateriInSameBab() {
        if (currentKaidah == null || allKaidahList == null) {
            return null;
        }

        // Find current position in bab
        List<MateriKaidah> materiInBab = getMateriInBab(currentKaidah.getIdBab());
        int currentPosition = -1;

        for (int i = 0; i < materiInBab.size(); i++) {
            if (materiInBab.get(i).getIdMateri() == currentKaidah.getIdMateri()) {
                currentPosition = i;
                break;
            }
        }

        // Return next materi if exists (this is the last materi if null)
        if (currentPosition >= 0 && currentPosition < materiInBab.size() - 1) {
            return materiInBab.get(currentPosition + 1);
        }

        return null;
    }

    /**
     * Get all materi in a specific bab
     */
    private List<MateriKaidah> getMateriInBab(int babId) {
        List<MateriKaidah> materiInBab = new ArrayList<>();
        if (allKaidahList != null) {
            for (MateriKaidah materi : allKaidahList) {
                if (materi.getIdBab() == babId) {
                    materiInBab.add(materi);
                }
            }
        }
        return materiInBab;
    }

    /**
     * Check bab completion and handle navigation appropriately
     */
    private void checkBabCompletionAndNavigate(MateriKaidah nextMateri) {
        android.util.Log.d("KaidahDetail", "=== checkBabCompletionAndNavigate() START ===");
        android.util.Log.d("KaidahDetail", "Current materi: " + currentKaidah.getJudulMateri());
        android.util.Log.d("KaidahDetail", "Next materi: " + (nextMateri != null ? nextMateri.getJudulMateri() : "None (Last materi)"));

        if (currentKaidah == null || allKaidahList == null) {
            android.util.Log.e("KaidahDetail", "Current kaidah or allKaidahList is null");
            // If this is the last materi, congratulations will be shown by babcongrats
            if (nextMateri == null) {
                android.util.Log.d("KaidahDetail", "Last materi completed - babcongrats will handle the notification");
            }
            return;
        }

        // Get all materi in current bab
        List<MateriKaidah> materiInBab = new ArrayList<>();
        for (MateriKaidah materi : allKaidahList) {
            if (materi.getIdBab() == currentKaidah.getIdBab()) {
                materiInBab.add(materi);
            }
        }

        if (materiInBab.isEmpty()) {
            android.util.Log.e("KaidahDetail", "No materi found in current bab");
            // If this is the last materi, congratulations will be shown by babcongrats
            if (nextMateri == null) {
                android.util.Log.d("KaidahDetail", "Last materi completed - babcongrats will handle the notification");
            }
            return;
        }

        android.util.Log.d("KaidahDetail", "Total materi in bab: " + materiInBab.size());

        // Check completion using BabProgressHelper
        babProgressHelper.checkBabCompletion(
            currentKaidah.getIdBab(),
            materiInBab,
            new BabProgressHelper.BabCompletionCallback() {
                @Override
                public void onProgressChecked(int babId, int totalMateri, int completedMateri,
                                              float completionPercentage, boolean isCompleted) {
                    android.util.Log.d("KaidahDetail", "Bab completion check result - Total: " + totalMateri +
                        ", Completed: " + completedMateri + ", IsCompleted: " + isCompleted);

                    if (isCompleted) {
                        android.util.Log.d("KaidahDetail", "Bab COMPLETED! Showing congratulations fragment");

                        // Find next bab that has materi
                        List<MateriKaidah> nextBabMateri = new ArrayList<>();
                        for (MateriKaidah materi : allKaidahList) {
                            if (materi.getIdBab() > currentKaidah.getIdBab()) {
                                nextBabMateri.add(materi);
                            }
                        }

                        // Show congratulations fragment for completed bab
                        MateriKaidah nextBab = !nextBabMateri.isEmpty() ? nextBabMateri.get(0) : null;
                        android.util.Log.d("KaidahDetail", "Showing congratulations fragment. Next bab: " +
                            (nextBab != null ? nextBab.getJudulMateri() : "None"));

                        if (isAdded() && getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                showCongratulationsFragment(currentKaidah, nextBab, completedMateri, totalMateri);
                            });
                        }
                    } else {
                        if (nextMateri != null) {
                            android.util.Log.d("KaidahDetail", "Bab not completed yet, navigating to next materi");
                            // Bab not completed and there's a next materi, continue with normal navigation
                            continueWithNextMateriNavigation(nextMateri);
                        } else {
                            android.util.Log.d("KaidahDetail", "Bab not completed and no next materi - no simple toast needed");
                            // This is the last materi in bab but bab is not completed
                            // No toast needed - babcongrats will handle completion when appropriate
                        }
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    android.util.Log.e("KaidahDetail", "Error checking bab completion: " + errorMessage);
                    if (nextMateri != null) {
                        // On error, continue with normal navigation to next materi
                        continueWithNextMateriNavigation(nextMateri);
                    } else {
                        // This is the last materi - no toast needed as babcongrats will handle it
                        android.util.Log.d("KaidahDetail", "Last materi error case - no toast needed");
                    }
                }
            }
        );
    }

    // Removed showSimpleCompletionToast() method as babcongrats already handles completion notification

    /**
     * Continue with normal navigation to next materi
     */
    private void continueWithNextMateriNavigation(MateriKaidah nextMateri) {
        android.util.Log.d("KaidahDetail", "Continuing with normal navigation to next materi");

        // Create final copies for lambda access
        final int nextMateriId = nextMateri.getIdMateri();
        final MateriKaidah[] updatedNextKaidah = {nextMateri};

        // Fetch next materi directly from API
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                android.util.Log.d("KaidahDetail", "Fetching next materi directly from API: " + nextMateriId);

                if (fetchMateriFromApiAndSave(nextMateriId)) {
                    android.util.Log.d("KaidahDetail", "Successfully fetched next materi from API");

                    // Update current index and kaidah on main thread
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            for (int i = 0; i < allKaidahList.size(); i++) {
                                if (allKaidahList.get(i).getIdMateri() == nextMateriId) {
                                    currentKaidahIndex = i;
                                    currentKaidah = nextMateri;
                                    break;
                                }
                            }

                            // Load next kaidah data
                            bindData(nextMateri);

                            // Scroll to top
                            if (getView() != null) {
                                getView().post(() -> {
                                    ((androidx.core.widget.NestedScrollView) getView().findViewById(R.id.scrollView))
                                            .smoothScrollTo(0, 0);
                                });
                            }
                        });
                    }
                } else {
                    android.util.Log.e("KaidahDetail", "Failed to fetch next materi from API");
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Gagal memuat materi selanjutnya", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            } catch (Exception e) {
                android.util.Log.e("KaidahDetail", "Error navigating to next materi", e);
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Terjadi kesalahan saat memuat materi selanjutnya", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    /**
     * Check if current bab is completed and show congratulations screen
     */
    private void checkBabCompletionAndShowCongrats() {
        android.util.Log.d("KaidahDetail", "=== checkBabCompletionAndShowCongrats() START ===");

        if (currentKaidah == null || allKaidahList == null) {
            android.util.Log.e("KaidahDetail", "Current kaidah or allKaidahList is null");
            return;
        }

        android.util.Log.d("KaidahDetail", "Current materi: " + currentKaidah.getJudulMateri() + ", Bab ID: " + currentKaidah.getIdBab());

        // Get all materi in current bab
        List<MateriKaidah> materiInBab = new ArrayList<>();
        for (MateriKaidah materi : allKaidahList) {
            if (materi.getIdBab() == currentKaidah.getIdBab()) {
                materiInBab.add(materi);
            }
        }

        if (materiInBab.isEmpty()) {
            return;
        }

        // Check completion using BabProgressHelper
        babProgressHelper.checkBabCompletion(
            currentKaidah.getIdBab(),
            materiInBab,
            new BabProgressHelper.BabCompletionCallback() {
                @Override
                public void onProgressChecked(int babId, int totalMateri, int completedMateri,
                                              float completionPercentage, boolean isCompleted) {
                    if (isCompleted) {
                        android.util.Log.d("KaidahDetail", "Bab COMPLETED! Total materi: " + totalMateri + ", Completed: " + completedMateri);

                        // Find next bab that has materi
                        List<MateriKaidah> nextBabMateri = new ArrayList<>();
                        for (MateriKaidah materi : allKaidahList) {
                            if (materi.getIdBab() > currentKaidah.getIdBab()) {
                                nextBabMateri.add(materi);
                                }
                        }

                        // Always show congratulations fragment when bab is completed
                        MateriKaidah nextBab = !nextBabMateri.isEmpty() ? nextBabMateri.get(0) : null;
                        android.util.Log.d("KaidahDetail", "Showing congratulations fragment for completed bab. Next bab: " + (nextBab != null ? nextBab.getJudulMateri() : "None"));
                        showCongratulationsFragment(currentKaidah, nextBab, completedMateri, totalMateri);
                    } else {
                        // Bab not completed yet, show simple toast
                        if (isAdded() && getContext() != null) {
                            Toast.makeText(getContext(), "Selamat! Anda telah menyelesaikan materi ini.",
                                        Toast.LENGTH_SHORT).show();
                        }

                        // Go back to list
                        if (getActivity() != null) {
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    android.util.Log.e("KaidahDetail", "Error checking bab completion: " + errorMessage);
                    // Fallback to simple toast on main thread
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getActivity(), "Selamat! Anda telah menyelesaikan materi ini.",
                                    Toast.LENGTH_SHORT).show();
                        });
                    }

                    // Go back to list
                    if (getFragmentManager() != null) {
                        getFragmentManager().popBackStack();
                    }
                }
            }
        );
    }

    /**
     * Show congratulations fragment
     */
    private void showCongratulationsFragment(MateriKaidah currentMateri, MateriKaidah nextMateri,
                                             int completedMateri, int totalMateri) {
        try {
            android.util.Log.d("KaidahDetail", "=== showCongratulationsFragment() START ===");
            android.util.Log.d("KaidahDetail", "Current materi: " + currentMateri.getJudulMateri() +
                              " (Bab ID: " + currentMateri.getIdBab() + ")");

            // Convert MateriKaidah to Bab objects
            Bab currentBab = getBabFromMateri(currentMateri);
            android.util.Log.d("KaidahDetail", "Current bab converted: " +
                              (currentBab != null ? currentBab.getNamaBab() + " (ID: " + currentBab.getIdBab() + ", Urutan: " + currentBab.getUrutan() + ")" : "null"));

            // Note: Next bab finding is now handled by BabCongratsFragment independently
            // Note: BabCongratsFragment now handles finding the next bab independently
            // Bab nextBab = getNextBab(currentBab); // No longer needed

            android.util.Log.d("KaidahDetail", "Next bab handling: BabCongratsFragment will find next bab independently");

            android.util.Log.d("KaidahDetail", "Creating BabCongratsFragment with currentBab: " +
                              (currentBab != null ? currentBab.getNamaBab() : "null") +
                              ", nextBab: null (BabCongrats will find next bab independently)");

            BabCongratsFragment congratsFragment = BabCongratsFragment.newInstance(currentBab, null);

            if (getActivity() != null) {
                getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, congratsFragment)
                    .addToBackStack(null)
                    .commit();
                android.util.Log.d("KaidahDetail", "BabCongratsFragment committed successfully");
            } else {
                android.util.Log.e("KaidahDetail", "Activity is null, cannot show fragment");
            }
        } catch (Exception e) {
            android.util.Log.e("KaidahDetail", "Error showing congratulations fragment: " + e.getMessage(), e);
            // Fallback to simple toast
            if (isAdded() && getContext() != null) {
                Toast.makeText(getContext(), "Selamat! Bab telah selesai.",
                        Toast.LENGTH_LONG).show();
            }

            // Go back to list
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }
    }

    /**
     * Convert MateriKaidah to Bab by finding the Bab object in API data (no local database)
     */
    private Bab getBabFromMateri(MateriKaidah materi) {
        if (materi == null) {
            android.util.Log.e("KaidahDetail", "Materi is null in getBabFromMateri()");
            return null;
        }

        try {
            android.util.Log.d("KaidahDetail", "🔍 Looking for Bab with ID: " + materi.getIdBab() +
                              " from materi: " + materi.getJudulMateri());

            // If allBabList is not available, create a temporary Bab from materi data
            if (allBabList == null || allBabList.isEmpty()) {
                android.util.Log.w("KaidahDetail", "⚠️ allBabList is null or empty, creating temporary Bab from materi");
                Bab tempBab = new Bab();
                tempBab.setIdBab(materi.getIdBab());
                tempBab.setNamaBab("Bab " + materi.getIdBab()); // Use bab ID as fallback name
                tempBab.setUrutan(materi.getIdBab()); // Use bab ID as fallback urutan
                android.util.Log.d("KaidahDetail", "✅ Created temporary Bab: " + tempBab.getNamaBab() +
                                  " (ID: " + tempBab.getIdBab() + ", Urutan: " + tempBab.getUrutan() + ")");
                return tempBab;
            }

            // Use the cached API data
            for (Bab bab : allBabList) {
                if (bab.getIdBab() == materi.getIdBab()) {
                    android.util.Log.d("KaidahDetail", "✅ Found Bab: " + bab.getNamaBab() +
                                      " (ID: " + bab.getIdBab() + ", Urutan: " + bab.getUrutan() + ")");
                    return bab;
                }
            }

            // If not found in API data, create temporary Bab
            android.util.Log.w("KaidahDetail", "⚠️ Bab not found in API data, creating temporary Bab");
            Bab tempBab = new Bab();
            tempBab.setIdBab(materi.getIdBab());
            tempBab.setNamaBab("Bab " + materi.getIdBab());
            tempBab.setUrutan(materi.getIdBab());
            android.util.Log.d("KaidahDetail", "✅ Created fallback Bab: " + tempBab.getNamaBab() +
                              " (ID: " + tempBab.getIdBab() + ", Urutan: " + tempBab.getUrutan() + ")");
            return tempBab;
        } catch (Exception e) {
            android.util.Log.e("KaidahDetail", "Error getting Bab from Materi: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Find the next bab based on the current bab using API data (no local database)
     */
    private Bab getNextBab(Bab currentBab) {
        if (currentBab == null) {
            android.util.Log.d("KaidahDetail", "Current bab is null, cannot find next bab");
            return null;
        }

        try {
            android.util.Log.d("KaidahDetail", "Finding next bab for current bab: " + currentBab.getNamaBab() +
                              " (ID: " + currentBab.getIdBab() + ", Urutan: " + currentBab.getUrutan() + ")");

            // Use the cached API data instead of local database
            if (allBabList != null && !allBabList.isEmpty()) {
                android.util.Log.d("KaidahDetail", "allBabList has " + allBabList.size() + " items");

                // Log all available bab data for debugging
                for (int idx = 0; idx < allBabList.size(); idx++) {
                    Bab bab = allBabList.get(idx);
                    android.util.Log.d("KaidahDetail", "  Bab[" + idx + "]: " + bab.getNamaBab() +
                                      " (ID: " + bab.getIdBab() + ", Urutan: " + bab.getUrutan() + ")");
                }

                // Find the current bab in the API data
                for (int i = 0; i < allBabList.size(); i++) {
                    Bab bab = allBabList.get(i);
                    if (bab.getIdBab() == currentBab.getIdBab()) {
                        android.util.Log.d("KaidahDetail", "Found current bab at index " + i + ": " + bab.getNamaBab());

                        // Found current bab, look for next bab with higher urutan
                        for (int j = i + 1; j < allBabList.size(); j++) {
                            Bab nextBabCandidate = allBabList.get(j);
                            android.util.Log.d("KaidahDetail", "Checking candidate at index " + j + ": " + nextBabCandidate.getNamaBab() +
                                              " (Urutan: " + nextBabCandidate.getUrutan() + " vs Current: " + currentBab.getUrutan() + ")");

                            if (nextBabCandidate.getUrutan() > currentBab.getUrutan()) {
                                android.util.Log.d("KaidahDetail", "✓ Found next bab: " + nextBabCandidate.getNamaBab() +
                                          " (ID: " + nextBabCandidate.getIdBab() + ", Urutan: " + nextBabCandidate.getUrutan() + ")");
                                return nextBabCandidate;
                            }
                        }
                        android.util.Log.d("KaidahDetail", "No higher urutan bab found after current bab");
                        break; // Current bab not found, exit loop
                    }
                }
                android.util.Log.e("KaidahDetail", "Current bab ID " + currentBab.getIdBab() + " not found in allBabList");
            } else {
                android.util.Log.e("KaidahDetail", "allBabList is null or empty");
                android.util.Log.e("KaidahDetail", "allBabList = " + (allBabList == null ? "null" : "empty with size " + allBabList.size()));
            }

            android.util.Log.d("KaidahDetail", "No next bab found - this is the last bab");
            return null;
        } catch (Exception e) {
            android.util.Log.e("KaidahDetail", "Error finding next bab: " + e.getMessage(), e);
            return null;
        }
    }

    // KaidahNavigationListener interface implementation

    @Override
    public void navigateToBabList() {
        // Clear back stack and go to main list
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                .popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override
    public void navigateToNextBab(Bab nextBab) {
        if (nextBab != null && getActivity() != null) {
            android.util.Log.d("KaidahDetail", "Navigating to next bab: " + nextBab.getNamaBab());

            // Find the first materi of the next bab
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    // Get all materi from the next bab
                    List<MateriKaidah> nextBabMateriList = new ArrayList<>();
                    for (MateriKaidah materi : allKaidahList) {
                        if (materi.getIdBab() == nextBab.getIdBab()) {
                            nextBabMateriList.add(materi);
                        }
                    }

                    if (!nextBabMateriList.isEmpty()) {
                        // Get the first materi from the next bab
                        MateriKaidah firstMateriNextBab = nextBabMateriList.get(0);
                        android.util.Log.d("KaidahDetail", "Found first materi of next bab: " + firstMateriNextBab.getJudulMateri());

                        // Navigate to the first materi of the next bab
                        if (isAdded() && getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                // Replace current fragment with new KaidahDetailFragment for the first materi of next bab
                                KaidahDetailFragment newFragment = KaidahDetailFragment.newInstance(firstMateriNextBab.getIdMateri());

                                getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(android.R.id.content, newFragment)
                                    .addToBackStack(null)
                                    .commit();
                            });
                        }
                    } else {
                        android.util.Log.e("KaidahDetail", "No materi found in next bab: " + nextBab.getNamaBab());
                        // Fallback to bab list
                        if (isAdded() && getActivity() != null) {
                            getActivity().runOnUiThread(this::navigateToBabList);
                        }
                    }
                } catch (Exception e) {
                    android.util.Log.e("KaidahDetail", "Error navigating to next bab", e);
                    // Fallback to bab list
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(this::navigateToBabList);
                    }
                }
            });
        } else {
            android.util.Log.e("KaidahDetail", "Cannot navigate - nextBab is null or activity is null");
        }
    }

    /**
     * Fetch specific materi from API and save to database
     * @param materiId The ID of materi to fetch
     * @return true if successful, false otherwise
     */
    private boolean fetchMateriFromApiAndSave(int materiId) {
        android.util.Log.d("KaidahDetail", "=== FETCH MATERI FROM API AND SAVE START ===");
        android.util.Log.d("KaidahDetail", "Fetching materi ID: " + materiId);

        String sessionToken = sessionManager.getAuthToken();
        if (sessionToken == null || sessionToken.isEmpty()) {
            android.util.Log.e("KaidahDetail", "No session token for API fetch");
            return false;
        }

        try {
            // Create a semaphore to wait for async call
            java.util.concurrent.Semaphore semaphore = new java.util.concurrent.Semaphore(0);
            final boolean[] success = {false};

            // Load all kaidah from API and find the specific one
            apiService.getKaidahListWithWrapper().enqueue(new Callback<KaidahListResponse>() {
                @Override
                public void onResponse(Call<KaidahListResponse> call, Response<KaidahListResponse> response) {
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            KaidahListResponse kaidahResponse = response.body();
                            if (kaidahResponse.isSuccess() && kaidahResponse.getKaidahList() != null) {
                                List<MateriKaidah> kaidahList = kaidahResponse.getKaidahList();

                                // Find the specific kaidah by ID
                                MateriKaidah foundKaidah = null;
                                for (MateriKaidah kaidah : kaidahList) {
                                    if (kaidah.getIdMateri() == materiId) {
                                        foundKaidah = kaidah;
                                        break;
                                    }
                                }

                                if (foundKaidah != null) {
                                    android.util.Log.d("KaidahDetail", "Found materi in API: " + foundKaidah.getJudulMateri());

                                    // Create final copy for lambda
                                    final MateriKaidah finalFoundKaidah = foundKaidah;

                                    // Skip database save, use API directly
                                    android.util.Log.d("KaidahDetail", "Using API data directly, skipping database save");
                                    success[0] = true;
                                    semaphore.release();
                                } else {
                                    android.util.Log.e("KaidahDetail", "Materi ID " + materiId + " not found in API response");
                                    success[0] = false;
                                    semaphore.release();
                                }
                            } else {
                                android.util.Log.e("KaidahDetail", "API response not successful: " + kaidahResponse.getMessage());
                                success[0] = false;
                                semaphore.release();
                            }
                        } else {
                            android.util.Log.e("KaidahDetail", "API call failed: " + response.code());
                            success[0] = false;
                            semaphore.release();
                        }
                    } catch (Exception e) {
                        android.util.Log.e("KaidahDetail", "Exception in API response handling", e);
                        success[0] = false;
                        semaphore.release();
                    }
                }

                @Override
                public void onFailure(Call<KaidahListResponse> call, Throwable t) {
                    android.util.Log.e("KaidahDetail", "API call failed", t);
                    success[0] = false;
                    semaphore.release();
                }
            });

            // Wait for the API call to complete (max 10 seconds)
            boolean completed = semaphore.tryAcquire(10, java.util.concurrent.TimeUnit.SECONDS);
            if (!completed) {
                android.util.Log.e("KaidahDetail", "API call timed out");
                return false;
            }

            android.util.Log.d("KaidahDetail", "=== FETCH MATERI FROM API AND SAVE END ===");
            return success[0];

        } catch (Exception e) {
            android.util.Log.e("KaidahDetail", "Exception in fetchMateriFromApiAndSave", e);
            return false;
        }
    }

    /**
     * Load all kaidah from API (always fetch fresh data)
     */
    private void loadAllKaidahFromAPI(int kaidahId) {
        String sessionToken = sessionManager.getAuthToken();
        if (sessionToken == null || sessionToken.isEmpty()) {
            android.util.Log.e("KaidahDetail", "No session token for API fetch");
            if (isAdded() && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Session tidak valid", Toast.LENGTH_SHORT).show();
                });
            }
            return;
        }

        android.util.Log.d("KaidahDetail", "Loading all kaidah from API (fresh data only, no database caching)");

        // Load all kaidah from API
        apiService.getKaidahListWithWrapper().enqueue(new Callback<KaidahListResponse>() {
            @Override
            public void onResponse(Call<KaidahListResponse> call, Response<KaidahListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    KaidahListResponse kaidahResponse = response.body();
                    android.util.Log.d("KaidahDetail", "=== API RESPONSE DEBUG ===");
                    android.util.Log.d("KaidahDetail", "API Response success: " + kaidahResponse.isSuccess());
                    android.util.Log.d("KaidahDetail", "API Response message: " + kaidahResponse.getMessage());

                    if (kaidahResponse.isSuccess() && kaidahResponse.getKaidahList() != null) {
                        List<MateriKaidah> kaidahList = kaidahResponse.getKaidahList();
                        android.util.Log.d("KaidahDetail", "API returned fresh kaidah list size: " + kaidahList.size());

                        // Log all kaidah from API response
                        android.util.Log.d("KaidahDetail", "=== FRESH API KAIDAH LIST ===");
                        for (int i = 0; i < kaidahList.size(); i++) {
                            MateriKaidah kaidah = kaidahList.get(i);
                            android.util.Log.d("KaidahDetail", "Fresh API Kaidah[" + i + "]: ID=" + kaidah.getIdMateri() +
                                    ", Bab=" + kaidah.getIdBab() + ", Urutan=" + kaidah.getUrutan() +
                                    ", Judul=" + kaidah.getJudulMateri());
                        }

                        // Find the specific kaidah by ID
                        MateriKaidah foundKaidah = null;
                        for (MateriKaidah kaidah : kaidahList) {
                            if (kaidah.getIdMateri() == kaidahId) {
                                foundKaidah = kaidah;
                                break;
                            }
                        }

                        if (foundKaidah != null) {
                            android.util.Log.d("KaidahDetail", "Found kaidah in fresh API data: " + foundKaidah.getJudulMateri());

                            // Create final copies for lambda
                            final MateriKaidah finalKaidah = foundKaidah;
                            final List<MateriKaidah> finalKaidahList = kaidahList;
                            final int finalKaidahId = kaidahId;

                            // Update UI immediately with fresh data (no database caching)
                            if (isAdded() && getActivity() != null) {
                                getActivity().runOnUiThread(() -> {
                                    android.util.Log.d("KaidahDetail", "Updating UI with fresh API data");
                                    currentKaidah = finalKaidah;
                                    allKaidahList = finalKaidahList;

                                    // Find current kaidah index in fresh data
                                    for (int i = 0; i < allKaidahList.size(); i++) {
                                        if (allKaidahList.get(i).getIdMateri() == finalKaidahId) {
                                            currentKaidahIndex = i;
                                            break;
                                        }
                                    }

                                    android.util.Log.d("KaidahDetail", "Current kaidah index in fresh data: " + currentKaidahIndex);

                                    // Bind data and update navigation with fresh data
                                    bindData(finalKaidah);
                                    updateNavigationInfo();
                                });
                            }
                        } else {
                            android.util.Log.e("KaidahDetail", "Kaidah " + kaidahId + " not found in API response");
                            if (isAdded() && getActivity() != null) {
                                getActivity().runOnUiThread(() -> {
                                    Toast.makeText(getContext(), "Kaidah tidak ditemukan di server", Toast.LENGTH_SHORT).show();
                                });
                            }
                        }
                    } else {
                        android.util.Log.e("KaidahDetail", "API response not successful: " + kaidahResponse.getMessage());
                        if (isAdded() && getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                Toast.makeText(getContext(), kaidahResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                } else {
                    android.util.Log.e("KaidahDetail", "API call failed: " + response.code());
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Error API: " + response.code(), Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<KaidahListResponse> call, Throwable t) {
                android.util.Log.e("KaidahDetail", "API call failed", t);
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    /**
     * Mark current materi as completed
     */
    private volatile boolean isMarkingCompleted = false;

    private void markCurrentMateriAsCompleted() {
        if (currentKaidah == null || !sessionManager.isLoggedIn() || isMarkingCompleted) {
            return;
        }

        // Prevent double execution
        isMarkingCompleted = true;

        // Update local data immediately
        currentKaidah.setProgressPercentage(100);
        currentKaidah.setCompleted(true);

        // Update UI immediately
        if (isAdded() && getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                bindData(currentKaidah);
            });
        }

        final int currentMateriId = currentKaidah.getIdMateri();

        // Simple background operation without ExecutorService complexity
        new Thread(() -> {
            try {
                int siswaId = sessionManager.getUserId();
                if (siswaId == -1) {
                    isMarkingCompleted = false;
                    return;
                }

                // API-only approach - sync with server API directly
                syncMateriCompletionWithServer(currentMateriId);

            } catch (Exception e) {
                // Log error but don't interrupt flow
                android.util.Log.e("KaidahDetail", "Error marking materi as completed: " + e.getMessage(), e);
            } finally {
                // Reset flag regardless of outcome
                isMarkingCompleted = false;
            }
        }).start();
    }

    /**
     * Sync materi completion with server API
     * Calls the new /api/progress/materi/{id}/complete endpoint
     */
    private void syncMateriCompletionWithServer(int materiId) {
        if (!sessionManager.isLoggedIn()) {
            return;
        }

        // Get API service
        com.khozin.pembelajarankaidah.data.remote.ApiService apiService = com.khozin.pembelajarankaidah.network.RetrofitClient.getInstance().getApiService();

        // Get auth token
        String authToken = sessionManager.getAuthToken();
        if (authToken == null || authToken.isEmpty()) {
            return;
        }

        // Make API call with Authorization header
        retrofit2.Call<com.khozin.pembelajarankaidah.data.model.ApiResponse<java.util.Map<String, Object>>> call =
            apiService.completeMateri("Bearer " + authToken, materiId);

        call.enqueue(new retrofit2.Callback<com.khozin.pembelajarankaidah.data.model.ApiResponse<java.util.Map<String, Object>>>() {
            @Override
            public void onResponse(retrofit2.Call<com.khozin.pembelajarankaidah.data.model.ApiResponse<java.util.Map<String, Object>>> call,
                                   retrofit2.Response<com.khozin.pembelajarankaidah.data.model.ApiResponse<java.util.Map<String, Object>>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    com.khozin.pembelajarankaidah.data.model.ApiResponse<java.util.Map<String, Object>> apiResponse = response.body();

                    if (apiResponse.getStatus() != null && apiResponse.getStatus().equals("success")) {
                        // Progress sync successful - logged but no user interruption
                        android.util.Log.d("KaidahDetail", "✓ Progress successfully synced with server (no toast)");
                    } else {
                        android.util.Log.e("KaidahDetail", "API returned error status: " + apiResponse.getMessage());
                    }
                } else {
                    // Show error message on main thread for critical failures
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Gagal sinkron dengan server: " + response.message(), Toast.LENGTH_LONG).show();
                        });
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<com.khozin.pembelajarankaidah.data.model.ApiResponse<java.util.Map<String, Object>>> call,
                              Throwable t) {
                android.util.Log.e("KaidahDetail", "API call failed: " + t.getMessage(), t);

                // Show error message on main thread for critical failures
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Koneksi gagal: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }
            }
        });
    }

    /**
     * Create siswa record from session data if it doesn't exist in database
     */
    private com.khozin.pembelajarankaidah.data.model.Siswa createSiswaFromSessionData(int siswaId) {
        try {
            android.util.Log.d("KaidahDetail", "=== CREATE SISWA FROM SESSION DEBUG START ===");
            android.util.Log.d("KaidahDetail", "Attempting to create siswa record from session data for ID: " + siswaId);

            // Debug: Check if session manager is logged in
            android.util.Log.d("KaidahDetail", "SessionManager.isLoggedIn(): " + sessionManager.isLoggedIn());
            android.util.Log.d("KaidahDetail", "SessionManager.getUserId(): " + sessionManager.getUserId());
            android.util.Log.d("KaidahDetail", "SessionManager.getUserName(): " + sessionManager.getUserName());

            // Get siswa data from session
            com.khozin.pembelajarankaidah.data.model.Siswa sessionSiswa = sessionManager.getUserData();
            if (sessionSiswa == null) {
                android.util.Log.e("KaidahDetail", "No siswa data found in session");

                // Try to create siswa from basic session info
                android.util.Log.d("KaidahDetail", "Trying to create siswa from basic session info...");
                sessionSiswa = new com.khozin.pembelajarankaidah.data.model.Siswa();
                sessionSiswa.setId(siswaId);
                sessionSiswa.setNis(sessionManager.getUserNis());
                sessionSiswa.setNamaLengkap(sessionManager.getUserName());
                sessionSiswa.setKelas(sessionManager.getUserClass());
                sessionSiswa.setStatus("AKTIF");
                sessionSiswa.setWaktuDibuat(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
                sessionSiswa.setWaktuDiubah(sessionSiswa.getWaktuDibuat());

                android.util.Log.d("KaidahDetail", "Created siswa from basic session: " + sessionSiswa.getNamaLengkap());
            } else {
                android.util.Log.d("KaidahDetail", "Found siswa in session: " + sessionSiswa.getNamaLengkap());
            }

            android.util.Log.d("KaidahDetail", "Session siswa data: " + sessionSiswa.getNamaLengkap() +
                              " (ID: " + sessionSiswa.getId() + ", NIS: " + sessionSiswa.getNis() + ")");

            // Verify the ID matches
            if (sessionSiswa.getId() != siswaId) {
                android.util.Log.e("KaidahDetail", "Session siswa ID (" + sessionSiswa.getId() +
                                  ") doesn't match requested ID (" + siswaId + ")");
                return null;
            }

            // Create new siswa record for database
            com.khozin.pembelajarankaidah.data.model.Siswa newSiswa = new com.khozin.pembelajarankaidah.data.model.Siswa();
            newSiswa.setId(sessionSiswa.getId());
            newSiswa.setNis(sessionSiswa.getNis());
            newSiswa.setNamaLengkap(sessionSiswa.getNamaLengkap());
            newSiswa.setJenisKelamin(sessionSiswa.getJenisKelamin());
            newSiswa.setKelas(sessionSiswa.getKelas());

            // Handle password field - ensure it's not NULL
            String password = sessionSiswa.getKataSandi();
            if (password == null || password.trim().isEmpty()) {
                // Generate default password if not available
                password = "default123";
                android.util.Log.d("KaidahDetail", "Using default password for siswa (NULL/empty)");
            }
            newSiswa.setKataSandi(password);

            newSiswa.setStatus(sessionSiswa.getStatus());
            newSiswa.setWaktuDibuat(sessionSiswa.getWaktuDibuat());
            newSiswa.setWaktuDiubah(sessionSiswa.getWaktuDiubah());

            // Database operations removed - API only approach
            android.util.Log.d("KaidahDetail", "Siswa data handled via API only");
            return newSiswa;

        } catch (Exception e) {
            android.util.Log.e("KaidahDetail", "Error creating siswa from session data: " + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void navigateToFirstMateriOfBab(Bab bab) {
        android.util.Log.d("KaidahDetail", "Navigating to first materi of bab: " + bab.getNamaBab());

        if (bab == null) {
            android.util.Log.e("KaidahDetail", "Bab is null, cannot navigate");
            if (getContext() != null) {
                Toast.makeText(getContext(), "Data bab tidak valid", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        // Find all materi in the specified bab
        java.util.List<MateriKaidah> materiInBab = new java.util.ArrayList<>();
        if (allKaidahList != null) {
            for (MateriKaidah materi : allKaidahList) {
                if (materi.getIdBab() == bab.getIdBab()) {
                    materiInBab.add(materi);
                }
            }
        }

        if (materiInBab.isEmpty()) {
            android.util.Log.e("KaidahDetail", "No materi found in bab: " + bab.getNamaBab());
            if (getContext() != null) {
                Toast.makeText(getContext(), "Tidak ada materi dalam bab ini", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        // Sort materi by urutan
        java.util.Collections.sort(materiInBab, (a, b) -> Integer.compare(a.getUrutanAsInt(), b.getUrutanAsInt()));

        // Get the first materi
        MateriKaidah firstMateri = materiInBab.get(0);
        android.util.Log.d("KaidahDetail", "Found first materi: " + firstMateri.getJudulMateri() +
                          " (Urutan: " + firstMateri.getUrutan() + ")");

        // Navigate to the first materi
        if (getActivity() != null) {
            // Create new instance of KaidahDetailFragment for the first materi
            KaidahDetailFragment newFragment = KaidahDetailFragment.newInstance(firstMateri.getIdMateri());

            // Replace the current fragment
            getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, newFragment)
                .addToBackStack(null)
                .commit();

            android.util.Log.d("KaidahDetail", "Navigation to first materi completed");
        } else {
            android.util.Log.e("KaidahDetail", "Activity is null, cannot navigate");
        }
    }

    /**
     * Synchronize bab data from API to local database
     * This ensures that getNextBab() method works correctly
     */
    private void synchronizeBabData() {
        android.util.Log.d("KaidahDetail", "Synchronizing bab data from API");

        String sessionToken = sessionManager.getAuthToken();
        if (sessionToken == null || sessionToken.isEmpty()) {
            android.util.Log.e("KaidahDetail", "No session token for bab data synchronization");
            return;
        }

        apiService.getChapters("Bearer " + sessionToken).enqueue(new Callback<com.khozin.pembelajarankaidah.data.model.BabListResponse>() {
            @Override
            public void onResponse(Call<com.khozin.pembelajarankaidah.data.model.BabListResponse> call,
                                   Response<com.khozin.pembelajarankaidah.data.model.BabListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    com.khozin.pembelajarankaidah.data.model.BabListResponse babResponse = response.body();
                    android.util.Log.d("KaidahDetail", "Bab API response success: " + babResponse.isSuccess());

                    if (babResponse.isSuccess() && babResponse.getData() != null) {
                        List<Bab> babList = babResponse.getData().getChapters();
                        android.util.Log.d("KaidahDetail", "Bab data from API: " + babList.size() + " chapters");

                        // NO LOCAL DATABASE - Use API data directly
                        // Update the allBabList with fresh bab data
                        allBabList = babList;
                        android.util.Log.d("KaidahDetail", "✓ All bab list updated with bab data (no local database)");

                        // Add a delay to ensure data is loaded before proceeding
                        android.util.Log.d("KaidahDetail", "✅ synchronizeBabData() completed successfully - bab data available for getNextBab()");

                        // Log all bab data for verification
                        android.util.Log.d("KaidahDetail", "=== BAB DATA FROM API (NO LOCAL DB) ===");
                        for (Bab bab : babList) {
                            android.util.Log.d("KaidahDetail", "API Bab: ID=" + bab.getIdBab() +
                                              ", Nama=" + bab.getNamaBab() +
                                              ", Urutan=" + bab.getUrutan());
                        }
                    } else {
                        android.util.Log.e("KaidahDetail", "Bab API response not successful: " + babResponse.getMessage());
                    }
                } else {
                    android.util.Log.e("KaidahDetail", "Bab API call failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<com.khozin.pembelajarankaidah.data.model.BabListResponse> call, Throwable t) {
                android.util.Log.e("KaidahDetail", "Network error while synchronizing bab data: " + t.getMessage(), t);
            }
        });
    }

}