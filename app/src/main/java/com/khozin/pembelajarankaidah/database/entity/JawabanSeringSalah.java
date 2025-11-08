package com.khozin.pembelajarankaidah.database.entity;

import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

/**
 * Result class for jawaban yang sering salah
 */
public class JawabanSeringSalah {
    @ColumnInfo(name = "jawaban")
    public String jawaban;

    @ColumnInfo(name = "salah_count")
    public int salahCount;

    public JawabanSeringSalah() {}

    @NonNull
    @Override
    public String toString() {
        return "JawabanSeringSalah{" +
                "jawaban='" + jawaban + '\'' +
                ", salahCount=" + salahCount +
                '}';
    }
}