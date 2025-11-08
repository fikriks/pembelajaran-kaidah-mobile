package com.khozin.pembelajarankaidah.database.entity;

import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

/**
 * Result class for analytics by date
 */
public class AnalyticsByDate {
    @ColumnInfo(name = "tanggal")
    public String tanggal;

    @ColumnInfo(name = "total_jawaban")
    public int totalJawaban;

    @ColumnInfo(name = "jawaban_benar")
    public int jawabanBenar;

    @ColumnInfo(name = "rata_waktu")
    public double rataWaktu;

    public AnalyticsByDate() {}

    @NonNull
    @Override
    public String toString() {
        return "AnalyticsByDate{" +
                "tanggal='" + tanggal + '\'' +
                ", totalJawaban=" + totalJawaban +
                ", jawabanBenar=" + jawabanBenar +
                ", rataWaktu=" + rataWaktu +
                '}';
    }
}