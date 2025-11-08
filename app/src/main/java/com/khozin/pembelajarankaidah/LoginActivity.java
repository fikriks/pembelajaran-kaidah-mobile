package com.khozin.pembelajarankaidah;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.khozin.pembelajarankaidah.data.model.LoginResponse;
import com.khozin.pembelajarankaidah.data.repository.LoginRepository;
import com.khozin.pembelajarankaidah.utils.SessionManager;

/**
 * Login Activity dengan API integration lengkap
 * Menggunakan LoginRepository untuk business logic
 */
public class LoginActivity extends AppCompatActivity {

    // UI Components
    private TextInputEditText etNis;
    private TextInputEditText etPassword;
    private TextInputLayout tilNis;
    private TextInputLayout tilPassword;
    private Button btnLogin;
    private ProgressBar progressIndicator;

    // Business Logic
    private LoginRepository loginRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize repository
        loginRepository = new LoginRepository(this);

        // Check jika sudah login
        if (loginRepository.isLoggedIn()) {
            navigateToMain();
            return;
        }

        initViews();
        setupListeners();
    }

    /**
     * Initialize views
     */
    private void initViews() {
        etNis = findViewById(R.id.etNis);
        etPassword = findViewById(R.id.etPassword);
        tilNis = findViewById(R.id.tilNis);
        tilPassword = findViewById(R.id.tilPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressIndicator = findViewById(R.id.progressIndicator);

        // Set initial state
        setViewState(true);
    }

    /**
     * Setup listeners
     */
    private void setupListeners() {
        // Text change listeners untuk validation
        etNis.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateNis();
                updateLoginButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePassword();
                updateLoginButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

  
        // Password toggle is handled automatically by Material Design (endIconMode="password_toggle")

        // Login button click
        btnLogin.setOnClickListener(v -> attemptLogin());
    }

    /**
     * Attempt login
     */
    private void attemptLogin() {
        if (!validateInputs()) {
            return;
        }

        String nis = etNis.getText().toString().trim();
        String password = etPassword.getText().toString();

        // Clear previous errors
        tilNis.setError(null);
        tilPassword.setError(null);

        // Call login API
        loginRepository.login(nis, password, new LoginRepository.LoginCallback() {
            @Override
            public void onLoading() {
                setViewState(false);
            }

            @Override
            public void onSuccess(LoginResponse loginResponse) {
                Log.d("LOGIN_DEBUG", "SUCCESS: Login callback triggered");
                Log.d("LOGIN_DEBUG", "User: " + loginResponse.getUserDisplayName());

                setViewState(true);
                showToast("Login berhasil! Selamat datang, " + loginResponse.getUserDisplayName());

                Log.d("LOGIN_DEBUG", "Toast shown, checking session...");
                boolean isLoggedIn = loginRepository.isLoggedIn();
                Log.d("LOGIN_DEBUG", "Session valid: " + isLoggedIn);

                if (isLoggedIn) {
                    Log.d("LOGIN_DEBUG", "Session valid, navigating to main...");
                    navigateToMain();
                    Log.d("LOGIN_DEBUG", "Navigation completed");
                } else {
                    Log.e("LOGIN_DEBUG", "Session creation failed!");
                    showToast("Error: Sesi tidak tersimpan");
                }
            }

            @Override
            public void onError(String errorMessage) {
                setViewState(true);
                handleLoginError(errorMessage);
            }
        });
    }

    /**
     * Handle login error
     */
    private void handleLoginError(String errorMessage) {
        if (errorMessage.toLowerCase().contains("nis") ||
            errorMessage.toLowerCase().contains("password") ||
            errorMessage.toLowerCase().contains("credential")) {
            // Authentication error
            tilNis.setError("NIS atau password salah");
            // Clear password error to avoid icon conflict with password toggle
            tilPassword.setError(null);
            tilNis.requestFocus();
        } else if (errorMessage.toLowerCase().contains("network") ||
                   errorMessage.toLowerCase().contains("koneksi")) {
            // Network error
            showToast("Periksa koneksi internet Anda");
        } else {
            // Other errors
            showToast(errorMessage);
        }
    }

    /**
     * Validate all inputs
     */
    private boolean validateInputs() {
        boolean nisValid = validateNis();
        boolean passwordValid = validatePassword();
        return nisValid && passwordValid;
    }

    /**
     * Validate NIS
     */
    private boolean validateNis() {
        String nis = etNis.getText().toString().trim();

        if (nis.isEmpty()) {
            tilNis.setError("NIS harus diisi");
            return false;
        }

        if (!loginRepository.isValidNis(nis)) {
            tilNis.setError("NIS minimal 5 digit angka");
            return false;
        }

        tilNis.setError(null);
        return true;
    }

    /**
     * Validate Password
     */
    private boolean validatePassword() {
        String password = etPassword.getText().toString();

        if (password.isEmpty()) {
            tilPassword.setError("Password harus diisi");
            return false;
        }

        if (!loginRepository.isValidPassword(password)) {
            tilPassword.setError("Password minimal 3 karakter");
            return false;
        }

        tilPassword.setError(null);
        return true;
    }

    /**
     * Update login button state based on input validation
     */
    private void updateLoginButtonState() {
        String nis = etNis.getText().toString().trim();
        String password = etPassword.getText().toString();

        boolean enabled = !nis.isEmpty() && !password.isEmpty() &&
                         loginRepository.isValidNis(nis) &&
                         loginRepository.isValidPassword(password);

        btnLogin.setEnabled(enabled);
    }

    /**
     * Set view state (enable/disable inputs and show/hide loading)
     */
    private void setViewState(boolean enabled) {
        etNis.setEnabled(enabled);
        etPassword.setEnabled(enabled);
        btnLogin.setEnabled(enabled);

        if (enabled) {
            btnLogin.setText("Masuk");
            progressIndicator.setVisibility(View.GONE);
        } else {
            btnLogin.setText("Sedang masuk...");
            progressIndicator.setVisibility(View.VISIBLE);
        }

        updateLoginButtonState();
    }

    /**
     * Navigate to main activity
     */
    private void navigateToMain() {
        android.util.Log.d("LoginActivity", "=== NAVIGATE TO MAIN START ===");
        try {
            android.util.Log.d("LoginActivity", "Creating Intent for MainActivity...");
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            android.util.Log.d("LoginActivity", "Starting MainActivity...");
            startActivity(intent);

            android.util.Log.d("LoginActivity", "Finishing LoginActivity...");
            finish();

            android.util.Log.d("LoginActivity", "Navigation to MainActivity completed successfully");
        } catch (Exception e) {
            android.util.Log.e("LoginActivity", "Error during navigation to MainActivity", e);
        }
        android.util.Log.d("LoginActivity", "=== NAVIGATE TO MAIN END ===");
    }

    /**
     * Show toast message
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check login status saat resume
        if (loginRepository.isLoggedIn()) {
            navigateToMain();
        }
    }

    @Override
    @SuppressLint({"OnBackPressed", "GestureBackNavigation"})
    @SuppressWarnings("deprecation")
    public void onBackPressed() {
        // Confirm exit
        if (btnLogin.isEnabled()) {
            super.onBackPressed();
        } else {
            // Jika sedang loading, tetap panggil super untuk mematuhi lifecycle
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up resources jika perlu
    }
}