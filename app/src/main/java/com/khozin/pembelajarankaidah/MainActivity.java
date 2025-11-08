package com.khozin.pembelajarankaidah;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.khozin.pembelajarankaidah.data.repository.LoginRepository;
import com.khozin.pembelajarankaidah.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Main Activity dengan authentication check
 * Auto-redirect ke LoginActivity jika belum login
 */
public class MainActivity extends AppCompatActivity {

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

                if (itemId == R.id.navigation_home) {
                    // Home fragment
                    return true;
                } else if (itemId == R.id.navigation_kaidah) {
                    // Kaidah list fragment
                    return true;
                } else if (itemId == R.id.navigation_quiz) {
                    // Quiz fragment
                    return true;
                } else if (itemId == R.id.navigation_progress) {
                    // Progress fragment
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    // Profile fragment
                    return true;
                }

                return false;
            });

            // Set default selection
            bottomNavigation.setSelectedItemId(R.id.navigation_home);
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
}