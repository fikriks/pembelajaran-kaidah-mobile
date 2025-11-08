package com.khozin.pembelajarankaidah.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Relation;
import androidx.room.Index;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Entity Soal untuk tabel soal
 * Sesuai database schema di CLAUDE.md
 */
@Entity(tableName = "soal",
        indices = {
            @Index(value = {"id_materi"}),
            @Index(value = {"id_materi", "tingkat_kesulitan"})
        },
        foreignKeys = @ForeignKey(entity = MateriKaidah.class,
                parentColumns = "id_materi",
                childColumns = "id_materi",
                onDelete = ForeignKey.CASCADE))
public class Soal {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_soal")
    private int idSoal;

    @ColumnInfo(name = "id_materi")
    private int idMateri;

    @SerializedName("pertanyaan")
    @NonNull
    @ColumnInfo(name = "pertanyaan")
    private String pertanyaan;

    @SerializedName("tipe_soal")
    @NonNull
    @ColumnInfo(name = "tipe_soal")
    private String tipeSoal; // pilihan_ganda

    @SerializedName("tingkat_kesulitan")
    @NonNull
    @ColumnInfo(name = "tingkat_kesulitan")
    private String tingkatKesulitan; // mudah, sedang, sulit

    @ColumnInfo(name = "poin")
    private int poin;

    @ColumnInfo(name = "dibuat_oleh")
    private int dibuatOleh;

    @SerializedName("waktu_dibuat")
    @ColumnInfo(name = "waktu_dibuat")
    private String waktuDibuat;

    @SerializedName("waktu_diubah")
    @ColumnInfo(name = "waktu_diubah")
    private String waktuDiubah;

    // Additional fields untuk mobile app
    @ColumnInfo(name = "urutan_dalam_sesi")
    private int urutanDalamSesi;

    @ColumnInfo(name = "is_answered")
    private boolean isAnswered = false;

    @ColumnInfo(name = "selected_jawaban_id")
    private int selectedJawabanId = -1;

    // Note: pilihanJawaban is handled via separate queries, not stored in database entity
    @Ignore
    private List<Jawaban> pilihanJawaban;

    // Default constructor
    public Soal() {
        this.tipeSoal = "pilihan_ganda";
        this.tingkatKesulitan = "mudah";
        this.poin = 10;
    }

    // Constructor minimal
    @Ignore
    public Soal(int idMateri, String pertanyaan, String tingkatKesulitan, int poin) {
        this.idMateri = idMateri;
        this.pertanyaan = pertanyaan;
        this.tingkatKesulitan = tingkatKesulitan;
        this.poin = poin;
        this.tipeSoal = "pilihan_ganda";
    }

    // Getters and Setters
    public int getIdSoal() {
        return idSoal;
    }

    public void setIdSoal(int idSoal) {
        this.idSoal = idSoal;
    }

    public int getIdMateri() {
        return idMateri;
    }

    public void setIdMateri(int idMateri) {
        this.idMateri = idMateri;
    }

    @NonNull
    public String getPertanyaan() {
        return pertanyaan;
    }

    public void setPertanyaan(@NonNull String pertanyaan) {
        this.pertanyaan = pertanyaan;
    }

    @NonNull
    public String getTipeSoal() {
        return tipeSoal;
    }

    public void setTipeSoal(@NonNull String tipeSoal) {
        this.tipeSoal = tipeSoal;
    }

    @NonNull
    public String getTingkatKesulitan() {
        return tingkatKesulitan;
    }

    public void setTingkatKesulitan(@NonNull String tingkatKesulitan) {
        this.tingkatKesulitan = tingkatKesulitan;
    }

    public int getPoin() {
        return poin;
    }

    public void setPoin(int poin) {
        this.poin = poin;
    }

    public int getDibuatOleh() {
        return dibuatOleh;
    }

    public void setDibuatOleh(int dibuatOleh) {
        this.dibuatOleh = dibuatOleh;
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
    public List<Jawaban> getPilihanJawaban() {
        return pilihanJawaban;
    }

    public void setPilihanJawaban(@Nullable List<Jawaban> pilihanJawaban) {
        this.pilihanJawaban = pilihanJawaban;
    }

    public int getUrutanDalamSesi() {
        return urutanDalamSesi;
    }

    public void setUrutanDalamSesi(int urutanDalamSesi) {
        this.urutanDalamSesi = urutanDalamSesi;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }

    public int getSelectedJawabanId() {
        return selectedJawabanId;
    }

    public void setSelectedJawabanId(int selectedJawabanId) {
        this.selectedJawabanId = selectedJawabanId;
    }

    /**
     * Mendapatkan jawaban yang benar
     */
    @Nullable
    public Jawaban getJawabanBenar() {
        if (pilihanJawaban != null) {
            for (Jawaban jawaban : pilihanJawaban) {
                if (jawaban.isBenar()) {
                    return jawaban;
                }
            }
        }
        return null;
    }

    /**
     * Mendapatkan jawaban yang dipilih oleh user
     */
    @Nullable
    public Jawaban getSelectedJawaban() {
        if (pilihanJawaban != null && selectedJawabanId != -1) {
            for (Jawaban jawaban : pilihanJawaban) {
                if (jawaban.getIdPilihan() == selectedJawabanId) {
                    return jawaban;
                }
            }
        }
        return null;
    }

    /**
     * Check apakah jawaban user benar
     */
    public boolean isUserAnswerCorrect() {
        Jawaban selectedJawaban = getSelectedJawaban();
        Jawaban benarJawaban = getJawabanBenar();
        return selectedJawaban != null && benarJawaban != null &&
                selectedJawaban.getIdPilihan() == benarJawaban.getIdPilihan();
    }

    /**
     * Mendapatkan warna untuk tingkat kesulitan
     */
    public int getTingkatKesulitanColor() {
        switch (tingkatKesulitan.toLowerCase()) {
            case "mudah":
                return android.graphics.Color.parseColor("#4CAF50"); // Green
            case "sedang":
                return android.graphics.Color.parseColor("#FF9800"); // Orange
            case "sulit":
                return android.graphics.Color.parseColor("#F44336"); // Red
            default:
                return android.graphics.Color.parseColor("#9E9E9E"); // Grey
        }
    }

    /**
     * Mendapatkan nomor soal untuk display
     */
    public String getNomorSoal() {
        if (urutanDalamSesi > 0) {
            return "Soal " + urutanDalamSesi;
        }
        return "Soal ?";
    }

    /**
     * Mendapatkan status jawaban
     */
    public String getStatusJawaban() {
        if (!isAnswered) {
            return "Belum Dijawab";
        } else if (isUserAnswerCorrect()) {
            return "Benar ✅";
        } else {
            return "Salah ❌";
        }
    }

    @Override
    public String toString() {
        return "Soal{" +
                "idSoal=" + idSoal +
                ", idMateri=" + idMateri +
                ", pertanyaan='" + pertanyaan + '\'' +
                ", tingkatKesulitan='" + tingkatKesulitan + '\'' +
                ", poin=" + poin +
                ", urutanDalamSesi=" + urutanDalamSesi +
                ", isAnswered=" + isAnswered +
                '}';
    }
}