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
 * Entity SesiLatihan untuk tabel sesi_latihan
 * Sesuai database schema di CLAUDE.md
 * Penting untuk LCM algorithm tracking
 */
@Entity(tableName = "sesi_latihan",
        indices = {
            @Index(value = {"id_siswa"}),
            @Index(value = {"id_materi"}),
            @Index(value = {"id_siswa", "id_materi"}),
            @Index(value = {"status"})
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
public class SesiLatihan {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_sesi")
    private int idSesi;

    @ColumnInfo(name = "id_siswa")
    private int idSiswa;

    @ColumnInfo(name = "id_materi")
    private int idMateri;

    @SerializedName("seed_digunakan")
    @ColumnInfo(name = "seed_digunakan")
    private long seedDigunakan;

    @ColumnInfo(name = "total_soal")
    private int totalSoal;

    @ColumnInfo(name = "soal_benar")
    private int soalBenar;

    @ColumnInfo(name = "skor")
    private float skor;

    @SerializedName("waktu_mulai")
    @ColumnInfo(name = "waktu_mulai")
    private String waktuMulai;

    @SerializedName("waktu_selesai")
    @Nullable
    @ColumnInfo(name = "waktu_selesai")
    private String waktuSelesai;

    @ColumnInfo(name = "durasi_detik")
    @Nullable
    private Integer durasiDetik;

    @SerializedName("status")
    @NonNull
    @ColumnInfo(name = "status")
    private String status; // sedang_berjalan, selesai

    @SerializedName("waktu_dibuat")
    @ColumnInfo(name = "waktu_dibuat")
    private String waktuDibuat;

    // Additional fields untuk mobile app
    @ColumnInfo(name = "materi_judul")
    private String materiJudul;

    @ColumnInfo(name = "siswa_nama")
    private String siswaNama;

    @ColumnInfo(name = "current_question_index")
    private int currentQuestionIndex = 0;

    @ColumnInfo(name = "jumlah_soal_dijawab")
    private int jumlahSoalDijawab = 0;

    @ColumnInfo(name = "is_paused")
    private boolean isPaused = false;

    @ColumnInfo(name = "last_pause_time")
    private long lastPauseTime = 0;

    // LCM Algorithm Parameters (sesuai skripsi)
    public static final long LCM_A = 10;  // multiplier
    public static final long LCM_C = 23;  // increment
    public static final long LCM_M = 29;  // modulus

    // Default constructor
    public SesiLatihan() {
        this.totalSoal = 20;
        this.soalBenar = 0;
        this.skor = 0.0f;
        this.status = "sedang_berjalan";
        this.seedDigunakan = System.currentTimeMillis(); // Default seed
    }

    // Constructor untuk membuat sesi baru
    @Ignore
    public SesiLatihan(int idSiswa, int idMateri, int totalSoal, long seed) {
        this();
        this.idSiswa = idSiswa;
        this.idMateri = idMateri;
        this.totalSoal = totalSoal;
        this.seedDigunakan = seed;
        this.waktuMulai = getCurrentTimestamp();
        this.waktuDibuat = this.waktuMulai;
    }

    // Getters and Setters
    public int getIdSesi() {
        return idSesi;
    }

    public void setIdSesi(int idSesi) {
        this.idSesi = idSesi;
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

    public long getSeedDigunakan() {
        return seedDigunakan;
    }

    public void setSeedDigunakan(long seedDigunakan) {
        this.seedDigunakan = seedDigunakan;
    }

    public int getTotalSoal() {
        return totalSoal;
    }

    public void setTotalSoal(int totalSoal) {
        this.totalSoal = totalSoal;
    }

    public int getSoalBenar() {
        return soalBenar;
    }

    public void setSoalBenar(int soalBenar) {
        this.soalBenar = soalBenar;
        this.calculateSkor();
    }

    public float getSkor() {
        return skor;
    }

    public void setSkor(float skor) {
        this.skor = skor;
    }

    public String getWaktuMulai() {
        return waktuMulai;
    }

    public void setWaktuMulai(String waktuMulai) {
        this.waktuMulai = waktuMulai;
    }

    @Nullable
    public String getWaktuSelesai() {
        return waktuSelesai;
    }

    public void setWaktuSelesai(@Nullable String waktuSelesai) {
        this.waktuSelesai = waktuSelesai;
    }

    @Nullable
    public Integer getDurasiDetik() {
        return durasiDetik;
    }

    public void setDurasiDetik(@Nullable Integer durasiDetik) {
        this.durasiDetik = durasiDetik;
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

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(int currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }

    public int getJumlahSoalDijawab() {
        return jumlahSoalDijawab;
    }

    public void setJumlahSoalDijawab(int jumlahSoalDijawab) {
        this.jumlahSoalDijawab = jumlahSoalDijawab;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public long getLastPauseTime() {
        return lastPauseTime;
    }

    public void setLastPauseTime(long lastPauseTime) {
        this.lastPauseTime = lastPauseTime;
    }

    /**
     * Generate seed untuk LCM algorithm
     * Formula: timestamp + user_id + materi_id
     */
    public static long generateSeed(int userId, int materiId) {
        long timestamp = System.currentTimeMillis();
        return timestamp + userId + materiId;
    }

    /**
     * Calculate skor berdasarkan jawaban benar
     */
    private void calculateSkor() {
        if (totalSoal > 0) {
            this.skor = ((float) soalBenar / totalSoal) * 100;
        } else {
            this.skor = 0.0f;
        }
    }

    /**
     * Increment jawaban benar dan update skor
     */
    public void incrementSoalBenar() {
        this.soalBenar++;
        this.jumlahSoalDijawab++;
        this.calculateSkor();
    }

    /**
     * Increment jumlah soal dijawab (untuk jawaban salah)
     */
    public void incrementJumlahDijawab() {
        this.jumlahSoalDijawab++;
    }

    /**
     * Selesaikan sesi latihan
     */
    public void selesaikanSesi() {
        this.status = "selesai";
        this.waktuSelesai = getCurrentTimestamp();
        this.calculateDurasi();
        this.calculateSkor();
    }

    /**
     * Calculate durasi pengerjaan
     */
    private void calculateDurasi() {
        if (waktuMulai != null && waktuSelesai != null) {
            try {
                long start = Long.parseLong(waktuMulai);
                long end = Long.parseLong(waktuSelesai);
                this.durasiDetik = (int) ((end - start) / 1000);
            } catch (NumberFormatException e) {
                // Fallback to current duration calculation
                this.durasiDetik = 0;
            }
        }
    }

    /**
     * Get current timestamp
     */
    private String getCurrentTimestamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * Get progress percentage
     */
    public int getProgressPercentage() {
        if (totalSoal > 0) {
            return (jumlahSoalDijawab * 100) / totalSoal;
        }
        return 0;
    }

    /**
     * Check apakah sesi sudah selesai
     */
    public boolean isSelesai() {
        return "selesai".equals(status);
    }

    /**
     * Check apakah semua soal sudah dijawab
     */
    public boolean isAllQuestionsAnswered() {
        return jumlahSoalDijawab >= totalSoal;
    }

    /**
     * Get remaining questions
     */
    public int getRemainingQuestions() {
        return Math.max(0, totalSoal - jumlahSoalDijawab);
    }

    /**
     * Get formatted duration (MM:SS)
     */
    public String getFormattedDuration() {
        if (durasiDetik != null && durasiDetik > 0) {
            int minutes = durasiDetik / 60;
            int seconds = durasiDetik % 60;
            return String.format("%02d:%02d", minutes, seconds);
        }
        return "00:00";
    }

    /**
     * Get grade based on score
     */
    public String getGrade() {
        if (skor >= 90) return "A";
        if (skor >= 80) return "B";
        if (skor >= 70) return "C";
        if (skor >= 60) return "D";
        return "E";
    }

    /**
     * Get status text untuk display
     */
    public String getStatusText() {
        switch (status) {
            case "sedang_berjalan":
                return "Sedang Berjalan";
            case "selesai":
                return "Selesai";
            default:
                return status;
        }
    }

    @Override
    public String toString() {
        return "SesiLatihan{" +
                "idSesi=" + idSesi +
                ", idSiswa=" + idSiswa +
                ", idMateri=" + idMateri +
                ", seedDigunakan=" + seedDigunakan +
                ", totalSoal=" + totalSoal +
                ", soalBenar=" + soalBenar +
                ", skor=" + skor +
                ", status='" + status + '\'' +
                ", durasiDetik=" + durasiDetik +
                '}';
    }
}