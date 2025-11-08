package com.khozin.pembelajarankaidah.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.annotations.SerializedName;

/**
 * Entity Bab untuk tabel bab
 * Sesuai database schema di CLAUDE.md
 */
@Entity(tableName = "bab")
public class Bab {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_bab")
    private int idBab;

    @SerializedName("nama_bab")
    @NonNull
    @ColumnInfo(name = "nama_bab")
    private String namaBab;

    @SerializedName("deskripsi")
    @Nullable
    @ColumnInfo(name = "deskripsi")
    private String deskripsi;

    @ColumnInfo(name = "urutan")
    private int urutan;

    @SerializedName("is_active")
    @ColumnInfo(name = "is_active")
    private boolean isActive = true;

    @SerializedName("waktu_dibuat")
    @ColumnInfo(name = "waktu_dibuat")
    private String waktuDibuat;

    @SerializedName("waktu_diubah")
    @ColumnInfo(name = "waktu_diubah")
    private String waktuDiubah;

    // Additional fields untuk mobile app - Chapter Progress
    @ColumnInfo(name = "total_materi")
    private int totalMateri = 0;

    @ColumnInfo(name = "completed_materi")
    private int completedMateri = 0;

    @ColumnInfo(name = "in_progress_materi")
    private int inProgressMateri = 0;

    @ColumnInfo(name = "not_started_materi")
    private int notStartedMateri = 0;

    @ColumnInfo(name = "progress_percentage")
    private int progressPercentage = 0;

    @ColumnInfo(name = "is_unlocked")
    private boolean isUnlocked = false;

    @ColumnInfo(name = "status_color")
    private String statusColor = "secondary";

    @ColumnInfo(name = "next_action")
    private String nextAction = "start";

    @SerializedName("chapter_code")
    @Nullable
    @ColumnInfo(name = "chapter_code")
    private String chapterCode;

    // Computed fields for getBabsWithProgress()
    @ColumnInfo(name = "computed_status_color")
    private String computedStatusColor;

    @ColumnInfo(name = "computed_next_action")
    private String computedNextAction;

    // Additional field for getBabsWithMateriCount()
    @ColumnInfo(name = "materi_count")
    private int materiCount;

    // Default constructor
    public Bab() {
        this.urutan = 0;
        this.isActive = true;
        this.isUnlocked = false;
    }

    // Constructor minimal
    @Ignore
    public Bab(String namaBab, int urutan) {
        this.namaBab = namaBab;
        this.urutan = urutan;
        this.isActive = true;
        this.isUnlocked = false;
    }

    // Constructor with unlock status
    @Ignore
    public Bab(String namaBab, int urutan, boolean isUnlocked) {
        this.namaBab = namaBab;
        this.urutan = urutan;
        this.isActive = true;
        this.isUnlocked = isUnlocked;
    }

    // Getters and Setters
    public int getIdBab() {
        return idBab;
    }

    public void setIdBab(int idBab) {
        this.idBab = idBab;
    }

    @NonNull
    public String getNamaBab() {
        return namaBab;
    }

    public void setNamaBab(@NonNull String namaBab) {
        this.namaBab = namaBab;
    }

    @Nullable
    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(@Nullable String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public int getUrutan() {
        return urutan;
    }

    public void setUrutan(int urutan) {
        this.urutan = urutan;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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

    public int getTotalMateri() {
        return totalMateri;
    }

    public void setTotalMateri(int totalMateri) {
        this.totalMateri = totalMateri;
    }

    public int getCompletedMateri() {
        return completedMateri;
    }

    public void setCompletedMateri(int completedMateri) {
        this.completedMateri = completedMateri;
    }

    public int getInProgressMateri() {
        return inProgressMateri;
    }

    public void setInProgressMateri(int inProgressMateri) {
        this.inProgressMateri = inProgressMateri;
    }

    public int getNotStartedMateri() {
        return notStartedMateri;
    }

    public void setNotStartedMateri(int notStartedMateri) {
        this.notStartedMateri = notStartedMateri;
    }

    public int getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(int progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public void setUnlocked(boolean unlocked) {
        isUnlocked = unlocked;
    }

    public String getStatusColor() {
        return statusColor;
    }

    public void setStatusColor(String statusColor) {
        this.statusColor = statusColor;
    }

    public String getNextAction() {
        return nextAction;
    }

    public void setNextAction(String nextAction) {
        this.nextAction = nextAction;
    }

    @Nullable
    public String getChapterCode() {
        return chapterCode;
    }

    public void setChapterCode(@Nullable String chapterCode) {
        this.chapterCode = chapterCode;
    }

    public String getComputedStatusColor() {
        return computedStatusColor;
    }

    public void setComputedStatusColor(String computedStatusColor) {
        this.computedStatusColor = computedStatusColor;
    }

    public String getComputedNextAction() {
        return computedNextAction;
    }

    public void setComputedNextAction(String computedNextAction) {
        this.computedNextAction = computedNextAction;
    }

    public int getMateriCount() {
        return materiCount;
    }

    public void setMateriCount(int materiCount) {
        this.materiCount = materiCount;
    }

    /**
     * Mendapatkan deskripsi singkat untuk display
     */
    public String getDeskripsiSingkat() {
        if (deskripsi != null && !deskripsi.isEmpty() && deskripsi.length() > 80) {
            return deskripsi.substring(0, 77) + "...";
        }
        return deskripsi != null ? deskripsi : "";
    }

    /**
     * Mendapatkan status progress dalam bentuk text yang user-friendly
     */
    public String getStatusProgressText() {
        if (progressPercentage >= 100) {
            return "Selesai (" + completedMateri + "/" + totalMateri + ")";
        } else if (progressPercentage > 0) {
            return "Sedang Belajar (" + completedMateri + "/" + totalMateri + ")";
        } else {
            return "Belum Dimulai";
        }
    }

    /**
     * Mendapatkan bab name tanpa prefix "BAB X: "
     */
    public String getNamaBabSaja() {
        if (namaBab.contains(":")) {
            return namaBab.substring(namaBab.indexOf(":") + 1).trim();
        }
        return namaBab;
    }

    /**
     * Mendapatkan nomor bab (extract dari "BAB 1: KALAM")
     */
    public String getNomorBab() {
        if (namaBab.contains("BAB")) {
            String[] parts = namaBab.split(":");
            if (parts.length > 0) {
                return parts[0].trim();
            }
        }
        return "";
    }

    /**
     * Check apakah bab sudah bisa diakses
     * Logic: Bab pertama selalu unlocked, bab selanjutnya unlocked jika bab sebelumnya completed >= 80%
     */
    public boolean isAccessible() {
        return isActive && isUnlocked;
    }

    /**
     * Get status untuk display di UI
     */
    public String getDisplayStatus() {
        if (!isActive) {
            return "Tidak Aktif";
        } else if (!isUnlocked) {
            return "Terkunci";
        } else if (progressPercentage >= 100) {
            return "Selesai";
        } else if (progressPercentage > 0) {
            return "Sedang Berjalan";
        } else {
            return "Belum Dimulai";
        }
    }

    @Override
    public String toString() {
        return "Bab{" +
                "idBab=" + idBab +
                ", namaBab='" + namaBab + '\'' +
                ", urutan=" + urutan +
                ", totalMateri=" + totalMateri +
                ", progressPercentage=" + progressPercentage +
                ", isUnlocked=" + isUnlocked +
                '}';
    }
}