package com.khozin.pembelajarankaidah.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.annotations.SerializedName;

/**
 * Entity DetailJawabanSiswa untuk tabel detail_jawaban_siswa
 * Sesuai database schema di CLAUDE.md
 * Tracking detail jawaban siswa per sesi
 */
@Entity(tableName = "detail_jawaban_siswa",
        foreignKeys = {
            @ForeignKey(entity = SesiLatihan.class,
                    parentColumns = "id_sesi",
                    childColumns = "id_sesi",
                    onDelete = ForeignKey.CASCADE),
            @ForeignKey(entity = Soal.class,
                    parentColumns = "id_soal",
                    childColumns = "id_soal",
                    onDelete = ForeignKey.CASCADE),
            @ForeignKey(entity = Jawaban.class,
                    parentColumns = "id_pilihan",
                    childColumns = "id_pilihan",
                    onDelete = ForeignKey.SET_NULL)
        })
public class DetailJawabanSiswa {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_detail")
    private int idDetail;

    @ColumnInfo(name = "id_sesi")
    private int idSesi;

    @ColumnInfo(name = "id_soal")
    private int idSoal;

    @ColumnInfo(name = "id_pilihan")
    @Nullable
    private Integer idPilihan;

    @ColumnInfo(name = "urutan_soal")
    private int urutanSoal;

    @ColumnInfo(name = "is_benar")
    private boolean isBenar;

    @SerializedName("waktu_jawab")
    @ColumnInfo(name = "waktu_jawab")
    private String waktuJawab;

    // Additional fields untuk mobile app
    private String pertanyaanText;
    private String jawabanText;
    private String jawabanBenarText;
    private int waktuResponsDetik = 0;
    private boolean isReviewed = false;

    // Default constructor
    public DetailJawabanSiswa() {
        this.isBenar = false;
        this.urutanSoal = 0;
        this.waktuResponsDetik = 0;
        this.isReviewed = false;
    }

    // Constructor untuk membuat detail jawaban baru
    public DetailJawabanSiswa(int idSesi, int idSoal, Integer idPilihan, int urutanSoal, boolean isBenar) {
        this();
        this.idSesi = idSesi;
        this.idSoal = idSoal;
        this.idPilihan = idPilihan;
        this.urutanSoal = urutanSoal;
        this.isBenar = isBenar;
        this.waktuJawab = getCurrentTimestamp();
    }

    // Getters and Setters
    public int getIdDetail() {
        return idDetail;
    }

    public void setIdDetail(int idDetail) {
        this.idDetail = idDetail;
    }

    public int getIdSesi() {
        return idSesi;
    }

    public void setIdSesi(int idSesi) {
        this.idSesi = idSesi;
    }

    public int getIdSoal() {
        return idSoal;
    }

    public void setIdSoal(int idSoal) {
        this.idSoal = idSoal;
    }

    @Nullable
    public Integer getIdPilihan() {
        return idPilihan;
    }

    public void setIdPilihan(@Nullable Integer idPilihan) {
        this.idPilihan = idPilihan;
    }

    public int getUrutanSoal() {
        return urutanSoal;
    }

    public void setUrutanSoal(int urutanSoal) {
        this.urutanSoal = urutanSoal;
    }

    public boolean isBenar() {
        return isBenar;
    }

    public void setBenar(boolean benar) {
        isBenar = benar;
    }

    public String getWaktuJawab() {
        return waktuJawab;
    }

    public void setWaktuJawab(String waktuJawab) {
        this.waktuJawab = waktuJawab;
    }

    @Nullable
    public String getPertanyaanText() {
        return pertanyaanText;
    }

    public void setPertanyaanText(@Nullable String pertanyaanText) {
        this.pertanyaanText = pertanyaanText;
    }

    @Nullable
    public String getJawabanText() {
        return jawabanText;
    }

    public void setJawabanText(@Nullable String jawabanText) {
        this.jawabanText = jawabanText;
    }

    @Nullable
    public String getJawabanBenarText() {
        return jawabanBenarText;
    }

