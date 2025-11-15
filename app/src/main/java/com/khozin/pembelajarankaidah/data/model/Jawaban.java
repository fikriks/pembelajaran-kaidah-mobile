package com.khozin.pembelajarankaidah.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Model Jawaban untuk tabel pilihan_jawaban
 * API-only model - tidak menggunakan Room Database
 */
public class Jawaban implements Serializable {

    @SerializedName("id_jawaban")
    private int idJawaban;

    @SerializedName("id_soal")
    private int idSoal;

    @SerializedName("teks_jawaban")
    private String teksJawaban;

    @SerializedName("is_benar")
    private boolean isBenar;

    private String urutan;
    private String penjelasan;

    @SerializedName("is_active")
    private String isActive;

    // Additional fields for mobile app
    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    // Additional field for compatibility with getIdPilihan()
    @SerializedName("id_pilihan")
    private Integer idPilihan;

    // Constructor
    public Jawaban() {
        this.isBenar = false;
        this.urutan = "1";
        this.isActive = "1";
    }

    // Constructor with parameters
    public Jawaban(int idSoal, String teksJawaban, boolean isBenar) {
        this();
        this.idSoal = idSoal;
        this.teksJawaban = teksJawaban;
        this.isBenar = isBenar;
    }

    // Getters and Setters
    public int getIdJawaban() {
        return idJawaban;
    }

    public void setIdJawaban(int idJawaban) {
        this.idJawaban = idJawaban;
    }

    public int getIdSoal() {
        return idSoal;
    }

    public void setIdSoal(int idSoal) {
        this.idSoal = idSoal;
    }

    public String getTeksJawaban() {
        return teksJawaban;
    }

    public void setTeksJawaban(String teksJawaban) {
        this.teksJawaban = teksJawaban;
    }

    public boolean isBenar() {
        return isBenar;
    }

    public void setBenar(boolean benar) {
        isBenar = benar;
    }

    public String getUrutan() {
        return urutan;
    }

    public void setUrutan(String urutan) {
        this.urutan = urutan;
    }

    public String getPenjelasan() {
        return penjelasan;
    }

    public void setPenjelasan(String penjelasan) {
        this.penjelasan = penjelasan;
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
     * Check if jawaban contains Arabic text
     */
    public boolean isArabicText() {
        if (teksJawaban != null) {
            for (int i = 0; i < teksJawaban.length(); i++) {
                char c = teksJawaban.charAt(i);
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
        if (teksJawaban == null) return "";
        // Remove basic HTML tags if present
        return teksJawaban.replaceAll("<[^>]*>", "").trim();
    }

    /**
     * Get short answer for preview
     */
    public String getShortAnswer() {
        String text = getDisplayText();
        if (text.length() > 50) {
            return text.substring(0, 47) + "...";
        }
        return text;
    }

    /**
     * Get answer label (A, B, C, D, etc.)
     */
    public String getLabel() {
        if (urutan != null && !urutan.isEmpty()) {
            try {
                int order = Integer.parseInt(urutan);
                return String.valueOf((char) ('A' + order - 1));
            } catch (NumberFormatException e) {
                // If not a number, return first character
                return urutan.substring(0, 1).toUpperCase();
            }
        }
        return "";
    }

    /**
     * Get status text for display
     */
    public String getStatusText() {
        if (isBenar) {
            return "Benar ✅";
        } else {
            return "Salah ❌";
        }
    }

    /**
     * Get status color
     */
    public String getStatusColor() {
        if (isBenar) {
            return "#4CAF50"; // Green
        } else {
            return "#F44336"; // Red
        }
    }

    /**
     * Check if this is the correct answer
     */
    public boolean isCorrectAnswer() {
        return isBenar;
    }

    /**
     * Get comparison text for review
     */
    public String getComparisonText() {
        StringBuilder sb = new StringBuilder();
        sb.append(getLabel()).append(". ").append(getDisplayText());

        if (isBenar) {
            sb.append(" (Jawaban Benar)");
        }

        if (penjelasan != null && !penjelasan.trim().isEmpty()) {
            sb.append("\nPenjelasan: ").append(penjelasan);
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return "Jawaban{" +
                "idJawaban=" + idJawaban +
                ", idSoal=" + idSoal +
                ", teksJawaban='" + teksJawaban + '\'' +
                ", isBenar=" + isBenar +
                ", urutan='" + urutan + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Jawaban jawaban = (Jawaban) obj;
        return idJawaban == jawaban.idJawaban;
    }

    @Override
    public int hashCode() {
        return idJawaban;
    }

    // Getter and setter for idPilihan field
    public Integer getIdPilihan() {
        // If idPilihan is not set, return idJawaban as fallback
        return idPilihan != null ? idPilihan : getIdJawaban();
    }

    public void setIdPilihan(Integer idPilihan) {
        this.idPilihan = idPilihan;
    }

    // Legacy method aliases for backward compatibility
    public int getIdPilihanInt() {
        return getIdJawaban();
    }

    public void setIdPilihanInt(int idPilihan) {
        setIdJawaban(idPilihan);
    }

    public String getJawaban() {
        return getTeksJawaban();
    }

    public void setJawabanText(String jawabanText) {
        setTeksJawaban(jawabanText);
    }
}