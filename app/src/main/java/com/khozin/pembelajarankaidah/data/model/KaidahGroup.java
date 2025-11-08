package com.khozin.pembelajarankaidah.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Model untuk menampung data kaidah yang dikelompokkan per bab
 * Sesuai dengan response dari API endpoint /api/kaidah/grouped
 */
public class KaidahGroup {

    @SerializedName("bab")
    private Bab bab;

    @SerializedName("kaidah_list")
    private List<MateriKaidah> kaidahList;

    @SerializedName("total_kaidah")
    private int totalKaidah;

    @SerializedName("total_soal")
    private int totalSoal;

    // Default constructor
    public KaidahGroup() {
    }

    // Constructor with parameters
    public KaidahGroup(Bab bab, List<MateriKaidah> kaidahList, int totalKaidah, int totalSoal) {
        this.bab = bab;
        this.kaidahList = kaidahList;
        this.totalKaidah = totalKaidah;
        this.totalSoal = totalSoal;
    }

    // Getters and Setters
    public Bab getBab() {
        return bab;
    }

    public void setBab(Bab bab) {
        this.bab = bab;
    }

    public List<MateriKaidah> getKaidahList() {
        return kaidahList;
    }

    public void setKaidahList(List<MateriKaidah> kaidahList) {
        this.kaidahList = kaidahList;
    }

    public int getTotalKaidah() {
        return totalKaidah;
    }

    public void setTotalKaidah(int totalKaidah) {
        this.totalKaidah = totalKaidah;
    }

    public int getTotalSoal() {
        return totalSoal;
    }

    public void setTotalSoal(int totalSoal) {
        this.totalSoal = totalSoal;
    }

    /**
     * Mendapatkan judul bab untuk display
     */
    public String getJudulBab() {
        return bab != null ? bab.getNamaBab() : "";
    }

    /**
     * Mendapatkan nama bab saja (tanpa nomor)
     */
    public String getNamaBabSaja() {
        return bab != null ? bab.getNamaBabSaja() : "";
    }

    /**
     * Mendapatkan nomor bab
     */
    public String getNomorBab() {
        return bab != null ? bab.getNomorBab() : "";
    }

    /**
     * Mendapatkan deskripsi bab
     */
    public String getDeskripsiBab() {
        return bab != null ? bab.getDeskripsi() : "";
    }

    /**
     * Check apakah group ini kosong
     */
    public boolean isEmpty() {
        return kaidahList == null || kaidahList.isEmpty();
    }

    /**
     * Mendapatkan jumlah kaidah yang sudah selesai
     */
    public int getCompletedKaidah() {
        if (kaidahList == null || kaidahList.isEmpty()) {
            return 0;
        }

        int completed = 0;
        for (MateriKaidah kaidah : kaidahList) {
            if ("selesai".equals(kaidah.getStatus())) {
                completed++;
            }
        }
        return completed;
    }

    /**
     * Mendapatkan progress percentage untuk bab ini
     */
    public int getProgressPercentage() {
        if (totalKaidah == 0) return 0;
        return (getCompletedKaidah() * 100) / totalKaidah;
    }

    /**
     * Mendapatkan status text untuk bab ini
     */
    public String getStatusText() {
        int progress = getProgressPercentage();
        if (progress >= 100) {
            return "Selesai (" + getCompletedKaidah() + "/" + totalKaidah + ")";
        } else if (progress > 0) {
            return "Sedang Belajar (" + getCompletedKaidah() + "/" + totalKaidah + ")";
        } else {
            return "Belum Dimulai";
        }
    }

    @Override
    public String toString() {
        return "KaidahGroup{" +
                "bab=" + (bab != null ? bab.getNamaBab() : "null") +
                ", totalKaidah=" + totalKaidah +
                ", totalSoal=" + totalSoal +
                ", progress=" + getProgressPercentage() + "%" +
                '}';
    }
}