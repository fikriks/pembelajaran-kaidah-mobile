package com.khozin.pembelajarankaidah.data.remote;

import com.khozin.pembelajarankaidah.data.model.ApiResponse;
import com.khozin.pembelajarankaidah.data.model.LoginResponse;
import com.khozin.pembelajarankaidah.data.model.Siswa;
import com.khozin.pembelajarankaidah.data.model.Bab;
import com.khozin.pembelajarankaidah.data.model.MateriKaidah;
import com.khozin.pembelajarankaidah.data.model.SesiLatihan;
import com.khozin.pembelajarankaidah.data.model.StartSesiResponse;
import com.khozin.pembelajarankaidah.data.model.FinishSesiResponse;
import com.khozin.pembelajarankaidah.data.model.RiwayatBelajar;
import com.khozin.pembelajarankaidah.data.model.KaidahListResponse;
import com.khozin.pembelajarankaidah.data.model.KaidahGroupedResponse;
import com.khozin.pembelajarankaidah.data.model.BabListResponse;
import com.khozin.pembelajarankaidah.data.model.ChapterProgressResponse;
import com.khozin.pembelajarankaidah.network.ApiConstants;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.*;

/**
 * Main API Service interface untuk Retrofit
 * Covers semua endpoints yang dibutuhkan mobile app
 */
public interface ApiService {

    // ===================
    // AUTHENTICATION
    // ===================

    @POST(ApiConstants.AUTH_LOGIN)
    Call<ApiResponse<LoginResponse>> login(
            @Body Map<String, String> loginRequest
    );

    @POST(ApiConstants.AUTH_REGISTER)
    Call<ApiResponse<Siswa>> register(
            @Body Map<String, String> registerRequest
    );

    @GET(ApiConstants.AUTH_PROFILE)
    Call<ApiResponse<Siswa>> getProfile(
            @Header("Authorization") String sessionToken
    );

    // ===================
    // BAB/CHAPTER ENDPOINTS
    // ===================

    @GET(ApiConstants.BAB_CHAPTERS)
    Call<BabListResponse> getChapters(
            @Header("Authorization") String sessionToken
    );

    @GET(ApiConstants.BAB_CHAPTER_DETAIL)
    Call<ApiResponse<Bab>> getChapterDetail(
            @Path("chapterCode") String chapterCode
    );

    @GET(ApiConstants.BAB_PROGRESS_OVERVIEW)
    Call<ChapterProgressResponse> getProgressOverview(@Header("Authorization") String sessionToken);

    @GET(ApiConstants.BAB_OVERALL_PROGRESS)
    Call<ApiResponse<Map<String, Object>>> getOverallProgress();

    // ===================
    // KAIDAH/MATERI
    // ===================

    @GET(ApiConstants.KAIDAH_LIST)
    Call<ApiResponse<List<MateriKaidah>>> getKaidahList();

    /**
     * Get kaidah list dengan response wrapper yang sesuai struktur API
     * Untuk menghandle response { data: { kaidah: [...] } }
     */
    @GET(ApiConstants.KAIDAH_LIST)
    Call<KaidahListResponse> getKaidahListWithWrapper();

    /**
     * Get kaidah grouped by bab
     */
    @GET(ApiConstants.KAIDAH_GROUPED)
    Call<KaidahGroupedResponse> getKaidahGrouped(
            @Header("Authorization") String sessionToken
    );

    @GET(ApiConstants.KAIDAH_FIRST_BY_BAB)
    Call<ApiResponse<MateriKaidah>> getFirstMateriByBab(
            @Path("babId") int babId,
            @Header("Authorization") String sessionToken
    );

    @GET(ApiConstants.KAIDAH_DETAIL)
    Call<ApiResponse<MateriKaidah>> getKaidahDetail(
            @Path("id") int kaidahId
    );

    @GET(ApiConstants.KAIDAH_PROGRESS)
    Call<ApiResponse<RiwayatBelajar>> getKaidahProgress(
            @Path("id") int kaidahId
    );

    // ===================
    // SESI LATIHAN
    // ===================

    @POST(ApiConstants.SESI_START)
    Call<ApiResponse<StartSesiResponse>> startSesi(
            @Body Map<String, Object> sesiRequest
    );

    @GET(ApiConstants.SESI_ACTIVE)
    Call<ApiResponse<Map<String, Object>>> getActiveSesi(
            @Header("Authorization") String authToken
    );

    @GET(ApiConstants.SESI_DETAIL)
    Call<ApiResponse<SesiLatihan>> getSesiDetail(
            @Path("id") int sesiId
    );

    @POST(ApiConstants.SESI_JAWAB)
    Call<ApiResponse<Map<String, Object>>> submitJawaban(
            @Path("id") int sesiId,
            @Body Map<String, Object> jawabanRequest
    );

    @POST(ApiConstants.SESI_FINISH)
    Call<ApiResponse<FinishSesiResponse>> finishActiveSession(
            @Header("Authorization") String authToken
    );

    /**
     * Cancel/delete active session tanpa menyelesaikan
     * Digunakan ketika user keluar dari quiz sebelum selesai
     */
    @POST(ApiConstants.SESI_CANCEL)
    Call<ApiResponse<Map<String, Object>>> cancelActiveSession(
            @Header("Authorization") String authToken
    );

    @POST(ApiConstants.SESI_FINISH_WITH_ID)
    Call<ApiResponse<FinishSesiResponse>> finishSesi(
            @Path("id") int sesiId
    );

    @GET(ApiConstants.SESI_HASIL)
    Call<ApiResponse<Map<String, Object>>> getHasilSesi(
            @Path("id") int sesiId
    );

    // ===================
    // PROGRESS & STATISTIK
    // ===================

    @GET("progress")
    Call<ApiResponse<Map<String, Object>>> getProgress(
            @Header("Authorization") String sessionToken
    );

    @GET(ApiConstants.PROGRESS_LIST)
    Call<ApiResponse<List<RiwayatBelajar>>> getProgressList();

    @GET(ApiConstants.HISTORY_LIST)
    Call<ApiResponse<List<SesiLatihan>>> getHistoryList(
            @Query("limit") int limit,
            @Query("offset") int offset
    );

    @GET(ApiConstants.STATISTIK)
    Call<ApiResponse<Map<String, Object>>> getStatistik();

    // ===================
    // SOAL/QUESTION ENDPOINTS
    // ===================

    /**
     * Get random soal untuk bab tertentu
     * Menggunakan LCM algorithm untuk pengacakan
     */
    @POST("soal/random")
    Call<ApiResponse<Map<String, Object>>> getRandomSoal(
            @Body Map<String, Object> soalRequest
    );

    // ===================
    // PROGRESS METHODS
    // ===================

    /**
     * Mark materi as completed
     * Sync progress dengan server
     */
    @POST(ApiConstants.MATERI_COMPLETE)
    Call<ApiResponse<Map<String, Object>>> completeMateri(
            @Header("Authorization") String authToken,
            @Path("id") int materiId
    );

    // ===================
    // UTILITY METHODS
    // ===================

    /**
     * Check koneksi ke server
     */
    @GET("ping")
    Call<ApiResponse<Map<String, String>>> ping();

    /**
     * Update device info untuk tracking
     */
    @POST("device/update")
    Call<ApiResponse<Map<String, String>>> updateDeviceInfo(
            @Body Map<String, String> deviceInfo
    );

    /**
     * Logout (invalidate token di server)
     */
    @POST("logout")
    Call<ApiResponse<Map<String, String>>> logout(
            @Header("Authorization") String sessionToken
    );
}