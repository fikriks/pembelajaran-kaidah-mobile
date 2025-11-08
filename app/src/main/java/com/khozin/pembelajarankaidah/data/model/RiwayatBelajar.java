package com.khozin.pembelajarankaidah.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.annotations.SerializedName;

/**
 * Entity RiwayatBelajar untuk tabel riwayat_belajar
 * Sesuai database schema di CLAUDE.md
 * Tracking progress pembelajaran siswa per materi
 */
@Entity(tableName = "riwayat_belajar",
        indices = {
            @Index(value = {"id_siswa"}),
            @Index(value = {"id_materi"}),
            @Index(value = {"id_siswa", "id_materi"}),
            @Index(value = {"status"}),
            @Index(value = {"waktu_diubah"})
        },
        foreignKeys = {
            @ForeignKey(entity = Siswa.class,
                    parentColumns = "id",
                    childColumns = "id_siswa",
                    onDelete = ForeignKey.CASCADE),
            @ForeignKey(entity = MateriKaidah.class,
                    parentColumns = "id_materi",
                    childColumns = "id_materi",
                    onDelete = ForeignKey.CASCADE)
        })
public class RiwayatBelajar {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_riwayat")
    private int idRiwayat;

    @ColumnInfo(name = "id_siswa")
    private int idSiswa;

    @ColumnInfo(name = "id_materi")
    private int idMateri;

    @SerializedName("status")
    @NonNull
    @ColumnInfo(name = "status")
    private String status; // belum_dimulai, sedang_belajar, selesai

    @ColumnInfo(name = "persentase_penguasaan")
    private float persentasePenguasaan;

    @SerializedName("waktu_akses_terakhir")
    @Nullable
    @ColumnInfo(name = "waktu_akses_terakhir")
    private String waktuAksesTerakhir;

    @SerializedName("waktu_dibuat")
    @ColumnInfo(name = "waktu_dibuat")
    private String waktuDibuat;

    @SerializedName("waktu_diubah")
    @ColumnInfo(name = "waktu_diubah")
    private String waktuDiubah;

    // Additional fields untuk mobile app
    @ColumnInfo(name = "materi_judul")
    private String materiJudul;

    @ColumnInfo(name = "siswa_nama")
    private String siswaNama;

    @ColumnInfo(name = "total_sesi_diikuti")
    private int totalSesiDiikuti = 0;

    @ColumnInfo(name = "rata_rata_skor")
    private float rataRataSkor = 0.0f;

    @ColumnInfo(name = "total_waktu_belajar_menit")
    private int totalWaktuBelajarMenit = 0;

    @ColumnInfo(name = "streak_hari")
    private int streakHari = 0;

    @ColumnInfo(name = "tanggal_selesai_terakhir")
    private String tanggalSelesaiTerakhir;

    // Status constants
    public static final String STATUS_BELUM_DIMULAI = "belum_dimulai";
    public static final String STATUS_SEDANG_BELAJAR = "sedang_belajar";
    public static final String STATUS_SELESAI = "selesai";

    // Default constructor
    public RiwayatBelajar() {
        this.status = STATUS_BELUM_DIMULAI;
        this.persentasePenguasaan = 0.0f;
        this.totalSesiDiikuti = 0;
        this.rataRataSkor = 0.0f;
        this.totalWaktuBelajarMenit = 0;
        this.streakHari = 0;
        this.waktuDibuat = getCurrentTimestamp();
    }

    // Constructor untuk membuat riwayat baru
    @Ignore
    public RiwayatBelajar(int idSiswa, int idMateri) {
        this();
        this.idSiswa = idSiswa;
        this.idMateri = idMateri;
    }

    // Getters and Setters
    public int getIdRiwayat() {
        return idRiwayat;
    }

    public void setIdRiwayat(int idRiwayat) {
        this.idRiwayat = idRiwayat;
    }

    public int getIdSiswa() {
        return idSiswa;
    }

    public void setIdSiswa(int idSiswa) {
        this.idSiswa = idSiswa;
    }

    public int getIdMateri() {
        return idMateri;
    }

    public void setIdMateri(int idMateri) {
        this.idMateri = idMateri;
    }

    @NonNull
    public String getStatus() {
        return status;
    }

    public void setStatus(@NonNull String status) {
        this.status = status;
        this.waktuDiubah = getCurrentTimestamp();
    }

    public float getPersentasePenguasaan() {
        return persentasePenguasaan;
    }

    public void setPersentasePenguasaan(float persentasePenguasaan) {
        this.persentasePenguasaan = persentasePenguasaan;
        this.updateStatusFromProgress();
    }

    @Nullable
    public String getWaktuAksesTerakhir() {
        return waktuAksesTerakhir;
    }

