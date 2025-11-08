package com.khozin.pembelajarankaidah.ui.kaidah;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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
import com.khozin.pembelajarankaidah.data.model.KaidahListResponse;
import com.khozin.pembelajarankaidah.database.AppDatabase;
import com.khozin.pembelajarankaidah.utils.SessionManager;
import com.khozin.pembelajarankaidah.data.remote.ApiService;
import com.khozin.pembelajarankaidah.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment untuk menampilkan detail materi kaidah
 * Menampilkan penjelasan, contoh, dan tombol untuk memulai latihan
 */
public class KaidahDetailFragment extends Fragment {

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
    }

    /**
     * Setup listeners
     */
    private void setupListeners() {
        btnBack.setOnClickListener(v -> {
            // Go back to kaidah list
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
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
        if (allKaidahList != null && allKaidahList.size() > 0) {
            // Update current materi info
            int currentPosition = currentKaidahIndex + 1;
            int totalMateri = allKaidahList.size();
            tvCurrentMateriInfo.setText("Materi " + currentPosition + " dari " + totalMateri);

            // Update previous button state
            if (currentKaidahIndex <= 0) {
                // First materi, disable previous button
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
            if (currentKaidahIndex >= allKaidahList.size() - 1) {
                // Last materi, change text to "Selesai"
                btnNextMateri.setText("Selesai");
                btnNextMateri.setOnClickListener(v -> {
                    // Navigate back to list or show completion message
                    if (getFragmentManager() != null) {
                        getFragmentManager().popBackStack();
                    }
                    Toast.makeText(getContext(), "Selamat! Anda telah menyelesaikan semua materi.",
                            Toast.LENGTH_LONG).show();
                });
            } else {
                // Not last materi, set text to "Materi Selanjutnya"
                btnNextMateri.setText("Materi Selanjutnya");
                btnNextMateri.setOnClickListener(v -> {
                    navigateToNextMateri();
                });
            }
        }
    }

    /**
     * Navigate to next materi
     */
    private void navigateToNextMateri() {
        if (allKaidahList != null && currentKaidahIndex < allKaidahList.size() - 1) {
            // Mark current materi as completed before moving to next
            markCurrentMateriAsCompleted();

            // Move to next materi
            currentKaidahIndex++;
            MateriKaidah nextKaidah = allKaidahList.get(currentKaidahIndex);

            // Load next kaidah data
            bindData(nextKaidah);

  
            // Scroll to top
            if (getView() != null) {
                getView().post(() -> {
                    ((androidx.core.widget.NestedScrollView) getView().findViewById(R.id.scrollView))
                            .smoothScrollTo(0, 0);
                });
            }
        } else {
            // Last materi - mark as completed and show completion message
            markCurrentMateriAsCompleted();

            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }
            Toast.makeText(getContext(), "Selamat! Anda telah menyelesaikan semua materi.",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Navigate to previous materi
     */
    private void navigateToPreviousMateri() {
        if (allKaidahList != null && currentKaidahIndex > 0) {
            // Move to previous materi
            currentKaidahIndex--;
            MateriKaidah previousKaidah = allKaidahList.get(currentKaidahIndex);

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
        if (currentKaidah != null && sessionManager.isLoggedIn()) {
            android.util.Log.d("KaidahDetail", "Marking materi as completed: " + currentKaidah.getJudulKaidah());

            // Update local data immediately
            currentKaidah.setProgressPercentage(100);
            currentKaidah.setCompleted(true);

            // Update UI
            if (isAdded() && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    bindData(currentKaidah);
                    Toast.makeText(getContext(), "Materi \"" + currentKaidah.getJudulKaidah() + "\" telah selesai!", Toast.LENGTH_SHORT).show();
                });
            }

            // Save to database
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    database.materiKaidahDao().updateProgress(currentKaidah.getIdMateri(), 0, 100, true);
                    android.util.Log.d("KaidahDetail", "Materi completion saved to database");
                } catch (Exception e) {
                    android.util.Log.e("KaidahDetail", "Failed to save completion to database", e);
                }
            });

          }
    }

}