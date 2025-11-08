package com.khozin.pembelajarankaidah.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.khozin.pembelajarankaidah.data.model.Siswa;
import com.khozin.pembelajarankaidah.data.model.LoginResponse;

/**
 * Session Manager untuk mengelola user session
 * Menggunakan SharedPreferences untuk persist data
 */
public class SessionManager {

    // SharedPreferences keys
    private static final String PREF_NAME = "PembelajaranKaidahSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_AUTH_TOKEN = "authToken";
    private static final String KEY_USER_DATA = "userData";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_NIS = "userNis";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_CLASS = "userClass";
    private static final String KEY_LOGIN_TIME = "loginTime";
    private static final String KEY_TOKEN_EXPIRES = "tokenExpires";
    private static final String KEY_DEVICE_ID = "deviceId";
    private static final String KEY_APP_VERSION = "appVersion";
    private static final String KEY_LAST_ACTIVE = "lastActive";

    // Session timeout (24 jam)
    private static final long SESSION_TIMEOUT = 24 * 60 * 60 * 1000; // 24 jam dalam milliseconds

    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;
    private final Gson gson;

    public SessionManager(@NonNull Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        gson = new Gson();
    }

    /**
     * Simpan login session
     */
    public void createLoginSession(@NonNull LoginResponse loginResponse) {
        android.util.Log.d("SessionManager", "=== SESSION CREATION DEBUG START ===");

        if (!loginResponse.isValid()) {
            android.util.Log.e("SessionManager", "Login response not valid");
            throw new IllegalArgumentException("Login response tidak valid");
        }

        Siswa siswa = loginResponse.getSiswa();
        if (siswa == null) {
            android.util.Log.e("SessionManager", "Siswa data is null");
            throw new IllegalArgumentException("Data siswa tidak boleh null");
        }

        android.util.Log.d("SessionManager", "Creating session for: " + siswa.getNamaLengkap() +
                          " (ID: " + siswa.getId() + ", NIS: " + siswa.getNis() + ")");

        // Save user data
        editor.putBoolean(KEY_IS_LOGGED_IN, true);

        // Fix: Use correct method name from LoginResponse
        String token = loginResponse.getToken(); // Correct method name
        android.util.Log.d("SessionManager", "Token: " + (token != null ? "PRESENT" : "NULL"));
        editor.putString(KEY_AUTH_TOKEN, token);

        editor.putString(KEY_USER_DATA, gson.toJson(siswa));
        editor.putInt(KEY_USER_ID, siswa.getId());
        editor.putString(KEY_USER_NIS, siswa.getNis());
        editor.putString(KEY_USER_NAME, siswa.getNamaLengkap());
        editor.putString(KEY_USER_CLASS, siswa.getKelas());
        editor.putString(KEY_LOGIN_TIME, loginResponse.getLoginTime());
        editor.putString(KEY_TOKEN_EXPIRES, loginResponse.getExpiresAt());
        editor.putString(KEY_DEVICE_ID, generateDeviceId());
        editor.putString(KEY_APP_VERSION, getAppVersion());
        editor.putLong(KEY_LAST_ACTIVE, System.currentTimeMillis());

        android.util.Log.d("SessionManager", "Committing session data...");
        editor.apply();
        android.util.Log.d("SessionManager", "Session data committed successfully");
        android.util.Log.d("SessionManager", "=== SESSION CREATION DEBUG END ===");
    }

    /**
     * Check apakah user sudah login
     */
    public boolean isLoggedIn() {
        if (!preferences.getBoolean(KEY_IS_LOGGED_IN, false)) {
            return false;
        }

        // Check session timeout
        if (isSessionExpired()) {
            logout();
            return false;
        }

        // Check token expiry
        String tokenExpiry = preferences.getString(KEY_TOKEN_EXPIRES, null);
        if (tokenExpiry != null && !tokenExpiry.isEmpty()) {
            try {
                long expiryTime = Long.parseLong(tokenExpiry);
                if (System.currentTimeMillis() >= expiryTime) {
                    logout();
                    return false;
                }
            } catch (NumberFormatException e) {
                // Invalid expiry format, force logout
                logout();
                return false;
            }
        }

        return true;
    }

    /**
     * Check apakah session sudah expired
     */
    private boolean isSessionExpired() {
        long lastActive = preferences.getLong(KEY_LAST_ACTIVE, 0);
        return (System.currentTimeMillis() - lastActive) > SESSION_TIMEOUT;
    }

    /**
     * Update last active time
     */
    public void updateLastActive() {
        editor.putLong(KEY_LAST_ACTIVE, System.currentTimeMillis());
        editor.apply();
    }