    public void setWaktuAksesTerakhir(@Nullable String waktuAksesTerakhir) {
        this.waktuAksesTerakhir = waktuAksesTerakhir;
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

    @Nullable
    public String getMateriJudul() {
        return materiJudul;
    }

    public void setMateriJudul(@Nullable String materiJudul) {
        this.materiJudul = materiJudul;
    }

    @Nullable
    public String getSiswaNama() {
        return siswaNama;
    }

    public void setSiswaNama(@Nullable String siswaNama) {
        this.siswaNama = siswaNama;
    }

    public int getTotalSesiDiikuti() {
        return totalSesiDiikuti;
    }

    public void setTotalSesiDiikuti(int totalSesiDiikuti) {
        this.totalSesiDiikuti = totalSesiDiikuti;
    }

    public float getRataRataSkor() {
        return rataRataSkor;
    }

    public void setRataRataSkor(float rataRataSkor) {
        this.rataRataSkor = rataRataSkor;
    }

    public int getTotalWaktuBelajarMenit() {
        return totalWaktuBelajarMenit;
    }

    public void setTotalWaktuBelajarMenit(int totalWaktuBelajarMenit) {
        this.totalWaktuBelajarMenit = totalWaktuBelajarMenit;
    }

    public int getStreakHari() {
        return streakHari;
    }

    public void setStreakHari(int streakHari) {
        this.streakHari = streakHari;
    }

    @Nullable
    public String getTanggalSelesaiTerakhir() {
        return tanggalSelesaiTerakhir;
    }

    public void setTanggalSelesaiTerakhir(@Nullable String tanggalSelesaiTerakhir) {
        this.tanggalSelesaiTerakhir = tanggalSelesaiTerakhir;
    }

    /**
     * Get current timestamp
     */
    private String getCurrentTimestamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * Update status based on progress percentage
     */
    private void updateStatusFromProgress() {
        if (persentasePenguasaan <= 0) {
            this.status = STATUS_BELUM_DIMULAI;
        } else if (persentasePenguasaan < 100) {
            this.status = STATUS_SEDANG_BELAJAR;
        } else {
            this.status = STATUS_SELESAI;
        }
    }

    /**
     * Update akses terakhir
     */
    public void updateAksesTerakhir() {
        this.waktuAksesTerakhir = getCurrentTimestamp();
        this.waktuDiubah = getCurrentTimestamp();
    }

    /**
     * Increment progress
     */
    public void incrementProgress(float increment) {
        this.persentasePenguasaan = Math.min(100.0f, this.persentasePenguasaan + increment);
        this.updateStatusFromProgress();
        this.updateAksesTerakhir();
    }

    /**
     * Tambah sesi dan update rata-rata skor
     */
    public void tambahSesi(float skorBaru, int durasiMenit) {
        this.totalSesiDiikuti++;
        this.totalWaktuBelajarMenit += durasiMenit;

        // Update rata-rata skor
        float totalSkor = this.rataRataSkor * (this.totalSesiDiikuti - 1) + skorBaru;
        this.rataRataSkor = totalSkor / this.totalSesiDiikuti;

        this.updateAksesTerakhir();
    }

    /**
     * Get status text untuk display
     */
    public String getStatusText() {
        switch (status) {
            case STATUS_BELUM_DIMULAI:
                return "Belum Dimulai";
            case STATUS_SEDANG_BELAJAR:
                return "Sedang Belajar";
            case STATUS_SELESAI:
                return "Selesai";
            default:
                return status;
        }
    }

    /**
     * Get warna untuk status
     */
    public int getStatusColor() {
        switch (status) {
            case STATUS_BELUM_DIMULAI:
                return android.graphics.Color.parseColor("#9E9E9E"); // Grey
            case STATUS_SEDANG_BELAJAR:
                return android.graphics.Color.parseColor("#2196F3"); // Blue
            case STATUS_SELESAI:
                return android.graphics.Color.parseColor("#4CAF50"); // Green
            default:
                return android.graphics.Color.parseColor("#9E9E9E"); // Grey
        }
    }

    /**
     * Get formatted waktu belajar
     */
    public String getFormattedWaktuBelajar() {
        if (totalWaktuBelajarMenit >= 60) {
            int jam = totalWaktuBelajarMenit / 60;
            int menit = totalWaktuBelajarMenit % 60;
            return String.format("%d jam %d menit", jam, menit);
        } else {
            return String.format("%d menit", totalWaktuBelajarMenit);
        }
    }

    /**
     * Get progress grade
     */
    public String getProgressGrade() {
        if (persentasePenguasaan >= 90) return "A";
        if (persentasePenguasaan >= 80) return "B";
        if (persentasePenguasaan >= 70) return "C";
        if (persentasePenguasaan >= 60) return "D";
        return "E";
    }

    /**
     * Check apakah sedang aktif belajar
     */
    public boolean isSedangBelajar() {
        return STATUS_SEDANG_BELAJAR.equals(status);
    }

    /**
     * Check apakah sudah selesai
     */
    public boolean isSelesai() {
        return STATUS_SELESAI.equals(status);
    }

    /**
     * Check apakah belum dimulai
     */
    public boolean isBelumDimulai() {
        return STATUS_BELUM_DIMULAI.equals(status);
    }

    /**
     * Check apakah perlu review (skor < 70%)
     */
    public boolean perluReview() {
        return rataRataSkor < 70.0f && totalSesiDiikuti > 0;
    }

    /**
     * Get recommendation text
     */
    public String getRekomendasiText() {
        if (isBelumDimulai()) {
            return "Mulai belajar materi ini";
        } else if (isSedangBelajar()) {
            return "Lanjutkan belajar untuk menyelesaikan";
        } else if (perluReview()) {
            return "Review kembali materi ini (skor < 70%)";
        } else {
            return "Materi sudah dikuasai dengan baik";
        }
    }

    @Override
    public String toString() {
        return "RiwayatBelajar{" +
                "idRiwayat=" + idRiwayat +
                ", idSiswa=" + idSiswa +
                ", idMateri=" + idMateri +
                ", status='" + status + '\'' +
                ", persentasePenguasaan=" + persentasePenguasaan +
                ", totalSesiDiikuti=" + totalSesiDiikuti +
                ", rataRataSkor=" + rataRataSkor +
                '}';
    }
}