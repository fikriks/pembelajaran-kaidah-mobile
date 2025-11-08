package com.khozin.pembelajarankaidah.database.entity;

import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

/**
 * Result class for SesiLatihan performance per materi
 */
public class SesiPerformaPerMateri {
    @ColumnInfo(name = "judul_kaidah")
    public String judulKaidah;

    @ColumnInfo(name = "total_sesi")
    public int totalSesi;

    @ColumnInfo(name = "rata_rata_skor")
    public double rataRataSkor;

    @ColumnInfo(name = "skor_tertinggi")
    public double skorTertinggi;

    public SesiPerformaPerMateri() {}

    @NonNull
    @Override
    public String toString() {
        return "SesiPerformaPerMateri{" +
                "judulKaidah='" + judulKaidah + '\'' +
                ", totalSesi=" + totalSesi +
                ", rataRataSkor=" + rataRataSkor +
                ", skorTertinggi=" + skorTertinggi +
                '}';
    }
}