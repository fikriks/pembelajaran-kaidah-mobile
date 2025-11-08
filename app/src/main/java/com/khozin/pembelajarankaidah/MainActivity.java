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
import com.khozin.pembelajarankaidah.ui.home.HomeFragment;
import com.khozin.pembelajarankaidah.ui.kaidah.KaidahListFragment;
import com.khozin.pembelajarankaidah.ui.kaidah.KaidahDetailFragment;
import com.khozin.pembelajarankaidah.ui.kaidah.BabCongratsFragment;
import com.khozin.pembelajarankaidah.data.model.Bab;
import com.khozin.pembelajarankaidah.data.model.MateriKaidah;
import com.khozin.pembelajarankaidah.database.AppDatabase;
import com.khozin.pembelajarankaidah.ui.quiz.QuizFragment;
import com.khozin.pembelajarankaidah.ui.progress.ProgressFragment;
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
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize repository
        loginRepository = new LoginRepository(this);

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

                if (itemId == R.id.navigation_home) {
                    selectedFragment = new HomeFragment();
                } else if (itemId == R.id.navigation_kaidah) {
                    selectedFragment = new KaidahListFragment();
                } else if (itemId == R.id.navigation_quiz) {
                    selectedFragment = new QuizFragment();
                } else if (itemId == R.id.navigation_progress) {
                    selectedFragment = new ProgressFragment();
                } else if (itemId == R.id.navigation_profile) {
                    selectedFragment = new ProfileFragment();
                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                    return true;
                }

                return false;
            });

            // Load default fragment (Home)
            loadFragment(new HomeFragment());
            bottomNavigation.setSelectedItemId(R.id.navigation_home);
        }
    }

    /**
     * Load fragment into container
     */
    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
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
        android.util.Log.d("MainActivity", "Navigating to kaidah detail with materi ID: " + materiId);

        // Navigate to kaidah tab first
        if (bottomNavigation != null) {
            android.util.Log.d("MainActivity", "Switching to kaidah tab");
            bottomNavigation.setSelectedItemId(R.id.navigation_kaidah);
        }

        // Add a small delay to ensure tab switching completes before navigating to detail
        // Then navigate to detail fragment
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            KaidahDetailFragment detailFragment = KaidahDetailFragment.newInstance(materiId);
            android.util.Log.d("MainActivity", "Created KaidahDetailFragment with materi ID: " + materiId);

            // Clear back stack to prevent going back to kaidah list
            getSupportFragmentManager().popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);

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

            android.util.Log.d("MainActivity", "Fragment transaction completed for materi ID: " + materiId);
        }, 300); // 300ms delay to ensure tab switching completes
    }
}