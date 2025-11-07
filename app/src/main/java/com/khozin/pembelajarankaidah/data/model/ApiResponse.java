package com.khozin.pembelajarankaidah.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.annotations.SerializedName;

/**
 * Generic API Response wrapper
 * Untuk standardisasi response dari backend
 */
public class ApiResponse<T> {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    @Nullable
    private T data;

    @SerializedName("errors")
    @Nullable
    private Object errors;

    @SerializedName("code")
    private int code;

    // Default constructor
    public ApiResponse() {
        this.status = "unknown";
        this.code = 0;
    }

    // Constructor untuk success response
    public ApiResponse(String message, T data) {
        this.status = "success";
        this.message = message;
        this.data = data;
        this.code = 200;
    }

    // Constructor untuk error response
    public ApiResponse(String status, String message, int code) {
        this.status = status;
        this.message = message;
        this.code = code;
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

    @Nullable
    public T getData() {
        return data;
    }

    public void setData(@Nullable T data) {
        this.data = data;
    }

    @Nullable
    public Object getErrors() {
        return errors;
    }

    public void setErrors(@Nullable Object errors) {
        this.errors = errors;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Check apakah response sukses
     */
    public boolean isSuccess() {
        return "success".equals(status) && code >= 200 && code < 300;
    }

    /**
     * Check apakah response error
     */
    public boolean isError() {
        return "error".equals(status) || code >= 400;
    }

    /**
     * Check apakah ada data
     */
    public boolean hasData() {
        return data != null;
    }

    /**
     * Get error message yang user-friendly
     */
    public String getErrorMessage() {
        if (isSuccess()) {
            return "";
        }

        // Jika ada custom message, gunakan itu
        if (message != null && !message.isEmpty()) {
            return message;
        }

        // Fallback ke default error messages based on status code
        switch (code) {
            case 400:
                return "Request tidak valid";
            case 401:
                return "Unauthorized, silakan login kembali";
            case 403:
                return "Tidak memiliki akses";
            case 404:
                return "Data tidak ditemukan";
            case 500:
                return "Server sedang bermasalah";
            default:
                return "Terjadi kesalahan yang tidak diketahui";
        }
    }

    /**
     * Create success response
     */
    public static <T> ApiResponse<T> success(@NonNull String message, @Nullable T data) {
        return new ApiResponse<>(message, data);
    }

    /**
     * Create error response
     */
    public static <T> ApiResponse<T> error(@NonNull String message, int code) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus("error");
        response.setMessage(message);
        response.setCode(code);
        return response;
    }

    /**
     * Create network error response
     */
    public static <T> ApiResponse<T> networkError(@NonNull String message) {
        return error(message, 0);
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", code=" + code +
                '}';
    }
}