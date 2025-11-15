package com.khozin.pembelajarankaidah.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Model RiwayatBelajar untuk tracking progress pembelajaran siswa per materi
 * API-only model - tidak menggunakan Room Database
 */
public class RiwayatBelajar implements Serializable {

    // Status constants
    public static final String STATUS_BELUM_DIMULAI = "belum_dimulai";
    public static final String STATUS_SEDANG_PROGRES = "sedang_progres";
    public static final String STATUS_SELESAI = "selesai";

    @SerializedName("id_riwayat")
    private int idRiwayat;

    @SerializedName("id_siswa")
    private int idSiswa;

    @SerializedName("id_materi")
    private int idMateri;

    @SerializedName("status_progress")
    private String statusProgress;

    @SerializedName("waktu_mulai")
    private String waktuMulai;

    @SerializedName("waktu_selesai")
    private String waktuSelesai;

    @SerializedName("durasi_menit")
    private int durasiMenit;

    @SerializedName("skor_akhir")
    private int skorAkhir;

    private String catatan;
    private int progressPercentage = 0; // Additional field for compatibility

    // Additional fields for mobile app
    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    // Additional fields from API response
    // Note: persentase_penguasaan dihapus karena logic jadi simpler (0% atau 100%)
    @SerializedName("status")
    private String status;

    // Constructor
    public RiwayatBelajar() {
        this.statusProgress = "STARTED";
        this.durasiMenit = 0;
        this.skorAkhir = 0;
    }

    // Constructor with parameters
    public RiwayatBelajar(int idSiswa, int idMateri) {
        this();
        this.idSiswa = idSiswa;
        this.idMateri = idMateri;
        this.waktuMulai = java.time.Instant.now().toString();
    }

    // Getters and Setters
    public int getIdRiwayat() {
        return idRiwayat;
    }

    public void setIdRiwayat(int idRiwayat) {
        this.idRiwayat = idRiwayat;
    }

    public int getIdSiswa() {
        return idSiswa;
    }

    public void setIdSiswa(int idSiswa) {
        this.idSiswa = idSiswa;
    }

    public int getIdMateri() {
        return idMateri;
    }

    public void setIdMateri(int idMateri) {
        this.idMateri = idMateri;
    }

    public String getStatusProgress() {
        return statusProgress;
    }

    public void setStatusProgress(String statusProgress) {
        this.statusProgress = statusProgress;
    }

    public String getWaktuMulai() {
        return waktuMulai;
    }

    public void setWaktuMulai(String waktuMulai) {
        this.waktuMulai = waktuMulai;
    }

    public String getWaktuSelesai() {
        return waktuSelesai;
    }

    public void setWaktuSelesai(String waktuSelesai) {
        this.waktuSelesai = waktuSelesai;
    }

    public int getDurasiMenit() {
        return durasiMenit;
    }

    public void setDurasiMenit(int durasiMenit) {
        this.durasiMenit = durasiMenit;
    }

    public int getSkorAkhir() {
        return skorAkhir;
    }

    public void setSkorAkhir(int skorAkhir) {
        this.skorAkhir = skorAkhir;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
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
     * Check if learning is completed
     */
    public boolean isCompleted() {
        return "COMPLETED".equals(statusProgress);
    }

    /**
     * Check if learning is in progress
     */
    public boolean isInProgress() {
        return "STARTED".equals(statusProgress);
    }

    /**
     * Mark learning as completed
     */
    public void markAsCompleted() {
        this.statusProgress = "COMPLETED";
        this.waktuSelesai = java.time.Instant.now().toString();
    }

    /**
     * Get completion status as percentage
     */
    public int getCompletionPercentage() {
        if ("COMPLETED".equals(statusProgress)) {
            return 100;
        } else if ("STARTED".equals(statusProgress)) {
            return 50; // Assume halfway for started items
        }
        return 0;
    }

    @Override
    public String toString() {
        return "RiwayatBelajar{" +
                "idRiwayat=" + idRiwayat +
                ", idSiswa=" + idSiswa +
                ", idMateri=" + idMateri +
                ", statusProgress='" + statusProgress + '\'' +
                ", durasiMenit=" + durasiMenit +
                ", skorAkhir=" + skorAkhir +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        RiwayatBelajar that = (RiwayatBelajar) obj;
        return idRiwayat == that.idRiwayat &&
                idSiswa == that.idSiswa &&
                idMateri == that.idMateri;
    }

    @Override
    public int hashCode() {
        int result = idRiwayat;
        result = 31 * result + idSiswa;
        result = 31 * result + idMateri;
        return result;
    }

    // Legacy method aliases for backward compatibility
    public String getStatus() {
        return getStatusProgress();
    }

    public void setStatus(String status) {
        this.statusProgress = status;
    }

    // Note: setPersentasePenguasaan method dihapus karena tidak diperlukan lagi

    public void updateAksesTerakhir() {
        // Update last access time - placeholder for API-only approach
        this.waktuSelesai = java.time.Instant.now().toString();
    }
}