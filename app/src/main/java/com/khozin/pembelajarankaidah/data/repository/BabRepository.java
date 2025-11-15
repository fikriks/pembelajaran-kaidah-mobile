package com.khozin.pembelajarankaidah.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.khozin.pembelajarankaidah.data.model.Bab;
import com.khozin.pembelajarankaidah.data.model.BabListResponse;
import com.khozin.pembelajarankaidah.data.model.ChapterProgressResponse;
import com.khozin.pembelajarankaidah.data.remote.ApiService;

import java.util.List;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository untuk Bab/Chapter data
 * API-only repository - tidak menggunakan local database
 */
public class BabRepository {

    private final ApiService apiService;
    private final Executor executor;

    // LiveData untuk result
    private final MutableLiveData<List<Bab>> chaptersLiveData = new MutableLiveData<>();
    private final MutableLiveData<ChapterProgressResponse> progressOverviewLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    // Local cache untuk chapters
    private List<Bab> cachedChapters = new ArrayList<>();

    public BabRepository(Context context, ApiService apiService) {
        this.apiService = apiService;
        this.executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Get all chapters from cache
     */
    public LiveData<List<Bab>> getAllChapters() {
        MutableLiveData<List<Bab>> result = new MutableLiveData<>();
        result.setValue(cachedChapters);
        return result;
    }

    /**
     * Get active chapters from cache
     */
    public LiveData<List<Bab>> getActiveChapters() {
        MutableLiveData<List<Bab>> result = new MutableLiveData<>();
        List<Bab> activeChapters = new ArrayList<>();
        for (Bab bab : cachedChapters) {
            if (bab.isActive()) {
                activeChapters.add(bab);
            }
        }
        result.setValue(activeChapters);
        return result;
    }

    /**
     * Get chapters with progress from cache
     */
    public LiveData<List<Bab>> getChaptersWithProgress() {
        MutableLiveData<List<Bab>> result = new MutableLiveData<>();
        result.setValue(cachedChapters);
        return result;
    }

    /**
     * Get unlocked chapters from cache
     */
    public LiveData<List<Bab>> getUnlockedChapters() {
        MutableLiveData<List<Bab>> result = new MutableLiveData<>();
        List<Bab> unlockedChapters = new ArrayList<>();
        for (Bab bab : cachedChapters) {
            if (bab.isUnlocked()) {
                unlockedChapters.add(bab);
            }
        }
        result.setValue(unlockedChapters);
        return result;
    }

    /**
     * Get chapter by ID from cache
     */
    public LiveData<Bab> getChapterById(int chapterId) {
        MutableLiveData<Bab> result = new MutableLiveData<>();
        for (Bab bab : cachedChapters) {
            if (bab.getIdBab() == chapterId) {
                result.setValue(bab);
                break;
            }
        }
        return result;
    }

    /**
     * Load chapters from API
     */
    public void loadChaptersFromApi(String authToken) {
        loadingLiveData.setValue(true);

        apiService.getChapters("Bearer " + authToken).enqueue(new Callback<BabListResponse>() {
            @Override
            public void onResponse(Call<BabListResponse> call, Response<BabListResponse> response) {
                loadingLiveData.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    BabListResponse babResponse = response.body();
                    if (babResponse.isSuccess()) {
                        List<Bab> chapters = babResponse.getData().getChapters();
                        chaptersLiveData.setValue(chapters);

                        // Cache chapters locally
                        cacheChapters(chapters);
                    } else {
                        errorLiveData.setValue(babResponse.getMessage() != null ?
                                babResponse.getMessage() : "Failed to load chapters");
                    }
                } else {
                    errorLiveData.setValue("Server error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<BabListResponse> call, Throwable t) {
                loadingLiveData.setValue(false);
                errorLiveData.setValue("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * Load progress overview from API
     */
    public void loadProgressOverview(String authToken) {
        loadingLiveData.setValue(true);

        apiService.getProgressOverview("Bearer " + authToken).enqueue(new Callback<ChapterProgressResponse>() {
            @Override
            public void onResponse(Call<ChapterProgressResponse> call, Response<ChapterProgressResponse> response) {
                loadingLiveData.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    ChapterProgressResponse progressResponse = response.body();
                    if (progressResponse.isSuccess()) {
                        progressOverviewLiveData.setValue(progressResponse);

                        // Update cached chapters with progress info
                        updateCachedProgress(progressResponse);
                    } else {
                        errorLiveData.setValue(progressResponse.getMessage() != null ?
                                progressResponse.getMessage() : "Failed to load progress");
                    }
                } else {
                    errorLiveData.setValue("Server error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ChapterProgressResponse> call, Throwable t) {
                loadingLiveData.setValue(false);
                errorLiveData.setValue("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * Cache chapters locally
     */
    private void cacheChapters(List<Bab> chapters) {
        executor.execute(() -> {
            try {
                cachedChapters.clear();
                cachedChapters.addAll(chapters);
            } catch (Exception e) {
                errorLiveData.postValue("Error caching chapters: " + e.getMessage());
            }
        });
    }

    /**
     * Update cached progress information
     */
    private void updateCachedProgress(ChapterProgressResponse progressResponse) {
        executor.execute(() -> {
            try {
                if (progressResponse.getData() != null &&
                    progressResponse.getData().getChapters() != null) {

                    for (Bab progressChapter : progressResponse.getData().getChapters()) {
                        // Find corresponding chapter in cache
                        for (Bab cachedChapter : cachedChapters) {
                            if (cachedChapter.getIdBab() == progressChapter.getIdBab()) {
                                // Update cached chapter with progress info
                                cachedChapter.setTotalMateri(progressChapter.getTotalMateri());
                                cachedChapter.setCompletedMateri(progressChapter.getCompletedMateri());
                                cachedChapter.setInProgressMateri(progressChapter.getInProgressMateri());
                                cachedChapter.setNotStartedMateri(progressChapter.getNotStartedMateri());
                                cachedChapter.setProgressPercentage(progressChapter.getProgressPercentage());
                                cachedChapter.setStatusColor(progressChapter.getStatusColor());
                                cachedChapter.setNextAction(progressChapter.getNextAction());
                                // Automatically update unlock status based on progress
                                cachedChapter.updateUnlockStatus();
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                errorLiveData.postValue("Error updating cached progress: " + e.getMessage());
            }
        });
    }

    /**
     * Get chapter by ID (synchronous)
     */
    public Bab getChapterByIdSync(int chapterId) {
        for (Bab bab : cachedChapters) {
            if (bab.getIdBab() == chapterId) {
                return bab;
            }
        }
        return null;
    }

    /**
     * Get active chapters (synchronous)
     */
    public List<Bab> getActiveChaptersSync() {
        List<Bab> activeChapters = new ArrayList<>();
        for (Bab bab : cachedChapters) {
            if (bab.isActive()) {
                activeChapters.add(bab);
            }
        }
        return activeChapters;
    }

    /**
     * Search chapters by name
     */
    public List<Bab> searchChapters(String query) {
        List<Bab> results = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) {
            return results;
        }

        String searchQuery = query.toLowerCase().trim();
        for (Bab bab : cachedChapters) {
            if (bab.getNamaBab() != null && bab.getNamaBab().toLowerCase().contains(searchQuery)) {
                results.add(bab);
            }
        }
        return results;
    }

    /**
     * Update chapter progress in cache
     */
    public void updateChapterProgress(int chapterId, int totalMateri, int completedMateri) {
        executor.execute(() -> {
            try {
                int inProgressMateri = totalMateri - completedMateri;
                int notStartedMateri = 0; // Calculate based on logic
                int progressPercentage = totalMateri > 0 ?
                        (int) ((completedMateri / (double) totalMateri) * 100) : 0;

                String statusColor = progressPercentage >= 100 ? "success" :
                                  progressPercentage > 0 ? "warning" : "secondary";
                String nextAction = progressPercentage >= 100 ? "review" :
                                 progressPercentage > 0 ? "continue" : "start";

                // Update cached chapter
                for (Bab bab : cachedChapters) {
                    if (bab.getIdBab() == chapterId) {
                        bab.setTotalMateri(totalMateri);
                        bab.setCompletedMateri(completedMateri);
                        bab.setInProgressMateri(inProgressMateri);
                        bab.setNotStartedMateri(notStartedMateri);
                        bab.setProgressPercentage(progressPercentage);
                        bab.setStatusColor(statusColor);
                        bab.setNextAction(nextAction);
                        // Automatically update unlock status based on progress
                        bab.updateUnlockStatus();
                        break;
                    }
                }

                // Check if next chapter should be unlocked
                checkAndUnlockNextChapter();

            } catch (Exception e) {
                errorLiveData.postValue("Error updating progress: " + e.getMessage());
            }
        });
    }

    /**
     * Check and unlock next chapter based on progress
     */
    private void checkAndUnlockNextChapter() {
        executor.execute(() -> {
            try {
                // Find chapters in order
                cachedChapters.sort((b1, b2) -> Integer.compare(b1.getUrutan(), b2.getUrutan()));

                Bab nextChapterToUnlock = null;

                for (int i = 0; i < cachedChapters.size() - 1; i++) {
                    Bab currentChapter = cachedChapters.get(i);
                    Bab nextChapter = cachedChapters.get(i + 1);

                    // If current chapter is completed and next chapter is locked
                    if (currentChapter.getProgressPercentage() >= 100 &&
                        !nextChapter.isUnlocked()) {
                        nextChapterToUnlock = nextChapter;
                        break;
                    }
                }

                if (nextChapterToUnlock != null) {
                    nextChapterToUnlock.setUnlocked(true);
                }
            } catch (Exception e) {
                errorLiveData.postValue("Error unlocking chapter: " + e.getMessage());
            }
        });
    }

    /**
     * Refresh chapters from API
     */
    public void refreshChapters(String authToken) {
        loadChaptersFromApi(authToken);
        loadProgressOverview(authToken);
    }

    // Getters for LiveData
    public MutableLiveData<List<Bab>> getChaptersLiveData() {
        return chaptersLiveData;
    }

    public MutableLiveData<ChapterProgressResponse> getProgressOverviewLiveData() {
        return progressOverviewLiveData;
    }

    public MutableLiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    /**
     * Clear error message
     */
    public void clearError() {
        errorLiveData.setValue(null);
    }
}