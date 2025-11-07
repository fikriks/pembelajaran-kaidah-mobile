package com.khozin.pembelajarankaidah;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
    private MaterialButton btnLogin;
    private CircularProgressIndicator progressIndicator;

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
                setViewState(true);
                showToast("Login berhasil! Selamat datang, " + loginResponse.getUserDisplayName());
                navigateToMain();
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
            tilPassword.requestFocus();
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
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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
    public void onBackPressed() {
        // Confirm exit
        if (btnLogin.isEnabled()) {
            super.onBackPressed();
        }
        // Jika sedang loading, prevent back press
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up resources jika perlu
    }
}