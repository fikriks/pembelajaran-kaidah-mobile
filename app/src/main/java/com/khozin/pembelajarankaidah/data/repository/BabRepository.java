package com.khozin.pembelajarankaidah.data.repository;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.khozin.pembelajarankaidah.data.model.Bab;
import com.khozin.pembelajarankaidah.data.model.BabListResponse;
import com.khozin.pembelajarankaidah.data.model.ChapterProgressResponse;
import com.khozin.pembelajarankaidah.data.remote.ApiService;
import com.khozin.pembelajarankaidah.database.AppDatabase;
import com.khozin.pembelajarankaidah.database.dao.BabDao;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository untuk Bab/Chapter data
 * Menghandle operasi data antara local database dan API
 */
public class BabRepository {

    private final BabDao babDao;
    private final ApiService apiService;
    private final Executor executor;

    // LiveData untuk result
    private final MutableLiveData<List<Bab>> chaptersLiveData = new MutableLiveData<>();
    private final MutableLiveData<ChapterProgressResponse> progressOverviewLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public BabRepository(Context context, ApiService apiService) {
        AppDatabase database = AppDatabase.getDatabase(context);
        this.babDao = database.babDao();
        this.apiService = apiService;
        this.executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Get all chapters from local database
     */
    public LiveData<List<Bab>> getAllChapters() {
        return babDao.getAllBabs();
    }

    /**
     * Get active chapters from local database
     */
    public LiveData<List<Bab>> getActiveChapters() {
        return babDao.getActiveBabs();
    }

    /**
     * Get chapters with progress from local database
     */
    public LiveData<List<Bab>> getChaptersWithProgress() {
        return babDao.getBabsWithProgress();
    }

    /**
     * Get unlocked chapters from local database
     */
    public LiveData<List<Bab>> getUnlockedChapters() {
        return babDao.getUnlockedBabs();
    }

    /**
     * Get chapter by ID
     */
    public LiveData<Bab> getChapterById(int chapterId) {
        return babDao.getBabByIdLiveData(chapterId);
    }

    /**
     * Load chapters from API
     */
    public void loadChaptersFromApi() {
        loadingLiveData.setValue(true);

        apiService.getChapters().enqueue(new Callback<BabListResponse>() {
            @Override
            public void onResponse(Call<BabListResponse> call, Response<BabListResponse> response) {
                loadingLiveData.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    BabListResponse babResponse = response.body();
                    if (babResponse.isSuccess()) {
                        List<Bab> chapters = babResponse.getData();
                        chaptersLiveData.setValue(chapters);

                        // Save to local database in background
                        saveChaptersToLocal(chapters);
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
    public void loadProgressOverview() {
        loadingLiveData.setValue(true);

        apiService.getProgressOverview().enqueue(new Callback<ChapterProgressResponse>() {
            @Override
            public void onResponse(Call<ChapterProgressResponse> call, Response<ChapterProgressResponse> response) {
                loadingLiveData.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    ChapterProgressResponse progressResponse = response.body();
                    if (progressResponse.isSuccess()) {
                        progressOverviewLiveData.setValue(progressResponse);

                        // Update local database with progress info
                        updateLocalProgress(progressResponse);
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
     * Save chapters to local database
     */
    private void saveChaptersToLocal(List<Bab> chapters) {
        executor.execute(() -> {
            try {
                // Clear existing chapters first
                babDao.deleteAllBabs();

                // Insert new chapters
                babDao.insertAll(chapters);
            } catch (Exception e) {
                errorLiveData.postValue("Error saving chapters: " + e.getMessage());
            }
        });
    }

    /**
     * Update local progress information
     */
    private void updateLocalProgress(ChapterProgressResponse progressResponse) {
        executor.execute(() -> {
            try {
                if (progressResponse.getData() != null &&
                    progressResponse.getData().getChapters() != null) {

                    for (Bab chapter : progressResponse.getData().getChapters()) {
                        babDao.updateProgressInfo(
                                chapter.getIdBab(),
                                chapter.getTotalMateri(),
                                chapter.getCompletedMateri(),
                                chapter.getInProgressMateri(),
                                chapter.getNotStartedMateri(),
                                chapter.getProgressPercentage(),
                                chapter.getStatusColor(),
                                chapter.getNextAction()
                        );

                        babDao.updateUnlockStatus(chapter.getIdBab(), chapter.isUnlocked());
                    }
                }
            } catch (Exception e) {
                errorLiveData.postValue("Error updating progress: " + e.getMessage());
            }
        });
    }

    /**
     * Get chapter by ID (synchronous)
     */
    public Bab getChapterByIdSync(int chapterId) {
        return babDao.getBabById(chapterId);
    }

    /**
     * Get active chapters (synchronous)
     */
    public List<Bab> getActiveChaptersSync() {
        return babDao.getActiveBabsSync();
    }

    /**
     * Search chapters by name
     */
    public List<Bab> searchChapters(String query) {
        return babDao.searchBabsSync(query);
    }

    /**
     * Update chapter progress
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

                babDao.updateProgressInfo(
                        chapterId, totalMateri, completedMateri, inProgressMateri,
                        notStartedMateri, progressPercentage, statusColor, nextAction
                );

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
                List<Bab> chapters = babDao.getActiveBabsSync();
                Bab nextChapterToUnlock = null;

                for (int i = 0; i < chapters.size() - 1; i++) {
                    Bab currentChapter = chapters.get(i);
                    Bab nextChapter = chapters.get(i + 1);

                    // If current chapter is completed and next chapter is locked
                    if (currentChapter.getProgressPercentage() >= 100 &&
                        !nextChapter.isUnlocked()) {
                        nextChapterToUnlock = nextChapter;
                        break;
                    }
                }

                if (nextChapterToUnlock != null) {
                    babDao.updateUnlockStatus(nextChapterToUnlock.getIdBab(), true);
                }
            } catch (Exception e) {
                errorLiveData.postValue("Error unlocking chapter: " + e.getMessage());
            }
        });
    }

    /**
     * Refresh chapters from API
     */
    public void refreshChapters() {
        loadChaptersFromApi();
        loadProgressOverview();
    }

    /**
     * Get statistics
     */
    public BabDao.BabStatistics getBabStatistics() {
        return babDao.getBabStatistics();
    }

    /**
     * Get chapters for synchronization
     */
    public List<Bab> getChaptersForSync(String lastSync) {
        return babDao.getBabsForSync(lastSync);
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