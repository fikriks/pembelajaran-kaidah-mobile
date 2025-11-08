package com.khozin.pembelajarankaidah.database.entity;

import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

/**
 * Result class for LCM seed statistics
 */
public class SeedStatistics {
    @ColumnInfo(name = "total_sesi")
    public int totalSesi;

    @ColumnInfo(name = "unique_seeds")
    public int uniqueSeeds;

    @ColumnInfo(name = "max_seed")
    public long maxSeed;

    @ColumnInfo(name = "min_seed")
    public long minSeed;

    public SeedStatistics() {}

    @NonNull
    @Override
    public String toString() {
        return "SeedStatistics{" +
                "totalSesi=" + totalSesi +
                ", uniqueSeeds=" + uniqueSeeds +
                ", maxSeed=" + maxSeed +
                ", minSeed=" + minSeed +
                '}';
    }
}