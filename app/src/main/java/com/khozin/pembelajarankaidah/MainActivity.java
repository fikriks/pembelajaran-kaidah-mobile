package com.khozin.pembelajarankaidah;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.khozin.pembelajarankaidah.data.repository.LoginRepository;
import com.khozin.pembelajarankaidah.utils.SessionManager;
import com.khozin.pembelajarankaidah.data.remote.ApiService;
import com.khozin.pembelajarankaidah.network.RetrofitClient;
import com.khozin.pembelajarankaidah.data.model.ApiResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.khozin.pembelajarankaidah.ui.home.HomeFragment;
import com.khozin.pembelajarankaidah.ui.kaidah.KaidahListFragment;
import com.khozin.pembelajarankaidah.ui.kaidah.KaidahDetailFragment;
import com.khozin.pembelajarankaidah.ui.kaidah.BabCongratsFragment;
import com.khozin.pembelajarankaidah.data.model.Bab;
import com.khozin.pembelajarankaidah.data.model.MateriKaidah;
import com.khozin.pembelajarankaidah.database.AppDatabase;
import com.khozin.pembelajarankaidah.ui.quiz.QuizFragment;
import com.khozin.pembelajarankaidah.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Main Activity dengan authentication check
 * Auto-redirect ke LoginActivity jika belum login
 */
public class MainActivity extends AppCompatActivity implements BabCongratsFragment.KaidahNavigationListener {

    private LoginRepository loginRepository;
    private SessionManager sessionManager;
    private ApiService apiService;
    private BottomNavigationView bottomNavigation;
    private boolean isProgrammaticNavigation = false; // Flag to prevent listener interference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize repository and services
        loginRepository = new LoginRepository(this);
        sessionManager = new SessionManager(this);
        apiService = RetrofitClient.getInstance().getApiService();

        // Check authentication
        checkAuthentication();

