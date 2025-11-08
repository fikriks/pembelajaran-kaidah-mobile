package com.khozin.pembelajarankaidah.database.entity;

import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

/**
 * Result class for Soal with jawaban count
 */
public class SoalWithJawabanCount {
    @ColumnInfo(name = "id_soal")
    public int idSoal;

    @ColumnInfo(name = "id_materi")
    public int idMateri;

    @ColumnInfo(name = "pertanyaan")
    public String pertanyaan;

    @ColumnInfo(name = "tipe_soal")
    public String tipeSoal;

    @ColumnInfo(name = "tingkat_kesulitan")
    public String tingkatKesulitan;

    @ColumnInfo(name = "poin")
    public int poin;

    @ColumnInfo(name = "dibuat_oleh")
    public int dibuatOleh;

    @ColumnInfo(name = "waktu_dibuat")
    public String waktuDibuat;

    @ColumnInfo(name = "waktu_diubah")
    public String waktuDiubah;

    @ColumnInfo(name = "jawaban_count")
    public int jawabanCount;

    public SoalWithJawabanCount() {}

    @NonNull
    @Override
    public String toString() {
        return "SoalWithJawabanCount{" +
                "idSoal=" + idSoal +
                ", pertanyaan='" + pertanyaan + '\'' +
                ", jawabanCount=" + jawabanCount +
                '}';
    }
}