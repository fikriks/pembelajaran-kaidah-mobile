package com.khozin.pembelajarankaidah.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Response model for finish session API
 * Handles nested structure: data.sesi
 */
public class FinishSesiResponse implements Serializable {

    @SerializedName("sesi")
    private SesiLatihan sesi;

    // Default constructor
    public FinishSesiResponse() {}

    // Getters and Setters
    public SesiLatihan getSesi() {
        return sesi;
    }

    public void setSesi(SesiLatihan sesi) {
        this.sesi = sesi;
    }
}