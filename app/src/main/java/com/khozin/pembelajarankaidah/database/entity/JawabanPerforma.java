package com.khozin.pembelajarankaidah.database.entity;

import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

/**
 * Result class for Jawaban performance
 */
public class JawabanPerforma {
    @ColumnInfo(name = "id_pilihan")
    public int idPilihan;

    @ColumnInfo(name = "id_soal")
    public int idSoal;

    @ColumnInfo(name = "jawaban")
    public String jawaban;

    @ColumnInfo(name = "is_benar")
    public boolean isBenar;

    @ColumnInfo(name = "urutan")
    public int urutan;

    @ColumnInfo(name = "huruf_pilihan")
    public char hurufPilihan;

    @ColumnInfo(name = "is_selected")
    public boolean isSelected;

    @ColumnInfo(name = "total_dipilih")
    public int totalDipilih;

    @ColumnInfo(name = "total_benar")
    public int totalBenar;

    @ColumnInfo(name = "benar_rate")
    public double benarRate;

    public JawabanPerforma() {}

    @NonNull
    @Override
    public String toString() {
        return "JawabanPerforma{" +
                "idPilihan=" + idPilihan +
                ", jawaban='" + jawaban + '\'' +
                ", benarRate=" + benarRate +
                '}';
    }
}