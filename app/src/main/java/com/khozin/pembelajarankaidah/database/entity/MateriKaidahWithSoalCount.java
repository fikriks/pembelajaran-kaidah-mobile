package com.khozin.pembelajarankaidah.database.entity;

import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

/**
 * Result class for MateriKaidah with soal count
 */
public class MateriKaidahWithSoalCount {
    @ColumnInfo(name = "id_materi")
    public int idMateri;

    @ColumnInfo(name = "judul_kaidah")
    public String judulKaidah;

    @ColumnInfo(name = "deskripsi")
    public String deskripsi;

    @ColumnInfo(name = "penjelasan")
    public String penjelasan;

    @ColumnInfo(name = "contoh")
    public String contoh;

    @ColumnInfo(name = "tingkat_kesulitan")
    public String tingkatKesulitan;

    @ColumnInfo(name = "urutan")
    public int urutan;

    @ColumnInfo(name = "dibuat_oleh")
    public int dibuatOleh;

    @ColumnInfo(name = "waktu_dibuat")
    public String waktuDibuat;

    @ColumnInfo(name = "waktu_diubah")
    public String waktuDiubah;

    @ColumnInfo(name = "soal_count")
    public int soalCount;

    public MateriKaidahWithSoalCount() {}

    @NonNull
    @Override
    public String toString() {
        return "MateriKaidahWithSoalCount{" +
                "idMateri=" + idMateri +
                ", judulKaidah='" + judulKaidah + '\'' +
                ", soalCount=" + soalCount +
                '}';
    }
}