    /**
     * Get user data
     */
    @Nullable
    public Siswa getUserData() {
        String userDataJson = preferences.getString(KEY_USER_DATA, null);
        if (userDataJson != null) {
            try {
                return gson.fromJson(userDataJson, Siswa.class);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Get auth token
     */
    @Nullable
    public String getAuthToken() {
        return preferences.getString(KEY_AUTH_TOKEN, null);
    }

    /**
     * Get user ID
     */
    public int getUserId() {
        return preferences.getInt(KEY_USER_ID, -1);
    }

    /**
     * Get user NIS
     */
    @Nullable
    public String getUserNis() {
        return preferences.getString(KEY_USER_NIS, null);
    }

    /**
     * Get user name
     */
    @Nullable
    public String getUserName() {
        return preferences.getString(KEY_USER_NAME, null);
    }

    /**
     * Get user class
     */
    @Nullable
    public String getUserClass() {
        return preferences.getString(KEY_USER_CLASS, null);
    }

    /**
     * Get login time
     */
    @Nullable
    public String getLoginTime() {
        return preferences.getString(KEY_LOGIN_TIME, null);
    }

    /**
     * Get formatted login time
     */
    @NonNull
    public String getFormattedLoginTime() {
        String loginTime = getLoginTime();
        if (loginTime == null || loginTime.isEmpty()) {
            return "N/A";
        }

        try {
            long timestamp = Long.parseLong(loginTime);
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMM yyyy, HH:mm");
            return sdf.format(new java.util.Date(timestamp));
        } catch (NumberFormatException e) {
            return loginTime;
        }
    }

    /**
     * Get device ID
     */
    @NonNull
    public String getDeviceId() {
        String deviceId = preferences.getString(KEY_DEVICE_ID, null);
        if (deviceId == null) {
            deviceId = generateDeviceId();
            editor.putString(KEY_DEVICE_ID, deviceId);
            editor.apply();
        }
        return deviceId;
    }

    /**
     * Update user data
     */
    public void updateUserData(@NonNull Siswa siswa) {
        editor.putString(KEY_USER_DATA, gson.toJson(siswa));
        editor.putString(KEY_USER_NIS, siswa.getNis());
        editor.putString(KEY_USER_NAME, siswa.getNamaLengkap());
        editor.putString(KEY_USER_CLASS, siswa.getKelas());
        editor.apply();
    }

    /**
     * Update auth token
     */
    public void updateAuthToken(@NonNull String token, @Nullable String expiresAt) {
        editor.putString(KEY_AUTH_TOKEN, token);
        if (expiresAt != null) {
            editor.putString(KEY_TOKEN_EXPIRES, expiresAt);
        }
        editor.apply();
    }

    /**
     * Logout user
     */
    public void logout() {
        editor.clear();
        editor.apply();
    }

    /**
     * Check apakah session valid untuk API calls
     */
    public boolean isSessionValid() {
        return isLoggedIn() && getAuthToken() != null && !getAuthToken().isEmpty();
    }

    /**
     * Get session info untuk debugging
     */
    @NonNull
    public String getSessionInfo() {
        return String.format(
            "Session Info:\n" +
            "Logged In: %s\n" +
            "User ID: %d\n" +
            "User NIS: %s\n" +
            "User Name: %s\n" +
            "User Class: %s\n" +
            "Login Time: %s\n" +
            "Device ID: %s\n" +
            "Last Active: %s",
            isLoggedIn(),
            getUserId(),
            getUserNis(),
            getUserName(),
            getUserClass(),
            getFormattedLoginTime(),
            getDeviceId(),
            new java.util.Date(preferences.getLong(KEY_LAST_ACTIVE, 0)).toString()
        );
    }

    /**
     * Generate device ID
     */
    @NonNull
    private String generateDeviceId() {
        return android.provider.Settings.Secure.ANDROID_ID + "_" + System.currentTimeMillis();
    }

    /**
     * Get app version
     */
    @NonNull
    private String getAppVersion() {
        return "1.0.0"; // Placeholder, implement proper version retrieval
    }

    /**
     * Clear all data (untuk testing)
     */
    public void clearAll() {
        editor.clear();
        editor.apply();
    }

    /**
     * Export session data (untuk backup/restore)
     */
    @NonNull
    public String exportSessionData() {
        return gson.toJson(getAllSessionData());
    }

    /**
     * Import session data (untuk backup/restore)
     */
    public boolean importSessionData(@NonNull String sessionDataJson) {
        try {
            SessionData sessionData = gson.fromJson(sessionDataJson, SessionData.class);
            if (sessionData != null) {
                saveSessionData(sessionData);
                return true;
            }
        } catch (Exception e) {
            // Error importing data
        }
        return false;
    }

    /**
     * Get all session data
     */
    private SessionData getAllSessionData() {
        SessionData sessionData = new SessionData();
        sessionData.isLoggedIn = isLoggedIn();
        sessionData.authToken = getAuthToken();
        sessionData.userData = getUserData();
        sessionData.userId = getUserId();
        sessionData.userNis = getUserNis();
        sessionData.userName = getUserName();
        sessionData.userClass = getUserClass();
        sessionData.loginTime = getLoginTime();
        sessionData.tokenExpires = preferences.getString(KEY_TOKEN_EXPIRES, null);
        sessionData.deviceId = getDeviceId();
        sessionData.lastActive = preferences.getLong(KEY_LAST_ACTIVE, 0);
        return sessionData;
    }

    /**
     * Save session data
     */
    private void saveSessionData(@NonNull SessionData sessionData) {
        editor.putBoolean(KEY_IS_LOGGED_IN, sessionData.isLoggedIn);
        editor.putString(KEY_AUTH_TOKEN, sessionData.authToken);
        if (sessionData.userData != null) {
            editor.putString(KEY_USER_DATA, gson.toJson(sessionData.userData));
        }
        editor.putInt(KEY_USER_ID, sessionData.userId);
        editor.putString(KEY_USER_NIS, sessionData.userNis);
        editor.putString(KEY_USER_NAME, sessionData.userName);
        editor.putString(KEY_USER_CLASS, sessionData.userClass);
        editor.putString(KEY_LOGIN_TIME, sessionData.loginTime);
        editor.putString(KEY_TOKEN_EXPIRES, sessionData.tokenExpires);
        editor.putString(KEY_DEVICE_ID, sessionData.deviceId);
        editor.putLong(KEY_LAST_ACTIVE, sessionData.lastActive);
        editor.apply();
    }

    /**
     * Session data class for backup/restore
     */
    private static class SessionData {
        boolean isLoggedIn;
        String authToken;
        Siswa userData;
        int userId;
        String userNis;
        String userName;
        String userClass;
        String loginTime;
        String tokenExpires;
        String deviceId;
        long lastActive;
    }
}