package com.khozin.pembelajarankaidah.database.entity;

import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

/**
 * Result class for Soal statistics query
 */
public class SoalStatistics {
    @ColumnInfo(name = "total")
    public int total;

    @ColumnInfo(name = "mudah")
    public int mudah;

    @ColumnInfo(name = "sedang")
    public int sedang;

    @ColumnInfo(name = "sulit")
    public int sulit;

    @ColumnInfo(name = "rata_rata_poin")
    public double rataRataPoin;

    public SoalStatistics(int total, int mudah, int sedang, int sulit, double rataRataPoin) {
        this.total = total;
        this.mudah = mudah;
        this.sedang = sedang;
        this.sulit = sulit;
        this.rataRataPoin = rataRataPoin;
    }

    @NonNull
    @Override
    public String toString() {
        return "SoalStatistics{" +
                "total=" + total +
                ", mudah=" + mudah +
                ", sedang=" + sedang +
                ", sulit=" + sulit +
                ", rataRataPoin=" + rataRataPoin +
                '}';
    }
}

