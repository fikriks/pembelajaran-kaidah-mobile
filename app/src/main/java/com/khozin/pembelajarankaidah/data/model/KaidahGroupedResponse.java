package com.khozin.pembelajarankaidah.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.ArrayList;

/**
 * Response model untuk grouped kaidah API endpoint
 * Sesuai dengan response dari /api/kaidah/grouped
 */
public class KaidahGroupedResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private GroupedData data;

    public static class GroupedData {
        @SerializedName("groups")
        private List<KaidahGroup> groups;

        @SerializedName("summary")
        private Summary summary;

        public List<KaidahGroup> getGroups() {
            return groups;
        }

        public void setGroups(List<KaidahGroup> groups) {
            this.groups = groups;
        }

        public Summary getSummary() {
            return summary;
        }

        public void setSummary(Summary summary) {
            this.summary = summary;
        }
    }

    public static class Summary {
        @SerializedName("total_bab")
        private int totalBab;

        @SerializedName("total_kaidah")
        private int totalKaidah;

        @SerializedName("total_soal")
        private int totalSoal;

        public int getTotalBab() {
            return totalBab;
        }

        public void setTotalBab(int totalBab) {
            this.totalBab = totalBab;
        }

        public int getTotalKaidah() {
            return totalKaidah;
        }

        public void setTotalKaidah(int totalKaidah) {
            this.totalKaidah = totalKaidah;
        }

        public int getTotalSoal() {
            return totalSoal;
        }

        public void setTotalSoal(int totalSoal) {
            this.totalSoal = totalSoal;
        }
    }

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

    public GroupedData getData() {
        return data;
    }

    public void setData(GroupedData data) {
        this.data = data;
    }

    /**
     * Check if response is successful
     */
    public boolean isSuccess() {
        return "success".equals(status);
    }

    @Override
    public String toString() {
        return "KaidahGroupedResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }
}