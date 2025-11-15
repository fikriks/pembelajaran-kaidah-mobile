package com.khozin.pembelajarankaidah.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Response model for start session API
 * Handles nested structure: data.sesi and data.soal
 */
public class StartSesiResponse {

    @SerializedName("sesi")
    private SesiLatihan sesi;

    @SerializedName("soal")
    private List<Soal> soal;

    @SerializedName("lcm_info")
    private LCMInfo lcmInfo;

    // Default constructor
    public StartSesiResponse() {}

    // Getters and Setters
    public SesiLatihan getSesi() {
        return sesi;
    }

    public void setSesi(SesiLatihan sesi) {
        this.sesi = sesi;
    }

    public List<Soal> getSoal() {
        return soal;
    }

    public void setSoal(List<Soal> soal) {
        this.soal = soal;
    }

    public LCMInfo getLcmInfo() {
        return lcmInfo;
    }

    public void setLcmInfo(LCMInfo lcmInfo) {
        this.lcmInfo = lcmInfo;
    }

    /**
     * LCM Algorithm info
     */
    public static class LCMInfo {
        @SerializedName("algorithm")
        private String algorithm;

        @SerializedName("parameters")
        private LCMParameters parameters;

        @SerializedName("seed")
        private long seed;

        @SerializedName("randomization_verified")
        private boolean randomizationVerified;

        // Getters and Setters
        public String getAlgorithm() {
            return algorithm;
        }

        public void setAlgorithm(String algorithm) {
            this.algorithm = algorithm;
        }

        public LCMParameters getParameters() {
            return parameters;
        }

        public void setParameters(LCMParameters parameters) {
            this.parameters = parameters;
        }

        public long getSeed() {
            return seed;
        }

        public void setSeed(long seed) {
            this.seed = seed;
        }

        public boolean isRandomizationVerified() {
            return randomizationVerified;
        }

        public void setRandomizationVerified(boolean randomizationVerified) {
            this.randomizationVerified = randomizationVerified;
        }
    }

    /**
     * LCM Algorithm parameters
     */
    public static class LCMParameters {
        @SerializedName("multiplier")
        private long multiplier;

        @SerializedName("increment")
        private long increment;

        @SerializedName("modulus")
        private long modulus;

        @SerializedName("current_seed")
        private long currentSeed;

        // Getters and Setters
        public long getMultiplier() {
            return multiplier;
        }

        public void setMultiplier(long multiplier) {
            this.multiplier = multiplier;
        }

        public long getIncrement() {
            return increment;
        }

        public void setIncrement(long increment) {
            this.increment = increment;
        }

        public long getModulus() {
            return modulus;
        }

        public void setModulus(long modulus) {
            this.modulus = modulus;
        }

        public long getCurrentSeed() {
            return currentSeed;
        }

        public void setCurrentSeed(long currentSeed) {
            this.currentSeed = currentSeed;
        }
    }
}