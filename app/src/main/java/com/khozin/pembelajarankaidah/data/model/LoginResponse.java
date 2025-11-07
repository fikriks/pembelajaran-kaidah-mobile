package com.khozin.pembelajarankaidah.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.annotations.SerializedName;

/**
 * Response model untuk login API
 */
public class LoginResponse {

    @SerializedName("siswa")
    @Nullable
    private Siswa siswa;

    @SerializedName("token")
    @Nullable
    private String token;

    @SerializedName("login_time")
    @Nullable
    private String loginTime;

    @SerializedName("expires_at")
    @Nullable
    private String expiresAt;

    @SerializedName("device_info")
    @Nullable
    private String deviceInfo;

    // Default constructor
    public LoginResponse() {}

    // Constructor minimal
    public LoginResponse(@Nullable Siswa siswa, @Nullable String token, @Nullable String loginTime) {
        this.siswa = siswa;
        this.token = token;
        this.loginTime = loginTime;
    }

    // Getters and Setters
    @Nullable
    public Siswa getSiswa() {
        return siswa;
    }

    public void setSiswa(@Nullable Siswa siswa) {
        this.siswa = siswa;
    }

    @Nullable
    public String getToken() {
        return token;
    }

    public void setToken(@Nullable String token) {
        this.token = token;
    }

    @Nullable
    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(@Nullable String loginTime) {
        this.loginTime = loginTime;
    }

    @Nullable
    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(@Nullable String expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Nullable
    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(@Nullable String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    /**
     * Check apakah login valid (ada token dan siswa data)
     */
    public boolean isValid() {
        return token != null && !token.isEmpty() && siswa != null;
    }

    /**
     * Check apakah token sudah expired
     */
    public boolean isTokenExpired() {
        if (expiresAt == null || expiresAt.isEmpty()) {
            return false; // Tidak ada expiry info, anggap belum expired
        }

        try {
            long expiryTime = Long.parseLong(expiresAt);
            long currentTime = System.currentTimeMillis();
            return currentTime >= expiryTime;
        } catch (NumberFormatException e) {
            return false; // Invalid format, anggap belum expired
        }
    }

    /**
     * Get user display name
     */
    @NonNull
    public String getUserDisplayName() {
        if (siswa != null && siswa.getNamaLengkap() != null) {
            return siswa.getNamaLengkap();
        }
        return "Pengguna";
    }

    /**
     * Get user NIS
     */
    @Nullable
    public String getUserNis() {
        return siswa != null ? siswa.getNis() : null;
    }

    /**
     * Get user class
     */
    @Nullable
    public String getUserClass() {
        return siswa != null ? siswa.getKelas() : null;
    }

    /**
     * Get formatted login time
     */
    @NonNull
    public String getFormattedLoginTime() {
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
     * Get formatted expiry time
     */
    @NonNull
    public String getFormattedExpiryTime() {
        if (expiresAt == null || expiresAt.isEmpty()) {
            return "Tidak ada expiry";
        }

        try {
            long timestamp = Long.parseLong(expiresAt);
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMM yyyy, HH:mm");
            return sdf.format(new java.util.Date(timestamp));
        } catch (NumberFormatException e) {
            return expiresAt;
        }
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "siswa=" + siswa +
                ", token='" + token + '\'' +
                ", loginTime='" + loginTime + '\'' +
                ", expiresAt='" + expiresAt + '\'' +
                ", isValid=" + isValid() +
                '}';
    }
}