    public void setJawabanBenarText(@Nullable String jawabanBenarText) {
        this.jawabanBenarText = jawabanBenarText;
    }

    public int getWaktuResponsDetik() {
        return waktuResponsDetik;
    }

    public void setWaktuResponsDetik(int waktuResponsDetik) {
        this.waktuResponsDetik = waktuResponsDetik;
    }

    public boolean isReviewed() {
        return isReviewed;
    }

    public void setReviewed(boolean reviewed) {
        isReviewed = reviewed;
    }

    /**
     * Get current timestamp
     */
    private String getCurrentTimestamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * Get nomor soal untuk display
     */
    public String getNomorSoal() {
        return "Soal " + urutanSoal;
    }

    /**
     * Get status jawaban untuk display
     */
    public String getStatusJawaban() {
        if (isBenar) {
            return "Benar ✅";
        } else {
            return "Salah ❌";
        }
    }

    /**
     * Get warna untuk status
     */
    public int getStatusColor() {
        if (isBenar) {
            return android.graphics.Color.parseColor("#4CAF50"); // Green
        } else {
            return android.graphics.Color.parseColor("#F44336"); // Red
        }
    }

    /**
     * Get formatted response time
     */
    public String getFormattedResponseTime() {
        if (waktuResponsDetik > 0) {
            int minutes = waktuResponsDetik / 60;
            int seconds = waktuResponsDetik % 60;

            if (minutes > 0) {
                return String.format("%d menit %d detik", minutes, seconds);
            } else {
                return String.format("%d detik", seconds);
            }
        }
        return "-";
    }

    /**
     * Check apakah jawaban ini dipilih oleh user (null jika tidak ada jawaban)
     */
    public boolean hasAnswer() {
        return idPilihan != null;
    }

    /**
     * Check apakah ini jawaban yang benar
     */
    public boolean isCorrectAnswer() {
        return isBenar;
    }

    /**
     * Get comparison text untuk review
     */
    public String getComparisonText() {
        StringBuilder sb = new StringBuilder();

        if (jawabanText != null && !jawabanText.isEmpty()) {
            sb.append("Jawaban Anda: ").append(jawabanText);
        } else {
            sb.append("Tidak ada jawaban");
        }

        if (!isBenar && jawabanBenarText != null && !jawabanBenarText.isEmpty()) {
            sb.append("\nJawaban Benar: ").append(jawabanBenarText);
        }

        return sb.toString();
    }

    /**
     * Mark as reviewed
     */
    public void markAsReviewed() {
        this.isReviewed = true;
    }

    /**
     * Calculate response quality based on time
     */
    public String getResponseQuality() {
        if (waktuResponsDetik <= 10) {
            return "Cepat";
        } else if (waktuResponsDetik <= 30) {
            return "Normal";
        } else if (waktuResponsDetik <= 60) {
            return "Lambat";
        } else {
            return "Sangat Lambat";
        }
    }

    /**
     * Get response quality color
     */
    public int getResponseQualityColor() {
        if (waktuResponsDetik <= 10) {
            return android.graphics.Color.parseColor("#4CAF50"); // Green - Cepat
        } else if (waktuResponsDetik <= 30) {
            return android.graphics.Color.parseColor("#2196F3"); // Blue - Normal
        } else if (waktuResponsDetik <= 60) {
            return android.graphics.Color.parseColor("#FF9800"); // Orange - Lambat
        } else {
            return android.graphics.Color.parseColor("#F44336"); // Red - Sangat Lambat
        }
    }

    @Override
    public String toString() {
        return "DetailJawabanSiswa{" +
                "idDetail=" + idDetail +
                ", idSesi=" + idSesi +
                ", idSoal=" + idSoal +
                ", idPilihan=" + idPilihan +
                ", urutanSoal=" + urutanSoal +
                ", isBenar=" + isBenar +
                ", waktuResponsDetik=" + waktuResponsDetik +
                ", isReviewed=" + isReviewed +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        DetailJawabanSiswa that = (DetailJawabanSiswa) obj;
        return idDetail == that.idDetail;
    }

    @Override
    public int hashCode() {
        return idDetail;
    }
}