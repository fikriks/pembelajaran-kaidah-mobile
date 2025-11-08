package com.khozin.pembelajarankaidah.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Response model untuk chapter progress overview API
 * Handles response structure: { status: "success", data: { overall_progress: int, chapters: [...] } }
 */
public class ChapterProgressResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private ChapterProgressData data;

    /**
     * Inner class untuk progress data
     */
    public static class ChapterProgressData {
        @SerializedName("overall_progress")
        private int overallProgress;

        @SerializedName("total_chapters")
        private int totalChapters;

        @SerializedName("unlocked_chapters")
        private int unlockedChapters;

        @SerializedName("chapters")
        private List<Bab> chapters;

        // Getters and Setters
        public int getOverallProgress() {
            return overallProgress;
        }

        public void setOverallProgress(int overallProgress) {
            this.overallProgress = overallProgress;
        }

        public int getTotalChapters() {
            return totalChapters;
        }

        public void setTotalChapters(int totalChapters) {
            this.totalChapters = totalChapters;
        }

        public int getUnlockedChapters() {
            return unlockedChapters;
        }

        public void setUnlockedChapters(int unlockedChapters) {
            this.unlockedChapters = unlockedChapters;
        }

        public List<Bab> getChapters() {
            return chapters;
        }

        public void setChapters(List<Bab> chapters) {
            this.chapters = chapters;
        }

        @Override
        public String toString() {
            return "ChapterProgressData{" +
                    "overallProgress=" + overallProgress +
                    ", totalChapters=" + totalChapters +
                    ", unlockedChapters=" + unlockedChapters +
                    ", chapters size=" + (chapters != null ? chapters.size() : 0) +
                    '}';
        }
    }

    // Getters and Setters for outer class
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

    public ChapterProgressData getData() {
        return data;
    }

    public void setData(ChapterProgressData data) {
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
        return "ChapterProgressResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }
}