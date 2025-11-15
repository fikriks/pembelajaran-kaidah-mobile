package com.khozin.pembelajarankaidah.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Model MateriKaidah untuk materi pembelajaran kaidah
 * API-only model - tidak menggunakan Room Database
 */
public class MateriKaidah implements Serializable {

    @SerializedName("id_materi")
    private int idMateri;

    @SerializedName("id_bab")
    private int idBab;

    @SerializedName("judul_kaidah")
    private String judulMateri;

    @SerializedName("penjelasan")
    private String kontenMateri;

    @SerializedName("contoh")
    private String contohKalimat;

    private String urutan;

    @SerializedName("dibuat_oleh")
    private String dibuatOleh;

    private String isActive;

    // Additional fields for mobile app
    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    // Fields from API response
    @SerializedName("waktu_dibuat")
    private String waktuDibuat;

    @SerializedName("waktu_diubah")
    private String waktuDiubah;

    @SerializedName("deskripsi")
    private String deskripsi;

    @SerializedName("deskripsi_bab")
    private String deskripsiBab;

    @SerializedName("progress_percentage")
    private int progressPercentage = 0;

    @SerializedName("status")
    private String status;

    @SerializedName("completed")
    private boolean completed = false;

    // Constructor
    public MateriKaidah() {
        this.isActive = "1"; // Default active
    }

    // Constructor with parameters
    public MateriKaidah(int idBab, String judulMateri, String kontenMateri, String urutan) {
        this();
        this.idBab = idBab;
        this.judulMateri = judulMateri;
        this.kontenMateri = kontenMateri;
        this.urutan = urutan;
    }

    // Getters and Setters
    public int getIdMateri() {
        return idMateri;
    }

    public void setIdMateri(int idMateri) {
        this.idMateri = idMateri;
    }

    public int getIdBab() {
        return idBab;
    }

    public void setIdBab(int idBab) {
        this.idBab = idBab;
    }

    public String getJudulMateri() {
        return judulMateri;
    }

    public void setJudulMateri(String judulMateri) {
        this.judulMateri = judulMateri;
    }

    public String getKontenMateri() {
        return kontenMateri;
    }

    public void setKontenMateri(String kontenMateri) {
        this.kontenMateri = kontenMateri;
    }

    public String getContohKalimat() {
        return contohKalimat;
    }

    public void setContohKalimat(String contohKalimat) {
        this.contohKalimat = contohKalimat;
    }

    public String getUrutan() {
        return urutan;
    }

    public void setUrutan(String urutan) {
        this.urutan = urutan;
    }

    public String getDibuatOleh() {
        return dibuatOleh;
    }

    public void setDibuatOleh(String dibuatOleh) {
        this.dibuatOleh = dibuatOleh;
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
     * Check if materi contains Arabic text
     */
    public boolean isArabicText() {
        if (judulMateri != null) {
            for (int i = 0; i < judulMateri.length(); i++) {
                char c = judulMateri.charAt(i);
                if (c >= 0x0600 && c <= 0x06FF) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get display content (clean from HTML tags if any)
     */
    public String getDisplayContent() {
        if (kontenMateri == null) return "";
        // Remove basic HTML tags if present
        return kontenMateri.replaceAll("<[^>]*>", "").trim();
    }

    /**
     * Get short description for preview
     */
    public String getShortDescription() {
        String content = getDisplayContent();
        if (content.length() > 100) {
            return content.substring(0, 97) + "...";
        }
        return content;
    }

    @Override
    public String toString() {
        return "MateriKaidah{" +
                "idMateri=" + idMateri +
                ", idBab=" + idBab +
                ", judulMateri='" + judulMateri + '\'' +
                ", urutan='" + urutan + '\'' +
                ", isActive='" + isActive + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        MateriKaidah that = (MateriKaidah) obj;
        return idMateri == that.idMateri &&
                idBab == that.idBab;
    }

    @Override
    public int hashCode() {
        int result = idMateri;
        result = 31 * result + idBab;
        return result;
    }

    // Getter methods for new fields
    public String getWaktuDibuat() {
        return waktuDibuat;
    }

    public void setWaktuDibuat(String waktuDibuat) {
        this.waktuDibuat = waktuDibuat;
    }

    public String getWaktuDiubah() {
        return waktuDiubah;
    }

    public void setWaktuDiubah(String waktuDiubah) {
        this.waktuDiubah = waktuDiubah;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getDeskripsiBab() {
        return deskripsiBab;
    }

    public void setDeskripsiBab(String deskripsiBab) {
        this.deskripsiBab = deskripsiBab;
    }

    public int getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(int progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    // Legacy method aliases for backward compatibility
    public String getJudulKaidah() {
        return getJudulMateri();
    }

    public void setJudulKaidah(String judulKaidah) {
        setJudulMateri(judulKaidah);
    }

    // Use deskripsi field for UI, fallback to kontenMateri
    public String getDeskripsiForUI() {
        return deskripsi != null ? deskripsi : getKontenMateri();
    }

    public int getPersentasePenguasaan() {
        return getProgressPercentage();
    }

    // Legacy method aliases for backward compatibility
    public String getPenjelasan() {
        return kontenMateri; // Use kontenMateri as penjelasan
    }

    public String getContoh() {
        return contohKalimat; // Use contohKalimat as contoh
    }

    // Compatibility method for sorting
    public Integer getUrutanAsInt() {
        if (urutan == null || urutan.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(urutan);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}