package com.khozin.pembelajarankaidah.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.annotations.SerializedName;

/**
 * Entity Jawaban untuk tabel jawaban
 * Sesuai database schema di CLAUDE.md
 */
@Entity(tableName = "jawaban",
        foreignKeys = @ForeignKey(entity = Soal.class,
                parentColumns = "id_soal",
                childColumns = "id_soal",
                onDelete = ForeignKey.CASCADE))
public class Jawaban {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_pilihan")
    private int idPilihan;

    @ColumnInfo(name = "id_soal")
    private int idSoal;

    @SerializedName("jawaban")
    @NonNull
    @ColumnInfo(name = "jawaban")
    private String jawabanText;

    @ColumnInfo(name = "is_benar")
    private boolean isBenar;

    @ColumnInfo(name = "urutan")
    private int urutan;

    // Additional fields untuk mobile app
    private char hurufPilihan; // A, B, C, D
    private boolean isSelected = false;

    // Default constructor
    public Jawaban() {
        this.isBenar = false;
        this.urutan = 0;
        this.isSelected = false;
    }

    // Constructor minimal
    public Jawaban(int idSoal, String jawabanText, boolean isBenar, int urutan) {
        this.idSoal = idSoal;
        this.jawabanText = jawabanText;
        this.isBenar = isBenar;
        this.urutan = urutan;
        this.hurufPilihan = getHurufDariUrutan(urutan);
    }

    // Getters and Setters
    public int getIdPilihan() {
        return idPilihan;
    }

    public void setIdPilihan(int idPilihan) {
        this.idPilihan = idPilihan;
    }

    public int getIdSoal() {
        return idSoal;
    }

    public void setIdSoal(int idSoal) {
        this.idSoal = idSoal;
    }

    @NonNull
    public String getJawabanText() {
        return jawabanText;
    }

    public void setJawabanText(@NonNull String jawabanText) {
        this.jawabanText = jawabanText;
    }

    public boolean isBenar() {
        return isBenar;
    }

    public void setBenar(boolean benar) {
        isBenar = benar;
    }

    public int getUrutan() {
        return urutan;
    }

    public void setUrutan(int urutan) {
        this.urutan = urutan;
        this.hurufPilihan = getHurufDariUrutan(urutan);
    }

    public char getHurufPilihan() {
        return hurufPilihan;
    }

    public void setHurufPilihan(char hurufPilihan) {
        this.hurufPilihan = hurufPilihan;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    /**
     * Konversi urutan menjadi huruf pilihan (A, B, C, D, dst)
     */
    private char getHurufDariUrutan(int urutan) {
        return (char) ('A' + urutan);
    }

    /**
     * Mendapatkan format pilihan untuk display (A. Jawaban)
     */
    public String getFormatPilihan() {
        return hurufPilihan + ". " + jawabanText;
    }

    /**
     * Mendapatkan text jawaban untuk display (clean dari special characters)
     */
    public String getJawabanDisplay() {
        if (jawabanText == null) return "";
        // Clean dari HTML tags atau special characters jika ada
        return jawabanText.trim();
    }

    /**
     * Check apakah jawaban ini mengandung teks Arab
     */
    public boolean isArabicText() {
        if (jawabanText == null) return false;
        // Check untuk Arabic Unicode range
        for (int i = 0; i < jawabanText.length(); i++) {
            char c = jawabanText.charAt(i);
            if (c >= 0x0600 && c <= 0x06FF) {
                return true;
            }
        }
        return false;
    }

    /**
     * Mendapatkan status jawaban untuk display
     */
    public String getStatusJawaban() {
        if (isSelected) {
            return isBenar ? "Benar ✅" : "Salah ❌";
        } else {
            return isBenar ? "Kunci Jawaban (🔑)" : "";
        }
    }

    /**
     * Mendapatkan warna background untuk selection
     */
    public int getSelectionColor() {
        if (!isSelected) {
            return android.graphics.Color.parseColor("#F5F5F5"); // Light grey
        } else if (isBenar) {
            return android.graphics.Color.parseColor("#C8E6C9"); // Light green
        } else {
            return android.graphics.Color.parseColor("#FFCDD2"); // Light red
        }
    }

    /**
     * Mendapatkan warna text untuk selection
     */
    public int getTextColor() {
        if (!isSelected) {
            return android.graphics.Color.parseColor("#212121"); // Black
        } else if (isBenar) {
            return android.graphics.Color.parseColor("#2E7D32"); // Dark green
        } else {
            return android.graphics.Color.parseColor("#C62828"); // Dark red
        }
    }

    /**
     * Toggle selection state
     */
    public void toggleSelection() {
        isSelected = !isSelected;
    }

    /**
     * Reset selection state
     */
    public void resetSelection() {
        isSelected = false;
    }

    @Override
    public String toString() {
        return "Jawaban{" +
                "idPilihan=" + idPilihan +
                ", idSoal=" + idSoal +
                ", jawabanText='" + jawabanText + '\'' +
                ", isBenar=" + isBenar +
                ", hurufPilihan=" + hurufPilihan +
                ", isSelected=" + isSelected +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Jawaban jawaban = (Jawaban) obj;
        return idPilihan == jawaban.idPilihan &&
                idSoal == jawaban.idSoal;
    }

    @Override
    public int hashCode() {
        int result = idPilihan;
        result = 31 * result + idSoal;
        return result;
    }
}