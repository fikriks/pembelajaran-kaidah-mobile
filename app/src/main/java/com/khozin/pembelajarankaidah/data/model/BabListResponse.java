package com.khozin.pembelajarankaidah.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.ArrayList;

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
    private ChapterData data;

    /**
     * Inner class for chapter data wrapper
     */
    public static class ChapterData {
        @SerializedName("chapters")
        private List<Bab> chapters;

        @SerializedName("pagination")
        private PaginationData pagination;

        // Getters and Setters
        public List<Bab> getChapters() {
            return chapters;
        }

        public void setChapters(List<Bab> chapters) {
            this.chapters = chapters;
        }

        public PaginationData getPagination() {
            return pagination;
        }

        public void setPagination(PaginationData pagination) {
            this.pagination = pagination;
        }

        @Override
        public String toString() {
            return "ChapterData{" +
                    "chapters size=" + (chapters != null ? chapters.size() : 0) +
                    ", pagination=" + pagination +
                    '}';
        }
    }

    /**
     * Inner class for pagination data
     */
    public static class PaginationData {
        @SerializedName("total")
        private int total;

        @SerializedName("limit")
        private int limit;

        @SerializedName("offset")
        private int offset;

        @SerializedName("has_more")
        private boolean hasMore;

        // Getters and Setters
        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public boolean isHasMore() {
            return hasMore;
        }

        public void setHasMore(boolean hasMore) {
            this.hasMore = hasMore;
        }

        @Override
        public String toString() {
            return "PaginationData{" +
                    "total=" + total +
                    ", limit=" + limit +
                    ", offset=" + offset +
                    ", hasMore=" + hasMore +
                    '}';
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

    public ChapterData getData() {
        return data;
    }

    public void setData(ChapterData data) {
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
                ", data=" + data +
                '}';
    }
}