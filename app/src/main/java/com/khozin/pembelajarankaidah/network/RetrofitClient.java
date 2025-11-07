package com.khozin.pembelajarankaidah.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

/**
 * Retrofit Client untuk konfigurasi network
 * Singleton pattern untuk satu instance在整个app生命周期
 */
public class RetrofitClient {

    private static RetrofitClient instance = null;
    private final Retrofit retrofit;
    private final OkHttpClient okHttpClient;

    private RetrofitClient() {
        // Create logging interceptor
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Create OkHttp Client
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(ApiConstants.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(ApiConstants.READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(ApiConstants.WRITE_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(chain -> {
                    // Add common headers
                    okhttp3.Request originalRequest = chain.request();
                    okhttp3.Request.Builder requestBuilder = originalRequest.newBuilder()
                            .header("Accept", "application/json")
                            .header("Content-Type", "application/json")
                            .method(originalRequest.method(), originalRequest.body());
                    return chain.proceed(requestBuilder.build());
                })
                .build();

        // Create Retrofit instance
        retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Get singleton instance
     */
    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    /**
     * Get API Service
     */
    public com.khozin.pembelajarankaidah.data.remote.ApiService getApiService() {
        return retrofit.create(com.khozin.pembelajarankaidah.data.remote.ApiService.class);
    }

    /**
     * Get Retrofit instance
     */
    public Retrofit getRetrofit() {
        return retrofit;
    }

    /**
     * Get OkHttpClient
     */
    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    /**
     * Reset singleton instance (untuk testing atau config changes)
     */
    public static void resetInstance() {
        instance = null;
    }

    /**
     * Update base URL (untuk switching antara development/production)
     */
    public void updateBaseUrl(String newBaseUrl) {
        // Reset instance dengan base URL baru
        resetInstance();
    }

    /**
     * Check apakah network tersedia
     */
    public boolean isNetworkAvailable() {
        // Basic check - implement dengan ConnectivityManager di Android
        return true; // Placeholder, implement proper check
    }
}