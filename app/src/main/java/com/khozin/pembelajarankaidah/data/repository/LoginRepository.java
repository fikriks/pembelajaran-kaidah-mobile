package com.khozin.pembelajarankaidah.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.khozin.pembelajarankaidah.data.model.ApiResponse;
import com.khozin.pembelajarankaidah.data.model.LoginResponse;
import com.khozin.pembelajarankaidah.data.remote.ApiService;
import com.khozin.pembelajarankaidah.network.RetrofitClient;
import com.khozin.pembelajarankaidah.network.ApiConstants;
import com.khozin.pembelajarankaidah.utils.SessionManager;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository untuk login logic
 * Meng-handle API calls dan session management
 */
public class LoginRepository {

    private final ApiService apiService;
    private final SessionManager sessionManager;
    private final Context context;

    public LoginRepository(@NonNull Context context) {
        this.context = context.getApplicationContext();
        this.apiService = RetrofitClient.getInstance().getApiService();
        this.sessionManager = new SessionManager(this.context);
    }

    /**
     * Login dengan NIS dan password
     */
    public void login(@NonNull String nis, @NonNull String password, @NonNull LoginCallback callback) {
        // Validate input
        if (nis.isEmpty() || password.isEmpty()) {
            callback.onError("NIS dan password harus diisi");
            return;
        }

        // Prepare login request
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("nis", nis.trim());
        loginRequest.put("password", password);
        loginRequest.put("device_info", getDeviceInfo());

        // Show loading state
        callback.onLoading();

        // Execute API call
        apiService.login(loginRequest).enqueue(new Callback<ApiResponse<LoginResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<LoginResponse>> call,
                                   @NonNull Response<ApiResponse<LoginResponse>> response) {
                try {
                    ApiResponse<LoginResponse> apiResponse = response.body();

                    if (response.isSuccessful() && apiResponse != null) {
                        handleLoginSuccess(apiResponse, callback);
                    } else {
                        handleLoginError(response, callback);
                    }
                } catch (Exception e) {
                    callback.onError("Terjadi kesalahan saat memproses response");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<LoginResponse>> call,
                                  @NonNull Throwable t) {
                handleNetworkError(t, callback);
            }
        });
    }

    /**
     * Handle successful login response
     */
    private void handleLoginSuccess(@NonNull ApiResponse<LoginResponse> apiResponse,
                                   @NonNull LoginCallback callback) {
        Log.d("REPO_DEBUG", "=== RESPONSE ANALYSIS ===");
        Log.d("REPO_DEBUG", "Raw response: " + apiResponse.toString());
        Log.d("REPO_DEBUG", "Status: " + apiResponse.getStatus());
        Log.d("REPO_DEBUG", "Message: " + apiResponse.getMessage());
        Log.d("REPO_DEBUG", "Code: " + apiResponse.getCode());
        Log.d("REPO_DEBUG", "isSuccess(): " + apiResponse.isSuccess());
        Log.d("REPO_DEBUG", "hasData(): " + apiResponse.hasData());

        if (!apiResponse.isSuccess()) {
            Log.e("REPO_DEBUG", "API response not success!");
            Log.e("REPO_DEBUG", "Error message: " + apiResponse.getErrorMessage());
            callback.onError(apiResponse.getErrorMessage());
            return;
        }

        LoginResponse loginResponse = apiResponse.getData();
        Log.d("REPO_DEBUG", "LoginResponse: " + (loginResponse != null ? "NOT NULL" : "NULL"));

        if (loginResponse == null || !loginResponse.isValid()) {
            Log.e("REPO_DEBUG", "LoginResponse invalid or null. Valid: " +
                              (loginResponse != null ? loginResponse.isValid() : "NULL"));
            callback.onError("Response data tidak valid");
            return;
        }

        try {
            Log.d("REPO_DEBUG", "Creating session for: " + loginResponse.getUserDisplayName());

            // Save session
            sessionManager.createLoginSession(loginResponse);
            Log.d("REPO_DEBUG", "Session saved");

            // Verify session was saved
            boolean isSessionSaved = sessionManager.isLoggedIn();
            Log.d("REPO_DEBUG", "Session verification: " + isSessionSaved);

            if (isSessionSaved) {
                Log.d("REPO_DEBUG", "Calling onSuccess callback...");
                callback.onSuccess(loginResponse);
                Log.d("REPO_DEBUG", "onSuccess callback called");
            } else {
                Log.e("REPO_DEBUG", "Session creation failed!");
                callback.onError("Sesi login gagal disimpan");
            }

        } catch (Exception e) {
            Log.e("REPO_DEBUG", "Exception: " + e.getMessage(), e);
            callback.onError("Gagal menyimpan session: " + e.getMessage());
        }

        android.util.Log.d("LoginRepository", "=== LOGIN SUCCESS DEBUG END ===");
    }

    /**
     * Handle login error response
     */
    private void handleLoginError(@NonNull Response<ApiResponse<LoginResponse>> response,
                                  @NonNull LoginCallback callback) {
        String errorMessage;

        switch (response.code()) {
            case ApiConstants.STATUS_BAD_REQUEST:
                errorMessage = "Request tidak valid";
                break;
            case ApiConstants.STATUS_UNAUTHORIZED:
                errorMessage = ApiConstants.ERROR_INVALID_CREDENTIALS;
                break;
            case ApiConstants.STATUS_NOT_FOUND:
                errorMessage = ApiConstants.ERROR_USER_NOT_FOUND;
                break;
            case ApiConstants.STATUS_INTERNAL_ERROR:
                errorMessage = ApiConstants.ERROR_SERVER;
                break;
            default:
                errorMessage = "Login gagal (Error " + response.code() + ")";
                break;
        }

        // Try to get error message from response body
        ApiResponse<?> errorResponse = response.body();
        if (errorResponse != null && errorResponse.getMessage() != null) {
            errorMessage = errorResponse.getMessage();
        }

        callback.onError(errorMessage);
    }

    /**
     * Handle network error
     */
    private void handleNetworkError(@NonNull Throwable t, @NonNull LoginCallback callback) {
        String errorMessage;

        if (t instanceof java.net.SocketTimeoutException) {
            errorMessage = ApiConstants.ERROR_TIMEOUT;
        } else if (t instanceof java.net.UnknownHostException ||
                   t instanceof java.net.ConnectException) {
            errorMessage = ApiConstants.ERROR_NETWORK;
        } else if (t instanceof java.io.IOException) {
            errorMessage = "Tidak dapat terhubung ke server";
        } else {
            errorMessage = ApiConstants.ERROR_UNKNOWN + ": " + t.getMessage();
        }

        callback.onError(errorMessage);
    }

    /**
     * Check apakah user sudah login
     */
    public boolean isLoggedIn() {
        return sessionManager.isLoggedIn();
    }

    /**
     * Get session manager
     */
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    /**
     * Logout user
     */
    public void logout() {
        try {
            // Call logout API jika ada token
            String token = sessionManager.getAuthToken();
            if (token != null && !token.isEmpty()) {
                apiService.logout(token).enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Map<String, String>>> call,
                                       Response<ApiResponse<Map<String, String>>> response) {
                        // API logout success/failure tidak masalah
                        // Clear local session
                        sessionManager.logout();
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Map<String, String>>> call,
                                          Throwable t) {
                        // API logout gagal, tetap clear local session
                        sessionManager.logout();
                    }
                });
            } else {
                // Tidak ada token, langsung clear session
                sessionManager.logout();
            }
        } catch (Exception e) {
            // Error saat logout API, tetap clear local session
            sessionManager.logout();
        }
    }

    /**
     * Get device info untuk tracking
     */
    @NonNull
    private String getDeviceInfo() {
        try {
            String manufacturer = android.os.Build.MANUFACTURER;
            String model = android.os.Build.MODEL;
            String version = android.os.Build.VERSION.RELEASE;
            String sdkVersion = String.valueOf(android.os.Build.VERSION.SDK_INT);

            return String.format("%s %s (Android %s, SDK %s)",
                    manufacturer, model, version, sdkVersion);
        } catch (Exception e) {
            return "Android Device";
        }
    }

    /**
     * Get app version
     */
    @NonNull
    private String getAppVersion() {
        try {
            android.content.pm.PackageManager pm = context.getPackageManager();
            android.content.pm.PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            return "1.0.0";
        }
    }

    /**
     * Validate NIS format
     */
    public boolean isValidNis(@NonNull String nis) {
        // Basic NIS validation
        return nis.length() >= 5 && nis.matches("[0-9]+");
    }

    /**
     * Validate password format
     */
    public boolean isValidPassword(@NonNull String password) {
        // Basic password validation
        return password.length() >= 3;
    }

    /**
     * Callback interface untuk login
     */
    public interface LoginCallback {
        void onLoading();
        void onSuccess(@NonNull LoginResponse loginResponse);
        void onError(@NonNull String errorMessage);
    }
}