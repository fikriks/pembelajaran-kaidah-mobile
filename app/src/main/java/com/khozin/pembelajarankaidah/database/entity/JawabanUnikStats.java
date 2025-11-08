package com.khozin.pembelajarankaidah.database.entity;

import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

/**
 * Result class for Jawaban unique stats
 */
public class JawabanUnikStats {
    @ColumnInfo(name = "unique_soal")
    public int uniqueSoal;

    @ColumnInfo(name = "total_jawaban")
    public int totalJawaban;

    public JawabanUnikStats() {}

    @NonNull
    @Override
    public String toString() {
        return "JawabanUnikStats{" +
                "uniqueSoal=" + uniqueSoal +
                ", totalJawaban=" + totalJawaban +
                '}';
    }
}