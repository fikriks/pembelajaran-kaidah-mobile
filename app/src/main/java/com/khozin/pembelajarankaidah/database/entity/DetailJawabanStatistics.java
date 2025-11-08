package com.khozin.pembelajarankaidah.database.entity;

import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

/**
 * Result class for DetailJawabanSiswa statistics query
 */
public class DetailJawabanStatistics {
    @ColumnInfo(name = "total_jawaban")
    public int totalJawaban;

    @ColumnInfo(name = "jawaban_benar")
    public int jawabanBenar;

    @ColumnInfo(name = "jawaban_salah")
    public int jawabanSalah;

    @ColumnInfo(name = "rata_rata_waktu")
    public double rataRataWaktu;

    @ColumnInfo(name = "sudah_review")
    public int sudahReview;

    public DetailJawabanStatistics(int totalJawaban, int jawabanBenar, int jawabanSalah, double rataRataWaktu, int sudahReview) {
        this.totalJawaban = totalJawaban;
        this.jawabanBenar = jawabanBenar;
        this.jawabanSalah = jawabanSalah;
        this.rataRataWaktu = rataRataWaktu;
        this.sudahReview = sudahReview;
    }

    @NonNull
    @Override
    public String toString() {
        return "DetailJawabanStatistics{" +
                "totalJawaban=" + totalJawaban +
                ", jawabanBenar=" + jawabanBenar +
                ", jawabanSalah=" + jawabanSalah +
                ", rataRataWaktu=" + rataRataWaktu +
                ", sudahReview=" + sudahReview +
                '}';
    }
}