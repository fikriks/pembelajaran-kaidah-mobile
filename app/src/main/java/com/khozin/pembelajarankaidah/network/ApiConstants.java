package com.khozin.pembelajarankaidah.network;

/**
 * Konstanta API untuk komunikasi dengan backend
 */
public class ApiConstants {

    // Base URL - adjust sesuai development/production
    public static final String BASE_URL = "http://192.168.1.4:8080/api/"; // Untuk device fisik di jaringan yang sama
    // public static final String BASE_URL = "http://192.168.1.100:8080/api/"; // Untuk device fisik (ganti dengan IP laptop)
    // public static final String BASE_URL = "https://yourapp.com/api/"; // Production

    // API Endpoints
    public static final String AUTH_LOGIN = "siswa/login";
    public static final String AUTH_REGISTER = "siswa/register";
    public static final String AUTH_PROFILE = "siswa/profile";

    // BAB/CHAPTER ENDPOINTS
    public static final String BAB_CHAPTERS = "bab/chapters";
    public static final String BAB_CHAPTER_DETAIL = "bab/chapters/{chapterCode}";
    public static final String BAB_PROGRESS_OVERVIEW = "kaidah/progress/overview";
    public static final String BAB_OVERALL_PROGRESS = "kaidah/progress/overall";

    public static final String KAIDAH_LIST = "kaidah";
    public static final String KAIDAH_DETAIL = "kaidah/{id}";
    public static final String KAIDAH_PROGRESS = "kaidah/{id}/progress";
    public static final String KAIDAH_GROUPED = "kaidah/grouped";

    public static final String SESI_START = "sesi/start";
    public static final String SESI_ACTIVE = "sesi/active";
    public static final String SESI_DETAIL = "sesi/{id}";
    public static final String SESI_JAWAB = "sesi/{id}/jawab";
    public static final String SESI_FINISH = "sesi/{id}/finish";
    public static final String SESI_HASIL = "sesi/{id}/hasil";

    public static final String PROGRESS_LIST = "progress";
    public static final String HISTORY_LIST = "history";
    public static final String STATISTIK = "statistik";

    // HTTP Headers
    public static final String HEADER_CONTENT_TYPE = "Content-Type: application/json";
    public static final String HEADER_ACCEPT = "Accept: application/json";
    public static final String HEADER_AUTHORIZATION = "Authorization: Bearer ";

    // Request Timeouts (dalam detik)
    public static final int CONNECT_TIMEOUT = 30;
    public static final int READ_TIMEOUT = 30;
    public static final int WRITE_TIMEOUT = 30;

    // Cache settings
    public static final int CACHE_SIZE = 10 * 1024 * 1024; // 10 MB
    public static final int CACHE_MAX_AGE = 5 * 60; // 5 menit

    // Response Status Codes
    public static final int STATUS_OK = 200;
    public static final int STATUS_CREATED = 201;
    public static final int STATUS_BAD_REQUEST = 400;
    public static final int STATUS_UNAUTHORIZED = 401;
    public static final int STATUS_FORBIDDEN = 403;
    public static final int STATUS_NOT_FOUND = 404;
    public static final int STATUS_INTERNAL_ERROR = 500;

    // Error Messages
    public static final String ERROR_NETWORK = "Tidak ada koneksi internet";
    public static final String ERROR_TIMEOUT = "Koneksi timeout, coba lagi";
    public static final String ERROR_SERVER = "Server sedang bermasalah";
    public static final String ERROR_UNKNOWN = "Terjadi kesalahan yang tidak diketahui";
    public static final String ERROR_INVALID_CREDENTIALS = "NIS atau password salah";
    public static final String ERROR_USER_NOT_FOUND = "Pengguna tidak ditemukan";
    public static final String ERROR_TOKEN_EXPIRED = "Session expired, silakan login kembali";

    // Private constructor untuk mencegah instansiasi
    private ApiConstants() {}
}