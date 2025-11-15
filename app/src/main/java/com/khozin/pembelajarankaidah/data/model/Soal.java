package com.khozin.pembelajarankaidah.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Model Soal untuk tabel soal
 * API-only model - tidak menggunakan Room Database
 */
public class Soal implements Serializable {

    @SerializedName("id_soal")
    private int idSoal;

    @SerializedName("id_materi")
    private int idMateri;

    @SerializedName("teks_soal")
    private String teksSoal;

    @SerializedName("tipe_soal")
    private String tipeSoal;

    @SerializedName("tingkat_kesulitan")
    private String tingkatKesulitan;

    @SerializedName("is_active")
    private String isActive;

    // Additional fields for mobile app
    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    // Constructor
    public Soal() {
        this.tipeSoal = "pilihan_ganda";
        this.tingkatKesulitan = "sedang";
        this.isActive = "1";
    }

    // Constructor with parameters
    public Soal(int idMateri, String teksSoal) {
        this();
        this.idMateri = idMateri;
        this.teksSoal = teksSoal;
    }

    // Getters and Setters
    public int getIdSoal() {
        return idSoal;
    }

    public void setIdSoal(int idSoal) {
        this.idSoal = idSoal;
    }

    public int getIdMateri() {
        return idMateri;
    }

    public void setIdMateri(int idMateri) {
        this.idMateri = idMateri;
    }

    public String getTeksSoal() {
        return teksSoal;
    }

    public void setTeksSoal(String teksSoal) {
        this.teksSoal = teksSoal;
    }

    public String getTipeSoal() {
        return tipeSoal;
    }

    public void setTipeSoal(String tipeSoal) {
        this.tipeSoal = tipeSoal;
    }

    public String getTingkatKesulitan() {
        return tingkatKesulitan;
    }

    public void setTingkatKesulitan(String tingkatKesulitan) {
        this.tingkatKesulitan = tingkatKesulitan;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public boolean isActive() {
        return "1".equals(isActive);
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Check if soal contains Arabic text
     */
    public boolean isArabicText() {
        if (teksSoal != null) {
            for (int i = 0; i < teksSoal.length(); i++) {
                char c = teksSoal.charAt(i);
                if (c >= 0x0600 && c <= 0x06FF) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get display text (clean from HTML tags if any)
     */
    public String getDisplayText() {
        if (teksSoal == null) return "";
        // Remove basic HTML tags if present
        return teksSoal.replaceAll("<[^>]*>", "").trim();
    }

    /**
     * Get short question for preview
     */
    public String getShortQuestion() {
        String text = getDisplayText();
        if (text.length() > 100) {
            return text.substring(0, 97) + "...";
        }
        return text;
    }

    /**
     * Get difficulty level as color
     */
    public String getDifficultyColor() {
        if ("mudah".equals(tingkatKesulitan)) {
            return "#4CAF50"; // Green
        } else if ("sedang".equals(tingkatKesulitan)) {
            return "#FF9800"; // Orange
        } else if ("sulit".equals(tingkatKesulitan)) {
            return "#F44336"; // Red
        }
        return "#2196F3"; // Blue default
    }

    /**
     * Get question type display text
     */
    public String getTipeSoalDisplay() {
        if ("pilihan_ganda".equals(tipeSoal)) {
            return "Pilihan Ganda";
        } else if ("essay".equals(tipeSoal)) {
            return "Essay";
        } else if ("benar_salah".equals(tipeSoal)) {
            return "Benar/Salah";
        }
        return tipeSoal;
    }

    @Override
    public String toString() {
        return "Soal{" +
                "idSoal=" + idSoal +
                ", idMateri=" + idMateri +
                ", tipeSoal='" + tipeSoal + '\'' +
                ", tingkatKesulitan='" + tingkatKesulitan + '\'' +
                ", isActive='" + isActive + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Soal soal = (Soal) obj;
        return idSoal == soal.idSoal;
    }

    @Override
    public int hashCode() {
        return idSoal;
    }

    // Legacy method aliases for backward compatibility
    public String getPertanyaan() {
        return getTeksSoal();
    }

    public void setPertanyaan(String pertanyaan) {
        setTeksSoal(pertanyaan);
    }

    public void setIdBab(int idBab) {
        // Note: Soal model doesn't have idBab field in API-only version
        // This method is for compatibility only
    }

    private int poin = 0; // Additional field for compatibility

    public int getPoin() {
        return poin;
    }

    public void setPoin(int poin) {
        this.poin = poin;
    }
}