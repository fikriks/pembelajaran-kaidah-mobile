package com.khozin.pembelajarankaidah.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.ArrayList;

/**
 * Response wrapper untuk API endpoint yang mengembalikan data dengan struktur nested
 * Digunakan untuk endpoint /api/kaidah yang mengembalikan { data: { kaidah: [...] } }
 */
public class KaidahListResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private KaidahData data;

    /**
     * Inner class untuk merepresentasikan struktur data yang dikembalikan API
     */
    public static class KaidahData {
        @SerializedName("kaidah")
        private List<MateriKaidah> kaidah;

        @SerializedName("pagination")
        private Pagination pagination;

        public List<MateriKaidah> getKaidah() {
            return kaidah;
        }

        public void setKaidah(List<MateriKaidah> kaidah) {
            this.kaidah = kaidah;
        }

        public Pagination getPagination() {
            return pagination;
        }

        public void setPagination(Pagination pagination) {
            this.pagination = pagination;
        }
    }

    /**
     * Inner class untuk informasi pagination
     */
    public static class Pagination {
        @SerializedName("current_page")
        private int currentPage;

        @SerializedName("per_page")
        private int perPage;

        @SerializedName("total")
        private int total;

        @SerializedName("total_pages")
        private int totalPages;

        // Getters and setters
        public int getCurrentPage() { return currentPage; }
        public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }

        public int getPerPage() { return perPage; }
        public void setPerPage(int perPage) { this.perPage = perPage; }

        public int getTotal() { return total; }
        public void setTotal(int total) { this.total = total; }

        public int getTotalPages() { return totalPages; }
        public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    }

    // Getters and setters
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

    public KaidahData getData() {
        return data;
    }

    public void setData(KaidahData data) {
        this.data = data;
    }

    /**
     * Check jika response berhasil
     */
    public boolean isSuccess() {
        return "success".equals(status) && code == 200;
    }

    /**
     * Get list kaidah dari response
     */
    public List<MateriKaidah> getKaidahList() {
        if (data != null && data.getKaidah() != null) {
            return data.getKaidah();
        }
        return null;
    }
}