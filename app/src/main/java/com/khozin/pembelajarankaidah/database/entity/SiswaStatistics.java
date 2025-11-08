package com.khozin.pembelajarankaidah.database.entity;

import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

/**
 * Result class for Siswa statistics query
 */
public class SiswaStatistics {
    @ColumnInfo(name = "total")
    public int total;

    @ColumnInfo(name = "aktif")
    public int aktif;

    @ColumnInfo(name = "nonaktif")
    public int nonaktif;

    @ColumnInfo(name = "laki_laki")
    public int lakiLaki;

    @ColumnInfo(name = "perempuan")
    public int perempuan;

    public SiswaStatistics(int total, int aktif, int nonaktif, int lakiLaki, int perempuan) {
        this.total = total;
        this.aktif = aktif;
        this.nonaktif = nonaktif;
        this.lakiLaki = lakiLaki;
        this.perempuan = perempuan;
    }

    @NonNull
    @Override
    public String toString() {
        return "SiswaStatistics{" +
                "total=" + total +
                ", aktif=" + aktif +
                ", nonaktif=" + nonaktif +
                ", lakiLaki=" + lakiLaki +
                ", perempuan=" + perempuan +
                '}';
    }
}