        // Setup window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupNavigation();
    }

    /**
     * Check authentication status
     */
    private void checkAuthentication() {
        if (!loginRepository.isLoggedIn()) {
            // User not logged in, redirect to login
            navigateToLogin();
            return;
        }

        // Update last active time
        loginRepository.getSessionManager().updateLastActive();
    }

    /**
     * Initialize views
     */
    private void initViews() {
        bottomNavigation = findViewById(R.id.bottom_navigation);
    }

    /**
     * Setup bottom navigation
     */
    private void setupNavigation() {
        if (bottomNavigation != null) {
            bottomNavigation.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                Fragment selectedFragment = null;

                android.util.Log.d("MainActivity", "📍 Bottom navigation item selected: " + itemId);
                android.util.Log.d("MainActivity", "🔍 isProgrammaticNavigation flag: " + isProgrammaticNavigation);

                // Skip fragment creation if this is programmatic navigation
                if (isProgrammaticNavigation) {
                    android.util.Log.d("MainActivity", "⏭️ Skipping fragment creation due to programmatic navigation flag");
                    isProgrammaticNavigation = false; // Reset flag
                    return true;
                }

                if (itemId == R.id.navigation_home) {
                    selectedFragment = new HomeFragment();
                    android.util.Log.d("MainActivity", "🏠 Creating HomeFragment");
                } else if (itemId == R.id.navigation_kaidah) {
                    selectedFragment = new KaidahListFragment();
                    android.util.Log.d("MainActivity", "📚 Creating KaidahListFragment - this might override detail fragment!");
                } else if (itemId == R.id.navigation_quiz) {
                    selectedFragment = new QuizFragment();
                    android.util.Log.d("MainActivity", "📝 Creating QuizFragment");
                } else if (itemId == R.id.navigation_profile) {
                    selectedFragment = new ProfileFragment();
                    android.util.Log.d("MainActivity", "👤 Creating ProfileFragment");
                }

                if (selectedFragment != null) {
                    android.util.Log.d("MainActivity", "🔄 Loading fragment via bottom navigation: " + selectedFragment.getClass().getSimpleName());
                    loadFragment(selectedFragment);
                    return true;
                }

                return false;
            });

            // Load default fragment (Home)
            android.util.Log.d("MainActivity", "🏠 Loading default HomeFragment");
            loadFragment(new HomeFragment());
            bottomNavigation.setSelectedItemId(R.id.navigation_home);
        }
    }

    /**
     * Load fragment into container
     */
    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            android.util.Log.d("MainActivity", "🔄 loadFragment called with: " + fragment.getClass().getSimpleName());

            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            android.util.Log.d("MainActivity", "📋 Current fragment before load: " +
                (currentFragment != null ? currentFragment.getClass().getSimpleName() : "null"));

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();

            android.util.Log.d("MainActivity", "✅ Fragment transaction committed for: " + fragment.getClass().getSimpleName());
        } else {
            android.util.Log.w("MainActivity", "⚠️ loadFragment called with null fragment");
        }
    }

    /**
     * Navigate to login activity
     */
    private void navigateToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Logout user
     */
    public void logout() {
        loginRepository.logout();
        navigateToLogin();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthentication();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Update last active time
        if (loginRepository.isLoggedIn()) {
            loginRepository.getSessionManager().updateLastActive();
        }
    }

    // Implementation of KaidahNavigationListener interface
    @Override
    public void navigateToBabList() {
        android.util.Log.d("MainActivity", "Navigating back to bab list");
        // Clear back stack to return to bab list
        getSupportFragmentManager()
            .popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);

        // Navigate to kaidah list
        if (bottomNavigation != null) {
            bottomNavigation.setSelectedItemId(R.id.navigation_kaidah);
        }
    }

    @Override
    public void navigateToNextBab(Bab nextBab) {
        android.util.Log.d("MainActivity", "Navigating to next bab: " + nextBab.getNamaBab());
        android.util.Log.d("MainActivity", "Next bab ID: " + nextBab.getIdBab());
        android.util.Log.d("MainActivity", "Next bab Urutan: " + nextBab.getUrutan());

        // Find first materi of the next bab and navigate directly to it
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                android.util.Log.d("MainActivity", "Starting search for first materi of bab ID: " + nextBab.getIdBab());
                List<MateriKaidah> allMateri = AppDatabase.getDatabase(this).materiKaidahDao().getAllMateriSync();
                android.util.Log.d("MainActivity", "Total materi found: " + allMateri.size());

                MateriKaidah firstMateriOfNextBab = null;

                for (MateriKaidah materi : allMateri) {
                    android.util.Log.d("MainActivity", "Checking materi: " + materi.getJudulKaidah() + " (bab ID: " + materi.getIdBab() + ")");
                    if (materi.getIdBab() == nextBab.getIdBab()) {
                        firstMateriOfNextBab = materi;
                        android.util.Log.d("MainActivity", "Found first materi: " + materi.getJudulKaidah());
                        break; // Found the first materi of this bab
                    }
                }

                final MateriKaidah finalMateriOfNextBab = firstMateriOfNextBab; // Make effectively final

                if (finalMateriOfNextBab != null) {
                    android.util.Log.d("MainActivity", "Found first materi: " + finalMateriOfNextBab.getJudulKaidah());
                    final int materiId = finalMateriOfNextBab.getIdMateri(); // Capture ID before lambda

                    // Navigate to detail fragment on main thread
                    runOnUiThread(() -> {
                        navigateToKaidahDetail(materiId);
                    });
                } else {
                    android.util.Log.w("MainActivity", "No materi found for bab: " + nextBab.getNamaBab());
                    // Fallback to bab list if no materi found
                    runOnUiThread(() -> {
                        navigateToBabList();
                    });
                }
            } catch (Exception e) {
                android.util.Log.e("MainActivity", "Error finding first materi of next bab", e);
                // Fallback to bab list on error
                runOnUiThread(() -> {
                    navigateToBabList();
                });
            }
        });
    }

    /**
     * Navigate directly to KaidahDetailFragment with specific materi ID
     */
    private void navigateToKaidahDetail(int materiId) {
        android.util.Log.d("MainActivity", "=== NAVIGATE TO KAIDAH DETAIL STARTED ===");
        android.util.Log.d("MainActivity", "Navigating to kaidah detail with materi ID: " + materiId);
        android.util.Log.d("MainActivity", "Current fragment container ID: R.id.fragment_container");

        // Check current fragment before navigation
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        android.util.Log.d("MainActivity", "Current fragment before navigation: " +
            (currentFragment != null ? currentFragment.getClass().getSimpleName() : "null"));

        // Set flag to prevent bottom navigation listener from interfering
        isProgrammaticNavigation = true;
        android.util.Log.d("MainActivity", "🚩 Set isProgrammaticNavigation flag to true");

        // Create the detail fragment immediately
        KaidahDetailFragment detailFragment = KaidahDetailFragment.newInstance(materiId);
        android.util.Log.d("MainActivity", "✓ Created KaidahDetailFragment with materi ID: " + materiId);

        // Check fragment manager state
        android.util.Log.d("MainActivity", "Fragment manager back stack entry count: " +
            getSupportFragmentManager().getBackStackEntryCount());

        // Clear back stack to prevent going back to kaidah list
        android.util.Log.d("MainActivity", "🗑️ Clearing back stack");
        getSupportFragmentManager().popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);

        // Navigate to detail fragment first
        android.util.Log.d("MainActivity", "🔄 Starting fragment replace transaction");
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit();

        android.util.Log.d("MainActivity", "✅ Fragment transaction committed for materi ID: " + materiId);

        // Then switch to kaidah tab after a small delay to avoid listener interference
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            if (bottomNavigation != null) {
                android.util.Log.d("MainActivity", "📱 Switching to kaidah tab after fragment loaded");
                // Set flag again to ensure no interference
                isProgrammaticNavigation = true;
                bottomNavigation.setSelectedItemId(R.id.navigation_kaidah);
            } else {
                android.util.Log.e("MainActivity", "❌ Bottom navigation is null!");
            }
        }, 100); // 100ms delay to ensure fragment is loaded first

        // Verify fragment was added
        getSupportFragmentManager().executePendingTransactions();
        Fragment newFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        android.util.Log.d("MainActivity", "📋 New fragment after transaction: " +
            (newFragment != null ? newFragment.getClass().getSimpleName() : "null"));
    }

    @Override
    public void navigateToFirstMateriOfBab(Bab bab) {
        android.util.Log.d("MainActivity", "=== NAVIGATE TO FIRST MATERI OF BAB STARTED ===");
        android.util.Log.d("MainActivity", "Navigating to first materi of bab: " + bab.getNamaBab());
        android.util.Log.d("MainActivity", "Bab ID: " + bab.getIdBab());
        android.util.Log.d("MainActivity", "Bab Urutan: " + bab.getUrutan());

        if (bab == null) {
            android.util.Log.e("MainActivity", "Bab is null, cannot navigate");
            return;
        }

        // Get first materi of the specified bab directly from API
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                // Get session token
                String sessionToken = sessionManager.getAuthToken();
                if (sessionToken == null || sessionToken.isEmpty()) {
                    sessionToken = generateDefaultToken();
                }

                // Call API to get first materi of the bab
                apiService.getFirstMateriByBab(bab.getIdBab(), "Bearer " + sessionToken).enqueue(new Callback<ApiResponse<MateriKaidah>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<MateriKaidah>> call, Response<ApiResponse<MateriKaidah>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            MateriKaidah firstMateri = response.body().getData();
                            android.util.Log.d("MainActivity", "✓ Found first materi from API: " + firstMateri.getJudulKaidah() + " (ID: " + firstMateri.getIdMateri() + ", urutan: " + firstMateri.getUrutan() + ")");

                            // Navigate to detail fragment on main thread
                            runOnUiThread(() -> {
                                navigateToKaidahDetail(firstMateri.getIdMateri());
                            });
                        } else {
                            android.util.Log.w("MainActivity", "❌ API response failed for first materi of bab: " + bab.getNamaBab());
                            // Fallback to bab list if API fails
                            runOnUiThread(() -> {
                                navigateToBabList();
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<MateriKaidah>> call, Throwable t) {
                        android.util.Log.e("MainActivity", "❌ API call failed for first materi of bab", t);
                        // Fallback to bab list if network fails
                        runOnUiThread(() -> {
                            navigateToBabList();
                        });
                    }
                });

            } catch (Exception e) {
                android.util.Log.e("MainActivity", "Error finding first materi of bab", e);
                // Fallback to bab list on error
                runOnUiThread(() -> {
                    navigateToBabList();
                });
            }
        });
    }

    /**
     * Generate default token untuk testing
     */
    private String generateDefaultToken() {
        // Simple token generation for testing
        return "1:" + System.currentTimeMillis();
    }
}