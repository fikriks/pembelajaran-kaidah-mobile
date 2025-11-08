package com.khozin.pembelajarankaidah.database.entity;

import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

/**
 * Result class for Soal performance per tingkat kesulitan
 */
public class SoalPerformaPerTingkat {
    @ColumnInfo(name = "tingkat_kesulitan")
    public String tingkatKesulitan;

    @ColumnInfo(name = "total_soal")
    public int totalSoal;

    @ColumnInfo(name = "total_jawaban")
    public int totalJawaban;

    @ColumnInfo(name = "jawaban_benar")
    public int jawabanBenar;

    @ColumnInfo(name = "benar_rate")
    public double benarRate;

    public SoalPerformaPerTingkat() {}

    @NonNull
    @Override
    public String toString() {
        return "SoalPerformaPerTingkat{" +
                "tingkatKesulitan='" + tingkatKesulitan + '\'' +
                ", totalSoal=" + totalSoal +
                ", benarRate=" + benarRate +
                '}';
    }
}