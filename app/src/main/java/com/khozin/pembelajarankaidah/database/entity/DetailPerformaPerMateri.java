package com.khozin.pembelajarankaidah.database.entity;

import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

/**
 * Result class for DetailJawabanSiswa performance per materi
 */
public class DetailPerformaPerMateri {
    @ColumnInfo(name = "id_materi")
    public int idMateri;

    @ColumnInfo(name = "judul_kaidah")
    public String judulKaidah;

    @ColumnInfo(name = "total_jawaban")
    public int totalJawaban;

    @ColumnInfo(name = "jawaban_benar")
    public int jawabanBenar;

    @ColumnInfo(name = "benar_rate")
    public double benarRate;

    public DetailPerformaPerMateri() {}

    @NonNull
    @Override
    public String toString() {
        return "DetailPerformaPerMateri{" +
                "idMateri=" + idMateri +
                ", judulKaidah='" + judulKaidah + '\'' +
                ", totalJawaban=" + totalJawaban +
                ", benarRate=" + benarRate +
                '}';
    }
}