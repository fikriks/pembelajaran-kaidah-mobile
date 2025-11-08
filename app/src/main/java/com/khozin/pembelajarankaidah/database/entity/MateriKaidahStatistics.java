package com.khozin.pembelajarankaidah.database.entity;

import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

/**
 * Result class for MateriKaidah statistics query
 */
public class MateriKaidahStatistics {
    @ColumnInfo(name = "total")
    public int total;

    public MateriKaidahStatistics(int total) {
        this.total = total;
    }

    @NonNull
    @Override
    public String toString() {
        return "MateriKaidahStatistics{" +
                "total=" + total +
                '}';
    }
}

