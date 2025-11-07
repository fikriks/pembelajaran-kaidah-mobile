package com.khozin.pembelajarankaidah.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.annotations.SerializedName;

/**
 * Entity MateriKaidah untuk tabel materi_kaidah
 * Sesuai database schema di CLAUDE.md
 */
@Entity(tableName = "materi_kaidah")
public class MateriKaidah {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_materi")
    private int idMateri;

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

    @SerializedName("tingkat_kesulitan")
    @NonNull
    @ColumnInfo(name = "tingkat_kesulitan")
    private String tingkatKesulitan; // mudah, sedang, sulit

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
    private int totalSoal = 0;
    private boolean isCompleted = false;
    private int progressPercentage = 0;

    // Default constructor
    public MateriKaidah() {
        this.tingkatKesulitan = "mudah";
        this.urutan = 0;
    }

    // Constructor minimal
    public MateriKaidah(String judulKaidah, String tingkatKesulitan, int urutan) {
        this.judulKaidah = judulKaidah;
        this.tingkatKesulitan = tingkatKesulitan;
        this.urutan = urutan;
    }

    // Getters and Setters
    public int getIdMateri() {
        return idMateri;
    }

    public void setIdMateri(int idMateri) {
        this.idMateri = idMateri;
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

    @NonNull
    public String getTingkatKesulitan() {
        return tingkatKesulitan;
    }

    public void setTingkatKesulitan(@NonNull String tingkatKesulitan) {
        this.tingkatKesulitan = tingkatKesulitan;
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

    /**
     * Mendapatkan warna untuk tingkat kesulitan
     */
    public int getTingkatKesulitanColor() {
        switch (tingkatKesulitan.toLowerCase()) {
            case "mudah":
                return android.graphics.Color.parseColor("#4CAF50"); // Green
            case "sedang":
                return android.graphics.Color.parseColor("#FF9800"); // Orange
            case "sulit":
                return android.graphics.Color.parseColor("#F44336"); // Red
            default:
                return android.graphics.Color.parseColor("#9E9E9E"); // Grey
        }
    }

    /**
     * Mendapatkan ikon untuk tingkat kesulitan
     */
    public String getTingkatKesulitanIcon() {
        switch (tingkatKesulitan.toLowerCase()) {
            case "mudah":
                return "😊";
            case "sedang":
                return "😐";
            case "sulit":
                return "😰";
            default:
                return "❓";
        }
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
                ", judulKaidah='" + judulKaidah + '\'' +
                ", tingkatKesulitan='" + tingkatKesulitan + '\'' +
                ", urutan=" + urutan +
                ", totalSoal=" + totalSoal +
                ", progressPercentage=" + progressPercentage +
                '}';
    }
}