package com.khozin.pembelajarankaidah.database.entity;

import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

/**
 * Result class for learning streak statistics
 */
public class LearningStreak {
    @ColumnInfo(name = "max_streak")
    public int maxStreak;

    @ColumnInfo(name = "total_learning_days")
    public int totalLearningDays;

    public LearningStreak() {}

    @NonNull
    @Override
    public String toString() {
        return "LearningStreak{" +
                "maxStreak=" + maxStreak +
                ", totalLearningDays=" + totalLearningDays +
                '}';
    }
}