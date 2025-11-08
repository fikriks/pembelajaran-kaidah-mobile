package com.khozin.pembelajarankaidah.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Response wrapper untuk Bab list API
 * Handles response structure: { status: "success", data: [...] }
 */
public class BabListResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private List<Bab> data;

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Bab> getData() {
        return data;
    }

    public void setData(List<Bab> data) {
        this.data = data;
    }

    /**
     * Check if response is successful
     */
    public boolean isSuccess() {
        return "success".equals(status) && code == 200;
    }

    @Override
    public String toString() {
        return "BabListResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", code=" + code +
                ", data size=" + (data != null ? data.size() : 0) +
                '}';
    }
}