package com.khozin.pembelajarankaidah.database.entity;

import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

/**
 * Result class for SesiLatihan statistics query
 */
public class SesiLatihanStatistics {
    @ColumnInfo(name = "total_sesi")
    public int totalSesi;

    @ColumnInfo(name = "selesai")
    public int selesai;

    @ColumnInfo(name = "rata_rata_skor")
    public double rataRataSkor;

    @ColumnInfo(name = "skor_tertinggi")
    public double skorTertinggi;

    @ColumnInfo(name = "total_waktu")
    public long totalWaktu;

    public SesiLatihanStatistics(int totalSesi, int selesai, double rataRataSkor, double skorTertinggi, long totalWaktu) {
        this.totalSesi = totalSesi;
        this.selesai = selesai;
        this.rataRataSkor = rataRataSkor;
        this.skorTertinggi = skorTertinggi;
        this.totalWaktu = totalWaktu;
    }

    // Getter methods
    public int getTotalSesi() {
        return totalSesi;
    }

    public int getSelesai() {
        return selesai;
    }

    public float getRataRataSkor() {
        return (float) rataRataSkor;
    }

    public double getSkorTertinggi() {
        return skorTertinggi;
    }

    public long getTotalWaktu() {
        return totalWaktu;
    }

    @NonNull
    @Override
    public String toString() {
        return "SesiLatihanStatistics{" +
                "totalSesi=" + totalSesi +
                ", selesai=" + selesai +
                ", rataRataSkor=" + rataRataSkor +
                ", skorTertinggi=" + skorTertinggi +
                ", totalWaktu=" + totalWaktu +
                '}';
    }
}

