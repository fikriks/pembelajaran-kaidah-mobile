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
import com.khozin.pembelajarankaidah.database.AppDatabase;
import com.khozin.pembelajarankaidah.database.dao.MateriKaidahDao;
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
    private AppDatabase database;
    private SessionManager sessionManager;
    private ApiService apiService;
    private BabProgressHelper babProgressHelper;
    private MateriKaidah currentKaidah;
    private List<MateriKaidah> allKaidahList;
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

            // Debug logging
            android.util.Log.d("KaidahDetail", String.format(
                "Fragment created with kaidah ID: %d", currentKaidahId
            ));
        } else {
            android.util.Log.e("KaidahDetail", " getArguments() is null!");
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
        database = AppDatabase.getDatabase(requireContext());
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

        btnNextMateri.setOnClickListener(v -> {
            navigateToNextMateri();
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
     * Load kaidah data from database
     */
    private void loadKaidahData() {
        android.util.Log.d("KaidahDetail", String.format(
            "Loading kaidah data with ID: %d", currentKaidahId
        ));

        if (currentKaidahId == 0) {
            android.util.Log.e("KaidahDetail", "Invalid kaidah ID: 0");
            Toast.makeText(getContext(), "ID Kaidah tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                // Load current kaidah from database
                currentKaidah = database.materiKaidahDao().getById(currentKaidahId);

                // Debug: Check if kaidah found
                if (currentKaidah != null) {
                    android.util.Log.d("KaidahDetail", String.format(
                        "Kaidah found: %s", currentKaidah.getJudulKaidah()
                    ));
                } else {
                    android.util.Log.e("KaidahDetail", String.format(
                        "Kaidah with ID %d not found in database. Checking all available kaidah...", currentKaidahId
                    ));

                    // Debug: List all kaidah in database
                    List<MateriKaidah> allKaidahDebug = database.materiKaidahDao().getAllMateriSync();
                    android.util.Log.d("KaidahDetail", String.format(
                        "Total kaidah in database: %d", allKaidahDebug.size()
                    ));
                    for (MateriKaidah k : allKaidahDebug) {
                        android.util.Log.d("KaidahDetail", String.format(
                            "Available kaidah: ID=%d, Judul=%s", k.getIdMateri(), k.getJudulKaidah()
                        ));
                    }
                }

                if (currentKaidah != null) {
                    // Load all kaidah for navigation first
                    allKaidahList = database.materiKaidahDao().getAllMateriSync();

                    // Find current kaidah index in the sorted list
                    for (int i = 0; i < allKaidahList.size(); i++) {
                        if (allKaidahList.get(i).getIdMateri() == currentKaidah.getIdMateri()) {
                            currentKaidahIndex = i;
                            break;
                        }
                    }

                    // Update UI on main thread
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            // Bind data to views
                            bindData(currentKaidah);
                            // Update navigation info
                            updateNavigationInfo();
                        });
                    }
                } else {
                    // Fallback: Try to get kaidah from API
                    android.util.Log.d("KaidahDetail", "Kaidah not found in database, trying API fallback");
                    loadKaidahFromAPI(currentKaidahId);
                }

            } catch (Exception e) {
                e.printStackTrace();
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Gagal memuat data kaidah", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    /**
     * Load all kaidah for navigation
     */
    private void loadAllKaidah() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                // Load all kaidah from database, ordered by urutan
                allKaidahList = database.materiKaidahDao().getAllMateriSync();

                // Find current kaidah index in the sorted list
                if (currentKaidah != null && allKaidahList != null) {
                    for (int i = 0; i < allKaidahList.size(); i++) {
                        if (allKaidahList.get(i).getIdMateri() == currentKaidah.getIdMateri()) {
                            currentKaidahIndex = i;
                            break;
                        }
                    }
                }

                // Update navigation info on main thread
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(this::updateNavigationInfo);
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Fallback to sample data if database fails
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Gagal memuat data kaidah", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    /**
     * Bind data to views
     */
    private void bindData(MateriKaidah kaidah) {
        currentKaidah = kaidah;

        // Set judul
        tvJudulKaidah.setText(kaidah.getJudulKaidah());

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
        if (allKaidahList != null && allKaidahList.size() > 0 && currentKaidah != null) {
            // Get all materi in current bab
            List<MateriKaidah> materiInCurrentBab = new ArrayList<>();
            int currentMateriPositionInBab = 0;

            for (int i = 0; i < allKaidahList.size(); i++) {
                MateriKaidah materi = allKaidahList.get(i);
                if (materi.getIdBab() == currentKaidah.getIdBab()) {
                    materiInCurrentBab.add(materi);
                    // Find position of current materi in this bab
                    if (materi.getIdMateri() == currentKaidah.getIdMateri()) {
                        currentMateriPositionInBab = materiInCurrentBab.size();
                    }
                }
            }

            // Update current materi info based on bab
            if (!materiInCurrentBab.isEmpty()) {
                int currentPosition = currentMateriPositionInBab;
                int totalMateriInBab = materiInCurrentBab.size();
                tvCurrentMateriInfo.setText("Materi " + currentPosition + " dari " + totalMateriInBab + " (Bab " + currentKaidah.getIdBab() + ")");
            } else {
                // Fallback to original logic if no materi found in bab
                int currentPosition = currentKaidahIndex + 1;
                int totalMateri = allKaidahList.size();
                tvCurrentMateriInfo.setText("Materi " + currentPosition + " dari " + totalMateri);
            }

            // Update previous button state
            MateriKaidah previousMateri = getPreviousMateriInSameBab();
            if (previousMateri == null) {
                // First materi in bab, disable previous button
                btnPreviousMateri.setEnabled(false);
                btnPreviousMateri.setAlpha(0.5f);
                btnPreviousMateri.setOnClickListener(null);
            } else {
                // Not first materi, enable previous button
                btnPreviousMateri.setEnabled(true);
                btnPreviousMateri.setAlpha(1.0f);
                btnPreviousMateri.setOnClickListener(v -> {
                    navigateToPreviousMateri();
                });
            }

            // Update next button state
            btnNextMateri.setOnClickListener(v -> {
                // Always call navigateToNextMateri - it will handle the logic internally
                navigateToNextMateri();
            });

            if (currentKaidahIndex >= allKaidahList.size() - 1) {
                // Last materi, change text to "Selesai"
                btnNextMateri.setText("Selesai");
            } else {
                // Not last materi, set text to "Materi Selanjutnya"
                btnNextMateri.setText("Materi Selanjutnya");
            }
        }
    }

    /**
     * Navigate to next materi
     */
    private void navigateToNextMateri() {
        android.util.Log.d("KaidahDetail", "=== NAVIGATE TO NEXT MATERI DEBUG ===");
        android.util.Log.d("KaidahDetail", "Current kaidah index: " + currentKaidahIndex);
        android.util.Log.d("KaidahDetail", "Total kaidah list size: " + (allKaidahList != null ? allKaidahList.size() : "null"));
        android.util.Log.d("KaidahDetail", "Current kaidah: " + (currentKaidah != null ? currentKaidah.getJudulKaidah() : "null"));
        android.util.Log.d("KaidahDetail", "Is logged in: " + sessionManager.isLoggedIn());

        // Get next materi in the same bab
        MateriKaidah nextKaidah = getNextMateriInSameBab();

        if (nextKaidah != null) {
            // Mark current materi as completed before moving to next
            android.util.Log.d("KaidahDetail", "=== NAVIGATION DEBUG: About to call markCurrentMateriAsCompleted() ===");
            android.util.Log.d("KaidahDetail", "=== NAVIGATION DEBUG: Calling markCurrentMateriAsCompleted() for materi: " + currentKaidah.getJudulKaidah());

            // Create a semaphore to wait for completion to finish
            java.util.concurrent.Semaphore completionSemaphore = new java.util.concurrent.Semaphore(0);
            final boolean[] completionSuccess = {false};

            // Run completion marking in background but wait for it to complete
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    android.util.Log.d("KaidahDetail", "=== NAVIGATION DEBUG: Starting markCurrentMateriAsCompleted() execution ===");
                    markCurrentMateriAsCompleted();
                    completionSuccess[0] = true;
                    android.util.Log.d("KaidahDetail", "=== NAVIGATION DEBUG: markCurrentMateriAsCompleted() completed successfully ===");
                } catch (Exception e) {
                    android.util.Log.e("KaidahDetail", "=== NAVIGATION ERROR: Error in markCurrentMateriAsCompleted(): " + e.getMessage(), e);
                    android.util.Log.e("KaidahDetail", "=== NAVIGATION ERROR: Exception details: " + e.getClass().getSimpleName());
                    completionSuccess[0] = false;
                } finally {
                    completionSemaphore.release();
                }
            });

            // Wait for completion to finish (max 5 seconds)
            try {
                completionSemaphore.tryAcquire(5, java.util.concurrent.TimeUnit.SECONDS);
                if (completionSuccess[0]) {
                    android.util.Log.d("KaidahDetail", "Materi completion successful, proceeding to next materi");
                } else {
                    android.util.Log.w("KaidahDetail", "Materi completion may have failed, but proceeding anyway");
                }
            } catch (InterruptedException e) {
                android.util.Log.e("KaidahDetail", "Interrupted while waiting for materi completion", e);
                Thread.currentThread().interrupt();
            }

            // Create final copies for lambda access
            final int nextMateriId = nextKaidah.getIdMateri();
            final MateriKaidah[] updatedNextKaidah = {nextKaidah};

            // Check if next materi exists in database, if not fetch from API
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    MateriKaidah nextKaidahFromDb = database.materiKaidahDao().getById(nextMateriId);

                    if (nextKaidahFromDb == null) {
                        android.util.Log.w("KaidahDetail", "Next materi not found in database, fetching from API...");

                        // Fetch from API and save
                        if (fetchMateriFromApiAndSave(nextMateriId)) {
                            android.util.Log.d("KaidahDetail", "Successfully fetched next materi from API");
                            // Get the updated materi from database
                            nextKaidahFromDb = database.materiKaidahDao().getById(nextMateriId);
                            if (nextKaidahFromDb != null) {
                                updatedNextKaidah[0] = nextKaidahFromDb;
                            }
                        } else {
                            android.util.Log.e("KaidahDetail", "Failed to fetch next materi from API");

                            // Show error on main thread
                            if (isAdded() && getActivity() != null) {
                                getActivity().runOnUiThread(() -> {
                                    Toast.makeText(getContext(), "Gagal memuat materi selanjutnya", Toast.LENGTH_SHORT).show();
                                });
                            }
                            return;
                        }
                    } else {
                        updatedNextKaidah[0] = nextKaidahFromDb;
                    }

                    // Update current index and kaidah on main thread
                    final MateriKaidah finalNextKaidah = updatedNextKaidah[0];
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            for (int i = 0; i < allKaidahList.size(); i++) {
                                if (allKaidahList.get(i).getIdMateri() == finalNextKaidah.getIdMateri()) {
                                    currentKaidahIndex = i;
                                    currentKaidah = finalNextKaidah;
                                    break;
                                }
                            }

                            // Load next kaidah data
                            bindData(finalNextKaidah);

                            // Scroll to top
                            if (getView() != null) {
                                getView().post(() -> {
                                    ((androidx.core.widget.NestedScrollView) getView().findViewById(R.id.scrollView))
                                            .smoothScrollTo(0, 0);
                                });
                            }
                        });
                    }
                } catch (Exception e) {
                    android.util.Log.e("KaidahDetail", "Error checking next materi in database", e);

                    // Continue with navigation even if there's an error
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            for (int i = 0; i < allKaidahList.size(); i++) {
                                if (allKaidahList.get(i).getIdMateri() == nextMateriId) {
                                    currentKaidahIndex = i;
                                    currentKaidah = nextKaidah;
                                    break;
                                }
                            }

                            // Load next kaidah data
                            bindData(nextKaidah);

                            // Scroll to top
                            if (getView() != null) {
                                getView().post(() -> {
                                    ((androidx.core.widget.NestedScrollView) getView().findViewById(R.id.scrollView))
                                            .smoothScrollTo(0, 0);
                                });
                            }
                        });
                    }
                }
            });
        } else {
            // Last materi - mark as completed and check if bab is completed
            android.util.Log.d("KaidahDetail", "Last materi reached. Calling markCurrentMateriAsCompleted()");
            markCurrentMateriAsCompleted();

            // Check if current bab is completed
            checkBabCompletionAndShowCongrats();
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

        // Return next materi if exists
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
     * Check if current bab is completed and show congratulations screen
     */
    private void checkBabCompletionAndShowCongrats() {
        if (currentKaidah == null || allKaidahList == null) {
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
                        // Get next bab for navigation
                        MateriKaidah nextBab = babProgressHelper.getNextBab(allKaidahList, currentKaidah);

                        // Show congratulations fragment
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
            // Convert MateriKaidah to Bab objects
            Bab currentBab = getBabFromMateri(currentMateri);
            Bab nextBab = getBabFromMateri(nextMateri);

            BabCongratsFragment congratsFragment = BabCongratsFragment.newInstance(currentBab, nextBab);

            if (getActivity() != null) {
                getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, congratsFragment)
                    .addToBackStack(null)
                    .commit();
            }
        } catch (Exception e) {
            android.util.Log.e("KaidahDetail", "Error showing congratulations fragment: " + e.getMessage());
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
     * Convert MateriKaidah to Bab by getting the Bab object from database
     */
    private Bab getBabFromMateri(MateriKaidah materi) {
        if (materi == null || database == null) {
            return null;
        }

        try {
            // Get Bab from database using id_bab from MateriKaidah
            return database.babDao().getBabById(materi.getIdBab());
        } catch (Exception e) {
            android.util.Log.e("KaidahDetail", "Error getting Bab from Materi: " + e.getMessage());
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

            // Navigate to KaidahListFragment first to refresh the list
            // Then the user can click on the next bab from the list
            navigateToBabList();
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
            final AppDatabase finalDatabase = database; // Create final copy for lambda
            final MateriKaidahDao finalMateriKaidahDao = finalDatabase.materiKaidahDao(); // Create final DAO for lambda

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
                                    android.util.Log.d("KaidahDetail", "Found materi in API: " + foundKaidah.getJudulKaidah());

                                    // Create final copy for lambda
                                    final MateriKaidah finalFoundKaidah = foundKaidah;

                                    // Save to database on background thread
                                    Executors.newSingleThreadExecutor().execute(() -> {
                                        try {
                                            // Set default values for new materi
                                            finalFoundKaidah.setProgressPercentage(0);
                                            finalFoundKaidah.setCompleted(false);
                                            finalFoundKaidah.setTotalSoal(0);

                                            // Save only the specific materi
                                            finalMateriKaidahDao.insert(finalFoundKaidah);
                                            android.util.Log.d("KaidahDetail", "Successfully saved materi to database");
                                            success[0] = true;
                                        } catch (Exception e) {
                                            android.util.Log.e("KaidahDetail", "Failed to save materi to database", e);
                                            success[0] = false;
                                        } finally {
                                            semaphore.release();
                                        }
                                    });
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
     * Load kaidah from API as fallback
     */
    private void loadKaidahFromAPI(int kaidahId) {
        String sessionToken = sessionManager.getAuthToken();
        if (sessionToken == null || sessionToken.isEmpty()) {
            android.util.Log.e("KaidahDetail", "No session token for API fallback");
            if (isAdded() && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Session tidak valid", Toast.LENGTH_SHORT).show();
                });
            }
            return;
        }

        android.util.Log.d("KaidahDetail", "Loading kaidah " + kaidahId + " from API");

        // Load all kaidah from API and find the specific one
        apiService.getKaidahListWithWrapper().enqueue(new Callback<KaidahListResponse>() {
            @Override
            public void onResponse(Call<KaidahListResponse> call, Response<KaidahListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    KaidahListResponse kaidahResponse = response.body();
                    if (kaidahResponse.isSuccess() && kaidahResponse.getKaidahList() != null) {
                        List<MateriKaidah> kaidahList = kaidahResponse.getKaidahList();

                        // Find the specific kaidah by ID
                        MateriKaidah foundKaidah = null;
                        for (MateriKaidah kaidah : kaidahList) {
                            if (kaidah.getIdMateri() == kaidahId) {
                                foundKaidah = kaidah;
                                break;
                            }
                        }

                        if (foundKaidah != null) {
                            android.util.Log.d("KaidahDetail", "Found kaidah in API: " + foundKaidah.getJudulKaidah());

                            // Create final copies for lambda
                            final MateriKaidah finalKaidah = foundKaidah;
                            final List<MateriKaidah> finalKaidahList = kaidahList;
                            final int finalKaidahId = kaidahId;

                            // Save all kaidah data to database on background thread
                            Executors.newSingleThreadExecutor().execute(() -> {
                                try {
                                    // Save all kaidah from API to database
                                    database.materiKaidahDao().insertAll(finalKaidahList);
                                    android.util.Log.d("KaidahDetail", "Saved " + finalKaidahList.size() + " kaidah to database");
                                } catch (Exception e) {
                                    android.util.Log.e("KaidahDetail", "Failed to save kaidah data to database", e);
                                }
                            });

                            // Update UI on main thread
                            if (isAdded() && getActivity() != null) {
                                getActivity().runOnUiThread(() -> {
                                    currentKaidah = finalKaidah;
                                    allKaidahList = finalKaidahList;

                                    // Find current kaidah index
                                    for (int i = 0; i < allKaidahList.size(); i++) {
                                        if (allKaidahList.get(i).getIdMateri() == finalKaidahId) {
                                            currentKaidahIndex = i;
                                            break;
                                        }
                                    }

                                    // Bind data and update navigation
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
    private void markCurrentMateriAsCompleted() {
        android.util.Log.d("KaidahDetail", "=== MARK MATERI COMPLETED DEBUG START ===");

        if (currentKaidah == null) {
            android.util.Log.e("KaidahDetail", "ERROR: currentKaidah is null, cannot mark as completed");
            return;
        }

        if (!sessionManager.isLoggedIn()) {
            android.util.Log.e("KaidahDetail", "ERROR: User is not logged in, cannot mark as completed");
            return;
        }

        android.util.Log.d("KaidahDetail", "SUCCESS: All checks passed - proceeding to mark materi as completed");
        android.util.Log.d("KaidahDetail", "Materi to complete: " + currentKaidah.getJudulKaidah() + " (ID: " + currentKaidah.getIdMateri() + ")");
        android.util.Log.d("KaidahDetail", "Current progress before marking: " + currentKaidah.getProgressPercentage() + "%");
        android.util.Log.d("KaidahDetail", "Current completed status: " + currentKaidah.isCompleted());

        android.util.Log.d("KaidahDetail", "Marking materi as completed: " + currentKaidah.getJudulKaidah());
        android.util.Log.d("KaidahDetail", "Materi ID: " + currentKaidah.getIdMateri());

        // Update local data immediately
        currentKaidah.setProgressPercentage(100);
        currentKaidah.setCompleted(true);

        // Update UI immediately
        if (isAdded() && getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                bindData(currentKaidah);
            });
        }

        // Create copies of current data to avoid race conditions
        final int currentMateriId = currentKaidah.getIdMateri();
        final String currentMateriJudul = currentKaidah.getJudulKaidah();

        android.util.Log.d("KaidahDetail", "DEBUG: Creating copies - materiId=" + currentMateriId + ", judul=" + currentMateriJudul);

        // Save to database
        Executors.newSingleThreadExecutor().execute(() -> {
            // Error handling variables
            boolean shouldShowErrorToast = false;
            String errorMessage = "Gagal menyimpan progress materi";

            try {
                // Get current student ID
                int siswaId = sessionManager.getUserId();
                android.util.Log.d("KaidahDetail", "DATABASE DEBUG: Student ID: " + siswaId);

                if (siswaId == -1) {
                    android.util.Log.e("KaidahDetail", "DATABASE ERROR: Invalid student ID: " + siswaId + ". Cannot save riwayat belajar.");
                    return;
                }

                // Verify siswa exists in database before proceeding
                com.khozin.pembelajarankaidah.data.model.Siswa siswa = database.siswaDao().getById(siswaId);
                if (siswa == null) {
                    android.util.Log.w("KaidahDetail", "Siswa with ID " + siswaId + " not found in database. Attempting to create from session data.");

                    // Try to create siswa from session data
                    siswa = createSiswaFromSessionData(siswaId);
                    if (siswa == null) {
                        android.util.Log.e("KaidahDetail", "Failed to create siswa record. Cannot save riwayat belajar.");
                        android.util.Log.e("KaidahDetail", "This might indicate a login/session issue or database sync problem.");

                        // Show error message to user
                        if (isAdded() && getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                Toast.makeText(getContext(), "Error: Data siswa tidak ditemukan. Silakan login kembali.", Toast.LENGTH_LONG).show();
                            });
                        }
                        return;
                    } else {
                        android.util.Log.d("KaidahDetail", "Successfully created siswa record from session data: " + siswa.getNamaLengkap());
                    }
                }

                android.util.Log.d("KaidahDetail", "Siswa found: " + siswa.getNamaLengkap() + " (ID: " + siswaId + ")");

                // 1. FIRST: Ensure materi exists in database before updating it
                android.util.Log.d("KaidahDetail", "DATABASE DEBUG: Step 1 - Checking if materi ID " + currentMateriId + " exists in database");
                MateriKaidah materiInDatabase = database.materiKaidahDao().getById(currentMateriId);

                if (materiInDatabase == null) {
                    android.util.Log.e("KaidahDetail", "DATABASE WARNING: Materi ID " + currentMateriId + " does not exist in database!");
                    android.util.Log.e("KaidahDetail", "DATABASE WARNING: Fetching from API and saving to database BEFORE updating progress...");

                    // Try to fetch materi from API and save to database
                    if (fetchMateriFromApiAndSave(currentMateriId)) {
                        android.util.Log.d("KaidahDetail", "DATABASE SUCCESS: Successfully fetched and saved materi from API");
                        // Retry getting materi from database
                        materiInDatabase = database.materiKaidahDao().getById(currentMateriId);
                        if (materiInDatabase == null) {
                            android.util.Log.e("KaidahDetail", "DATABASE ERROR: Still could not find materi after API fetch!");
                            return;
                        }
                    } else {
                        android.util.Log.e("KaidahDetail", "DATABASE ERROR: Failed to fetch materi from API");

                        // Show error toast on main thread
                        if (isAdded() && getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                Toast.makeText(getContext(), "Error: Materi tidak ditemukan. Gagal mengunduh dari server.", Toast.LENGTH_LONG).show();
                            });
                        }
                        return;
                    }
                }

                android.util.Log.d("KaidahDetail", "DATABASE SUCCESS: Materi exists in database: " + materiInDatabase.getJudulKaidah());

                // 2. NOW: Update materi progress
                android.util.Log.d("KaidahDetail", "DATABASE DEBUG: Step 2 - About to update materi progress in materi_kaidah table");
                android.util.Log.d("KaidahDetail", "DATABASE DEBUG: Calling updateProgress with materiId=" + currentMateriId + ", totalSoal=0, progress=100, isCompleted=true");

                int result = database.materiKaidahDao().updateProgress(currentMateriId, 0, 100, true);
                android.util.Log.d("KaidahDetail", "DATABASE DEBUG: Materi progress update result: " + result + " rows affected");

                // Verify the update actually worked
                MateriKaidah updatedMateri = database.materiKaidahDao().getById(currentMateriId);
                if (updatedMateri != null) {
                    android.util.Log.d("KaidahDetail", "DATABASE SUCCESS: Verification - materi progress after update: " + updatedMateri.getProgressPercentage() + "%");
                    android.util.Log.d("KaidahDetail", "DATABASE SUCCESS: Verification - materi completed status after update: " + updatedMateri.isCompleted());
                } else {
                    android.util.Log.e("KaidahDetail", "DATABASE ERROR: Could not verify materi update - materi not found in database after update!");
                }
                android.util.Log.d("KaidahDetail", "DEBUG: currentKaidahIndex = " + currentKaidahIndex);
                android.util.Log.d("KaidahDetail", "DEBUG: Using copied materiId = " + currentMateriId);
                android.util.Log.d("KaidahDetail", "DEBUG: Using copied judul = " + currentMateriJudul);

                android.util.Log.d("KaidahDetail", "Materi ID " + currentMateriId + " exists in database: " + materiInDatabase.getJudulKaidah());

                // 3. Create or update riwayat belajar record
                android.util.Log.d("KaidahDetail", "DATABASE DEBUG: Checking existing riwayat for siswa " + siswaId + " and materi " + currentMateriId);
                RiwayatBelajar existingRiwayat = database.riwayatBelajarDao().getTerakhirBySiswaAndMateri(siswaId, currentMateriId);

                if (existingRiwayat != null) {
                    android.util.Log.d("KaidahDetail", "DATABASE DEBUG: Found existing riwayat - ID: " + existingRiwayat.getIdRiwayat() + ", Status: " + existingRiwayat.getStatus() + ", Progress: " + existingRiwayat.getPersentasePenguasaan() + "%");

                    // Update existing riwayat
                    android.util.Log.d("KaidahDetail", "DATABASE DEBUG: Updating existing riwayat to completed");
                    existingRiwayat.setPersentasePenguasaan(100.0f);
                    existingRiwayat.setStatus("selesai");
                    existingRiwayat.setWaktuDiubah(java.text.DateFormat.getDateTimeInstance().format(new java.util.Date()));
                    android.util.Log.d("KaidahDetail", "DATABASE DEBUG: About to call riwayatBelajarDao().update()");

                    int updateResult = database.riwayatBelajarDao().update(existingRiwayat);
                    android.util.Log.d("KaidahDetail", "DATABASE DEBUG: Existing riwayat update result: " + updateResult + " rows affected");

                    // Verify the riwayat update worked
                    RiwayatBelajar verifyUpdated = database.riwayatBelajarDao().getTerakhirBySiswaAndMateri(siswaId, currentMateriId);
                    if (verifyUpdated != null) {
                        android.util.Log.d("KaidahDetail", "DATABASE DEBUG: Verification - riwayat after update: " + verifyUpdated.getStatus() + ", Progress: " + verifyUpdated.getPersentasePenguasaan() + "%");
                    } else {
                        android.util.Log.e("KaidahDetail", "DATABASE ERROR: Could not verify riwayat update - riwayat not found after update!");
                    }
                } else {
                    android.util.Log.d("KaidahDetail", "DATABASE DEBUG: No existing riwayat found, creating new one");
                    // Create new riwayat with minimal required fields
                    RiwayatBelajar newRiwayat = new RiwayatBelajar();
                    newRiwayat.setIdSiswa(siswaId);
                    newRiwayat.setIdMateri(currentMateriId);
                    newRiwayat.setMateriJudul(currentMateriJudul);
                    newRiwayat.setStatus("selesai");
                    newRiwayat.setPersentasePenguasaan(100.0f);
                    newRiwayat.setWaktuDiubah(java.text.DateFormat.getDateTimeInstance().format(new java.util.Date()));

                    android.util.Log.d("KaidahDetail", "DATABASE DEBUG: About to insert new riwayat with materi ID: " + currentMateriId);
                    android.util.Log.d("KaidahDetail", "DATABASE DEBUG: newRiwayat.toString() = " + newRiwayat.toString());
                    android.util.Log.d("KaidahDetail", "DATABASE DEBUG: New riwayat details - SiswaID: " + siswaId + ", MateriID: " + currentMateriId + ", Status: selesai, Progress: 100%");

                    // Verify materi exists before inserting riwayat
                    MateriKaidah materiForDebug = database.materiKaidahDao().getById(currentMateriId);
                    android.util.Log.d("KaidahDetail", "DEBUG: Materi from DB: " + (materiForDebug != null ? materiForDebug.getJudulKaidah() : "NULL"));

                    // Verify siswa exists
                    com.khozin.pembelajarankaidah.data.model.Siswa siswaForDebug = database.siswaDao().getById(siswaId);
                    android.util.Log.d("KaidahDetail", "DEBUG: Siswa from DB: " + (siswaForDebug != null ? siswaForDebug.getNamaLengkap() : "NULL"));

                    // Only proceed if both siswa and materi exist
                    if (materiForDebug != null && siswaForDebug != null) {
                        try {
                            android.util.Log.d("KaidahDetail", "DATABASE DEBUG: About to call riwayatBelajarDao().insert()");
                            long insertResult = database.riwayatBelajarDao().insert(newRiwayat);
                            android.util.Log.d("KaidahDetail", "DATABASE DEBUG: New riwayat insert result: " + insertResult + " (new record ID)");

                            // Verify the insert worked
                            RiwayatBelajar verifyInserted = database.riwayatBelajarDao().getTerakhirBySiswaAndMateri(siswaId, currentMateriId);
                            if (verifyInserted != null) {
                                android.util.Log.d("KaidahDetail", "DATABASE DEBUG: Verification - riwayat after insert: " + verifyInserted.getStatus() + ", Progress: " + verifyInserted.getPersentasePenguasaan() + "%");
                                android.util.Log.d("KaidahDetail", "DATABASE DEBUG: Verification - riwayat ID: " + verifyInserted.getIdRiwayat());
                            } else {
                                android.util.Log.e("KaidahDetail", "DATABASE ERROR: Could not verify riwayat insert - riwayat not found after insert!");
                            }
                        } catch (Exception e) {
                            android.util.Log.e("KaidahDetail", "Failed to insert riwayat: " + e.getMessage());
                            // Try update if insert fails (possible duplicate)
                            try {
                                com.khozin.pembelajarankaidah.data.model.RiwayatBelajar duplicateRiwayat =
                                    database.riwayatBelajarDao().getTerakhirBySiswaAndMateri(siswaId, currentMateriId);
                                if (duplicateRiwayat != null) {
                                    duplicateRiwayat.setStatus("selesai");
                                    duplicateRiwayat.setPersentasePenguasaan(100.0f);
                                    duplicateRiwayat.setWaktuDiubah(java.text.DateFormat.getDateTimeInstance().format(new java.util.Date()));
                                    int updateResult = database.riwayatBelajarDao().update(duplicateRiwayat);
                                    android.util.Log.d("KaidahDetail", "Updated duplicate riwayat to completed, result: " + updateResult);
                                } else {
                                    shouldShowErrorToast = false;
                                    errorMessage = "Peringatan: Tidak dapat menyimpan riwayat, tapi materi ditandai selesai";
                                }
                            } catch (Exception updateException) {
                                android.util.Log.e("KaidahDetail", "Update also failed: " + updateException.getMessage());
                                shouldShowErrorToast = false;
                                errorMessage = "Peringatan: Database error, tapi materi ditandai selesai";
                            }
                        }
                    } else {
                        android.util.Log.e("KaidahDetail", "Cannot create riwayat - Missing data: materi=" +
                                (materiForDebug != null ? "OK" : "NULL") + ", siswa=" +
                                (siswaForDebug != null ? "OK" : "NULL"));
                        shouldShowErrorToast = false;
                        errorMessage = "Peringatan: Data referensi tidak lengkap, tapi materi ditandai selesai";
                    }
                }

                // 4. Verify the riwayat was saved correctly
                android.util.Log.d("KaidahDetail", "DATABASE DEBUG: Final verification - checking saved riwayat");
                RiwayatBelajar savedRiwayat = database.riwayatBelajarDao().getTerakhirBySiswaAndMateri(siswaId, currentMateriId);
                if (savedRiwayat != null) {
                    android.util.Log.d("KaidahDetail", "DATABASE SUCCESS: Final verification - Saved riwayat status: " + savedRiwayat.getStatus());
                    android.util.Log.d("KaidahDetail", "DATABASE SUCCESS: Final verification - Saved riwayat persentase: " + savedRiwayat.getPersentasePenguasaan() + "%");
                    android.util.Log.d("KaidahDetail", "DATABASE SUCCESS: Final verification - Saved riwayat ID: " + savedRiwayat.getIdRiwayat());
                } else {
                    android.util.Log.e("KaidahDetail", "DATABASE ERROR: Final verification - Failed to retrieve saved riwayat!");
                }

                android.util.Log.d("KaidahDetail", "=== MARK MATERI COMPLETED DEBUG END ===");

                // Sync with server API in background
                android.util.Log.d("KaidahDetail", "SYNC: Starting server sync for materi completion");
                syncMateriCompletionWithServer(currentMateriId);

                // Show success toast and refresh UI on main thread
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Materi \"" + currentMateriJudul + "\" telah selesai!", Toast.LENGTH_SHORT).show();

                        // SIMPLE APPROACH: Force refresh data from database on background thread
                        Executors.newSingleThreadExecutor().execute(() -> {
                            try {
                                // Get fresh data from database with progress
                                List<MateriKaidah> freshDataList = database.materiKaidahDao().getMateriWithProgressSync(1); // Assuming siswa ID = 1 for simple approach
                                MateriKaidah freshData = freshDataList.stream()
                                        .filter(m -> m.getIdMateri() == currentMateriId)
                                        .findFirst()
                                        .orElse(null);

                                if (freshData != null) {
                                    // Update current kaidah with fresh data
                                    currentKaidah.setProgressPercentage(freshData.getProgressPercentage());
                                    currentKaidah.setStatus(freshData.getStatus());
                                    currentKaidah.setCompleted(freshData.isCompleted());

                                    Log.d("KaidahDetail", "SIMPLE FIX: Fresh data from DB - Status: " + freshData.getStatus() + ", Progress: " + freshData.getProgressPercentage() + "%");

                                    // Refresh UI on main thread
                                    if (isAdded() && getActivity() != null) {
                                        getActivity().runOnUiThread(() -> {
                                            // Refresh the current data to show updated status
                                            bindData(currentKaidah);

                                            // Also refresh the kaidah list if available
                                            if (allKaidahList != null && currentKaidahIndex < allKaidahList.size()) {
                                                allKaidahList.set(currentKaidahIndex, currentKaidah);
                                            }
                                        });
                                    }
                                }
                            } catch (Exception dbException) {
                                Log.e("KaidahDetail", "SIMPLE FIX: Error getting fresh data", dbException);
                                // Still bind current data even if refresh fails
                                if (isAdded() && getActivity() != null) {
                                    getActivity().runOnUiThread(() -> bindData(currentKaidah));
                                }
                            }
                        });
                    });
                }

                // Refresh other fragments to show updated progress
                android.util.Log.d("KaidahDetail", "REFRESH FIX: Refreshing other fragments after materi completion");
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        // Try to find and refresh HomeFragment
                        try {
                            androidx.fragment.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            androidx.fragment.app.Fragment homeFragment = fragmentManager.findFragmentByTag("home");
                            if (homeFragment != null && homeFragment instanceof com.khozin.pembelajarankaidah.ui.home.HomeFragment) {
                                android.util.Log.d("KaidahDetail", "REFRESH FIX: Found HomeFragment, calling refresh");
                                ((com.khozin.pembelajarankaidah.ui.home.HomeFragment) homeFragment).refreshData();
                            }
                        } catch (Exception homeException) {
                            android.util.Log.e("KaidahDetail", "REFRESH FIX: Error refreshing HomeFragment", homeException);
                        }

                        // Try to find and refresh KaidahListFragment
                        try {
                            androidx.fragment.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            androidx.fragment.app.Fragment kaidahListFragment = fragmentManager.findFragmentByTag("kaidah");
                            if (kaidahListFragment != null && kaidahListFragment instanceof com.khozin.pembelajarankaidah.ui.kaidah.KaidahListFragment) {
                                android.util.Log.d("KaidahDetail", "REFRESH FIX: Found KaidahListFragment, calling refresh");
                                ((com.khozin.pembelajarankaidah.ui.kaidah.KaidahListFragment) kaidahListFragment).refreshKaidahData();
                            }
                        } catch (Exception kaidahException) {
                            android.util.Log.e("KaidahDetail", "REFRESH FIX: Error refreshing KaidahListFragment", kaidahException);
                        }
                    });
                }

            } catch (Exception e) {
                android.util.Log.e("KaidahDetail", "Failed to save completion to database", e);
                e.printStackTrace();

                // Only show error toast for critical database errors
                if (e instanceof android.database.sqlite.SQLiteConstraintException) {
                    errorMessage = "Error: Data materi tidak valid";
                    shouldShowErrorToast = true;
                } else if (e.getMessage() != null && e.getMessage().contains("FOREIGN KEY")) {
                    errorMessage = "Error: Materi tidak ditemukan di database";
                    shouldShowErrorToast = true;
                } else if (e.getMessage() != null && e.getMessage().contains("UNIQUE constraint")) {
                    errorMessage = "Error: Data duplikat";
                    shouldShowErrorToast = true;
                }

                // Make errorMessage final for lambda use
                final String finalErrorMessage = errorMessage;

                // Only show error toast if it's a real error that prevents completion
                if (shouldShowErrorToast && isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), finalErrorMessage, Toast.LENGTH_LONG).show();
                    });
                } else {
                    android.util.Log.d("KaidahDetail", "Error occurred but not showing toast (non-critical): " + e.getMessage());
                }
            }
        });
    }

    /**
     * Sync materi completion with server API
     * Calls the new /api/progress/materi/{id}/complete endpoint
     */
    private void syncMateriCompletionWithServer(int materiId) {
        android.util.Log.d("KaidahDetail", "=== SYNC MATERI COMPLETION WITH SERVER DEBUG START ===");
        android.util.Log.d("KaidahDetail", "Syncing materi completion for ID: " + materiId);

        if (!sessionManager.isLoggedIn()) {
            android.util.Log.e("KaidahDetail", "Cannot sync - user not logged in");
            return;
        }

        // Get API service
        com.khozin.pembelajarankaidah.data.remote.ApiService apiService = com.khozin.pembelajarankaidah.network.RetrofitClient.getInstance().getApiService();

        // Get auth token
        String authToken = sessionManager.getAuthToken();
        if (authToken == null || authToken.isEmpty()) {
            android.util.Log.e("KaidahDetail", "Cannot sync - no auth token available");
            return;
        }

        android.util.Log.d("KaidahDetail", "Making API call to complete materi: " + materiId);

        // Make API call with Authorization header
        retrofit2.Call<com.khozin.pembelajarankaidah.data.model.ApiResponse<java.util.Map<String, Object>>> call =
            apiService.completeMateri("Bearer " + authToken, materiId);

        call.enqueue(new retrofit2.Callback<com.khozin.pembelajarankaidah.data.model.ApiResponse<java.util.Map<String, Object>>>() {
            @Override
            public void onResponse(retrofit2.Call<com.khozin.pembelajarankaidah.data.model.ApiResponse<java.util.Map<String, Object>>> call,
                                   retrofit2.Response<com.khozin.pembelajarankaidah.data.model.ApiResponse<java.util.Map<String, Object>>> response) {

                android.util.Log.d("KaidahDetail", "API Response received - Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    com.khozin.pembelajarankaidah.data.model.ApiResponse<java.util.Map<String, Object>> apiResponse = response.body();
                    android.util.Log.d("KaidahDetail", "API Success: " + apiResponse.getMessage());

                    if (apiResponse.getStatus() != null && apiResponse.getStatus().equals("success")) {
                        android.util.Log.d("KaidahDetail", "Materi completion synced successfully with server");

                        // Show success message on main thread
                        if (isAdded() && getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                Toast.makeText(getContext(), "Progress berhasil disinkron dengan server", Toast.LENGTH_SHORT).show();
                            });
                        }
                    } else {
                        android.util.Log.e("KaidahDetail", "API returned error status: " + apiResponse.getMessage());
                    }
                } else {
                    android.util.Log.e("KaidahDetail", "API call failed - Code: " + response.code() + ", Message: " + response.message());

                    // Show error message on main thread
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

                // Show error message on main thread
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Koneksi gagal: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }
            }
        });

        android.util.Log.d("KaidahDetail", "=== SYNC MATERI COMPLETION WITH SERVER DEBUG END ===");
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

            // Insert into database
            long insertResult = database.siswaDao().insert(newSiswa);
            android.util.Log.d("KaidahDetail", "Siswa record inserted with result: " + insertResult);

            // Verify the insertion
            com.khozin.pembelajarankaidah.data.model.Siswa insertedSiswa = database.siswaDao().getById(siswaId);
            if (insertedSiswa != null) {
                android.util.Log.d("KaidahDetail", "Siswa record successfully created and verified: " + insertedSiswa.getNamaLengkap());
                return insertedSiswa;
            } else {
                android.util.Log.e("KaidahDetail", "Failed to verify inserted siswa record");
                return null;
            }

        } catch (Exception e) {
            android.util.Log.e("KaidahDetail", "Error creating siswa from session data: " + e.getMessage(), e);
            return null;
        }
    }

}