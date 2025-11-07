package com.khozin.pembelajarankaidah.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Linear Congruent Method (LCM) Algorithm Implementation
 *
 * Algoritma untuk menghasilkan bilangan acak dengan formula:
 * Xn+1 = (a × Xn + c) mod m
 *
 * Parameters sesuai skripsi:
 * - a (multiplier) = 10
 * - c (increment) = 23
 * - m (modulus) = 29
 * - X0 (seed) = timestamp + user_id + materi_id
 */
public class LinearCongruentMethod {

    // LCM Parameters (sesuai skripsi)
    private static final long A = 10;  // multiplier
    private static final long C = 23;  // increment
    private static final long M = 29;  // modulus

    private long seed;
    private long currentX;

    /**
     * Constructor dengan seed tertentu
     */
    public LinearCongruentMethod(long seed) {
        this.seed = seed;
        this.currentX = seed;
    }

    /**
     * Constructor dengan seed default (current timestamp)
     */
    public LinearCongruentMethod() {
        this(System.currentTimeMillis());
    }

    /**
     * Generate seed dari kombinasi timestamp, user ID, dan materi ID
     */
    public static long generateSeed(int userId, int materiId) {
        long timestamp = System.currentTimeMillis();
        return timestamp + userId + materiId;
    }

    /**
     * Generate seed untuk sesi latihan
     */
    public static long generateSeedForSession(int userId, int materiId, int sessionId) {
        long timestamp = System.currentTimeMillis();
        return timestamp + userId + materiId + (sessionId * 1000);
    }

    /**
     * Generate bilangan acak berikutnya menggunakan LCM
     */
    public long nextRandom() {
        currentX = (A * currentX + C) % M;
        return currentX;
    }

    /**
     * Generate bilangan acak dalam range [min, max]
     */
    public long nextRandom(long min, long max) {
        if (min > max) {
            throw new IllegalArgumentException("Min harus lebih kecil atau sama dengan max");
        }

        long range = max - min + 1;
        long randomValue = nextRandom();
        return min + (randomValue % range);
    }

    /**
     * Generate list of random numbers
     */
    public List<Long> generateRandomList(int count) {
        List<Long> randomList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            randomList.add(nextRandom());
        }
        return randomList;
    }

    /**
     * Generate list of random numbers dalam range tertentu
     */
    public List<Long> generateRandomList(int count, long min, long max) {
        List<Long> randomList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            randomList.add(nextRandom(min, max));
        }
        return randomList;
    }

    /**
     * Acak index array menggunakan LCM
     */
    public int[] shuffleIndices(int size) {
        if (size <= 0) {
            return new int[0];
        }

        // Generate list index 0..size-1
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            indices.add(i);
        }

        // Acak menggunakan Fisher-Yates dengan LCM
        for (int i = size - 1; i > 0; i--) {
            // Generate random index antara 0 dan i
            long randomIndex = nextRandom(0, i);
            Collections.swap(indices, i, (int) randomIndex);
        }

        // Convert ke array
        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = indices.get(i);
        }

        return result;
    }

    /**
     * Acak list generic menggunakan LCM
     */
    public <T> List<T> shuffleList(List<T> list) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        List<T> shuffledList = new ArrayList<>(list);
        int size = shuffledList.size();

        // Fisher-Yates shuffle dengan LCM
        for (int i = size - 1; i > 0; i--) {
            long randomIndex = nextRandom(0, i);
            Collections.swap(shuffledList, i, (int) randomIndex);
        }

        return shuffledList;
    }

    /**
     * Reset ke seed awal
     */
    public void reset() {
        currentX = seed;
    }

    /**
     * Set seed baru
     */
    public void setSeed(long newSeed) {
        this.seed = newSeed;
        this.currentX = newSeed;
    }

    /**
     * Get current seed
     */
    public long getSeed() {
        return seed;
    }

    /**
     * Get current X value
     */
    public long getCurrentX() {
        return currentX;
    }

    /**
     * Test randomness quality (sederhana)
     */
    public boolean testRandomness(int sampleSize) {
        if (sampleSize <= 0) return false;

        // Hitung frekuensi setiap nilai (0-28)
        int[] frequency = new int[29];

        for (int i = 0; i < sampleSize; i++) {
            long value = nextRandom();
            if (value >= 0 && value < 29) {
                frequency[(int) value]++;
            }
        }

        // Check distribusi (sederhana)
        double expected = (double) sampleSize / 29.0;
        double tolerance = expected * 0.3; // 30% tolerance

        for (int freq : frequency) {
            if (Math.abs(freq - expected) > tolerance) {
                return false;
            }
        }

        return true;
    }

    /**
     * Get LCM parameters info
     */
    public String getAlgorithmInfo() {
        return String.format(
            "Linear Congruent Method\n" +
            "Formula: Xn+1 = (a × Xn + c) mod m\n" +
            "Parameters: a=%d, c=%d, m=%d\n" +
            "Current Seed: %d\n" +
            "Current X: %d",
            A, C, M, seed, currentX
        );
    }

    /**
     * Generate sequence untuk debugging
     */
    public List<Long> generateSequence(int length) {
        List<Long> sequence = new ArrayList<>();
        long tempX = currentX; // Backup current state

        for (int i = 0; i < length; i++) {
            sequence.add(currentX);
            nextRandom();
        }

        currentX = tempX; // Restore
        return sequence;
    }

    /**
     * Check apakah seed akan menghasilkan period yang baik
     */
    public boolean hasGoodPeriod() {
        // Check basic conditions untuk full period
        return (C % M != 0) && // C dan M harus relatif prima
               (M % 4 == 0 && (A - 1) % 4 == 0) || // Jika M kelipatan 4
               (M % 4 != 0); // Jika M bukan kelipatan 4
    }

    @Override
    public String toString() {
        return "LinearCongruentMethod{" +
                "seed=" + seed +
                ", currentX=" + currentX +
                ", a=" + A +
                ", c=" + C +
                ", m=" + M +
                '}';
    }
}