package com.khozin.pembelajarankaidah.database.entity;

import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

/**
 * Result class for jawaban unik stats
 */
public class DetailJawabanUnikStats {
    @ColumnInfo(name = "unique_soal")
    public int uniqueSoal;

    @ColumnInfo(name = "total_jawaban")
    public int totalJawaban;

    public DetailJawabanUnikStats() {}

    @NonNull
    @Override
    public String toString() {
        return "DetailJawabanUnikStats{" +
                "uniqueSoal=" + uniqueSoal +
                ", totalJawaban=" + totalJawaban +
                '}';
    }
}