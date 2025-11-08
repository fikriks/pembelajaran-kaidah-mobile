package com.khozin.pembelajarankaidah.database.entity;

import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

/**
 * Result class for RiwayatBelajar statistics query
 */
public class RiwayatBelajarStatistics {
    @ColumnInfo(name = "total_materi")
    public int totalMateri;

    @ColumnInfo(name = "materi_selesai")
    public int materiSelesai;

    @ColumnInfo(name = "materi_dipelajari")
    public int materiDipelajari;

    @ColumnInfo(name = "rata_rata_progress")
    public double rataRataProgress;

    @ColumnInfo(name = "total_sesi")
    public long totalSesi;

    @ColumnInfo(name = "rata_rata_skor")
    public double rataRataSkor;

    public RiwayatBelajarStatistics(int totalMateri, int materiSelesai, int materiDipelajari, double rataRataProgress, long totalSesi, double rataRataSkor) {
        this.totalMateri = totalMateri;
        this.materiSelesai = materiSelesai;
        this.materiDipelajari = materiDipelajari;
        this.rataRataProgress = rataRataProgress;
        this.totalSesi = totalSesi;
        this.rataRataSkor = rataRataSkor;
    }

    @NonNull
    @Override
    public String toString() {
        return "RiwayatBelajarStatistics{" +
                "totalMateri=" + totalMateri +
                ", materiSelesai=" + materiSelesai +
                ", materiDipelajari=" + materiDipelajari +
                ", rataRataProgress=" + rataRataProgress +
                ", totalSesi=" + totalSesi +
                ", rataRataSkor=" + rataRataSkor +
                '}';
    }
}

