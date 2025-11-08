package com.khozin.pembelajarankaidah.database.entity;

import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

/**
 * Result class for SesiLatihan with materi info
 */
public class SesiWithMateriInfo {
    @ColumnInfo(name = "id_sesi")
    public int idSesi;

    @ColumnInfo(name = "id_siswa")
    public int idSiswa;

    @ColumnInfo(name = "id_materi")
    public int idMateri;

    @ColumnInfo(name = "seed_digunakan")
    public long seedDigunakan;

    @ColumnInfo(name = "total_soal")
    public int totalSoal;

    @ColumnInfo(name = "soal_benar")
    public int soalBenar;

    @ColumnInfo(name = "skor")
    public float skor;

    @ColumnInfo(name = "waktu_mulai")
    public String waktuMulai;

    @ColumnInfo(name = "waktu_selesai")
    public String waktuSelesai;

    @ColumnInfo(name = "durasi_detik")
    public Integer durasiDetik;

    @ColumnInfo(name = "status")
    public String status;

    @ColumnInfo(name = "waktu_dibuat")
    public String waktuDibuat;

    @ColumnInfo(name = "materi_judul")
    public String materiJudul;

    public SesiWithMateriInfo() {}

    @NonNull
    @Override
    public String toString() {
        return "SesiWithMateriInfo{" +
                "idSesi=" + idSesi +
                ", materiJudul='" + materiJudul + '\'' +
                ", skor=" + skor +
                '}';
    }
}