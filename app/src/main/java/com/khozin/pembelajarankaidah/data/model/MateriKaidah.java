package com.khozin.pembelajarankaidah.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Entity MateriKaidah untuk tabel materi_kaidah
 * Sesuai database schema di CLAUDE.md
 */
@Entity(
    tableName = "materi_kaidah",
    indices = {
        @Index(value = {"id_materi"}, unique = true),
        @Index(value = {"id_bab"}, unique = true),
        @Index(value = {"urutan"}),
        @Index(value = {"id_bab", "urutan"}, unique = true)
    }
)
public class MateriKaidah implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_materi")
    @SerializedName("id_materi")
    private int idMateri;

    @SerializedName("id_bab")
    @ColumnInfo(name = "id_bab")
    private int idBab;

    @SerializedName("judul_kaidah")
    @NonNull
    @ColumnInfo(name = "judul_kaidah")
    private String judulKaidah;

    @SerializedName("deskripsi")
    @Nullable
    @ColumnInfo(name = "deskripsi")
    private String deskripsi;

    @SerializedName("penjelasan")
    @Nullable
    @ColumnInfo(name = "penjelasan")
    private String penjelasan;

    @SerializedName("contoh")
    @Nullable
    @ColumnInfo(name = "contoh")
    private String contoh;

    @SerializedName("urutan")
    @ColumnInfo(name = "urutan")
    private int urutan;

    @ColumnInfo(name = "dibuat_oleh")
    private int dibuatOleh;

    @SerializedName("waktu_dibuat")
    @ColumnInfo(name = "waktu_dibuat")
    private String waktuDibuat;

    @SerializedName("waktu_diubah")
    @ColumnInfo(name = "waktu_diubah")
    private String waktuDiubah;

    // Additional fields untuk mobile app
    @ColumnInfo(name = "total_soal")
    private int totalSoal = 0;

    @ColumnInfo(name = "is_completed")
    private boolean isCompleted = false;

    @ColumnInfo(name = "progress_percentage")
    private int progressPercentage = 0;

    @SerializedName("status")
    @ColumnInfo(name = "status")
    private String status = "belum_dimulai";

    @SerializedName("is_locked")
    @ColumnInfo(name = "is_locked")
    private boolean isLocked = false;

    // Default constructor
    public MateriKaidah() {
        this.urutan = 0;
    }

    // Constructor minimal
    @Ignore
    public MateriKaidah(String judulKaidah, int urutan) {
        this.judulKaidah = judulKaidah;
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

    @NonNull
    public String getJudulKaidah() {
        return judulKaidah;
    }

    public void setJudulKaidah(@NonNull String judulKaidah) {
        this.judulKaidah = judulKaidah;
    }

    @Nullable
    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(@Nullable String deskripsi) {
        this.deskripsi = deskripsi;
    }

    @Nullable
    public String getPenjelasan() {
        return penjelasan;
    }

    public void setPenjelasan(@Nullable String penjelasan) {
        this.penjelasan = penjelasan;
    }

    @Nullable
    public String getContoh() {
        return contoh;
    }

    public void setContoh(@Nullable String contoh) {
        this.contoh = contoh;
    }

    
    public int getUrutan() {
        return urutan;
    }

    public void setUrutan(int urutan) {
        this.urutan = urutan;
    }

    public int getDibuatOleh() {
        return dibuatOleh;
    }

    public void setDibuatOleh(int dibuatOleh) {
        this.dibuatOleh = dibuatOleh;
    }

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

    public int getTotalSoal() {
        return totalSoal;
    }

    public void setTotalSoal(int totalSoal) {
        this.totalSoal = totalSoal;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
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

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public float getPersentasePenguasaan() {
        return progressPercentage;
    }

    
    /**
     * Mendapatkan deskripsi singkat untuk display
     */
    public String getDeskripsiSingkat() {
        if (deskripsi != null && !deskripsi.isEmpty() && deskripsi.length() > 50) {
            return deskripsi.substring(0, 47) + "...";
        }
        return deskripsi != null ? deskripsi : "";
    }

    /**
     * Mendapatkan status pembelajaran dalam bentuk text
     */
    public String getStatusPembelajaran() {
        if (isCompleted) {
            return "Selesai";
        } else if (progressPercentage > 0) {
            return "Sedang Belajar (" + progressPercentage + "%)";
        } else {
            return "Belum Dimulai";
        }
    }

    
    @Override
    public String toString() {
        return "MateriKaidah{" +
                "idMateri=" + idMateri +
                ", idBab=" + idBab +
                ", judulKaidah='" + judulKaidah + '\'' +
                ", urutan=" + urutan +
                ", totalSoal=" + totalSoal +
                ", progressPercentage=" + progressPercentage +
                '}';
    }
}