package com.khozin.pembelajarankaidah.database.entity;

import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

/**
 * Result class for materi yang belum dimulai
 */
public class MateriBelumDimulai {
    @ColumnInfo(name = "id_materi")
    public int idMateri;

    @ColumnInfo(name = "judul_kaidah")
    public String judulKaidah;

    @ColumnInfo(name = "urutan")
    public int urutan;

    public MateriBelumDimulai() {}

    @NonNull
    @Override
    public String toString() {
        return "MateriBelumDimulai{" +
                "idMateri=" + idMateri +
                ", judulKaidah='" + judulKaidah + '\'' +
                ", urutan=" + urutan +
                '}';
    }
}