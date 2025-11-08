package com.khozin.pembelajarankaidah.database.entity;

import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

/**
 * Result class for Jawaban statistics query
 */
public class JawabanStatistics {
    @ColumnInfo(name = "total_jawaban")
    public int totalJawaban;

    @ColumnInfo(name = "jawaban_benar")
    public int jawabanBenar;

    @ColumnInfo(name = "jawaban_salah")
    public int jawabanSalah;

    @ColumnInfo(name = "rata_rata_urutan")
    public double rataRataUrutan;

    public JawabanStatistics(int totalJawaban, int jawabanBenar, int jawabanSalah, double rataRataUrutan) {
        this.totalJawaban = totalJawaban;
        this.jawabanBenar = jawabanBenar;
        this.jawabanSalah = jawabanSalah;
        this.rataRataUrutan = rataRataUrutan;
    }

    @NonNull
    @Override
    public String toString() {
        return "JawabanStatistics{" +
                "totalJawaban=" + totalJawaban +
                ", jawabanBenar=" + jawabanBenar +
                ", jawabanSalah=" + jawabanSalah +
                ", rataRataUrutan=" + rataRataUrutan +
                '}';
    }
}

