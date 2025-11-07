package com.khozin.pembelajarankaidah.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;

/**
 * Entity Siswa untuk tabel siswa
 * Sesuai database schema di CLAUDE.md
 */
@Entity(tableName = "siswa")
public class Siswa {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("nis")
    @NonNull
    private String nis;

    @SerializedName("nama_lengkap")
    @NonNull
    private String namaLengkap;

    @SerializedName("kata_sandi")
    @NonNull
    private String kataSandi;

    @SerializedName("jenis_kelamin")
    @NonNull
    private String jenisKelamin; // L atau P

    @SerializedName("kelas")
    @NonNull
    private String kelas;

    @SerializedName("status")
    @NonNull
    private String status; // AKTIF atau NONAKTIF

    @SerializedName("waktu_dibuat")
    private String waktuDibuat;

    @SerializedName("waktu_diubah")
    private String waktuDiubah;

    // Default constructor
    public Siswa() {}

    // Constructor untuk login
    public Siswa(String nis, String namaLengkap, String kataSandi) {
        this.nis = nis;
        this.namaLengkap = namaLengkap;
        this.kataSandi = kataSandi;
        this.jenisKelamin = "";
        this.kelas = "";
        this.status = "AKTIF";
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getNis() {
        return nis;
    }

    public void setNis(@NonNull String nis) {
        this.nis = nis;
    }

    @NonNull
    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(@NonNull String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    @NonNull
    public String getKataSandi() {
        return kataSandi;
    }

    public void setKataSandi(@NonNull String kataSandi) {
        this.kataSandi = kataSandi;
    }

    @NonNull
    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(@NonNull String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    @NonNull
    public String getKelas() {
        return kelas;
    }

    public void setKelas(@NonNull String kelas) {
        this.kelas = kelas;
    }

    @NonNull
    public String getStatus() {
        return status;
    }

    public void setStatus(@NonNull String status) {
        this.status = status;
    }

    public String getWaktuDibuat() {
        return waktuDibuat;
    }

    public void setWaktuDibuat(String waktuDibuat) {
        this.waktuDibuat = waktuDibuat;
    }

    public String getWaktuDiubah() {
        return waktuDiubah;
    }

    public void setWaktuDiubah(String waktuDiubah) {
        this.waktuDiubah = waktuDiubah;
    }

    /**
     * Mendapatkan nama panggilan untuk display
     */
    public String getNamaPanggilan() {
        if (namaLengkap == null || namaLengkap.isEmpty()) {
            return "";
        }
        String[] namaArray = namaLengkap.split(" ");
        return namaArray[0];
    }

    /**
     * Mendapatkan jenis kelamin dalam format lengkap
     */
    public String getJenisKelaminLengkap() {
        if ("L".equals(jenisKelamin)) {
            return "Laki-laki";
        } else if ("P".equals(jenisKelamin)) {
            return "Perempuan";
        }
        return jenisKelamin;
    }

    /**
     * Check apakah siswa aktif
     */
    public boolean isAktif() {
        return "AKTIF".equals(status);
    }

    @Override
    public String toString() {
        return "Siswa{" +
                "id=" + id +
                ", nis='" + nis + '\'' +
                ", namaLengkap='" + namaLengkap + '\'' +
                ", kelas='" + kelas + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}