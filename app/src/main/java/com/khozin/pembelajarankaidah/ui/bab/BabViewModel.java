package com.khozin.pembelajarankaidah.ui.bab;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.khozin.pembelajarankaidah.data.model.Bab;
import com.khozin.pembelajarankaidah.data.model.BabListResponse;
import com.khozin.pembelajarankaidah.data.model.ChapterProgressResponse;
import com.khozin.pembelajarankaidah.data.remote.ApiService;
import com.khozin.pembelajarankaidah.database.AppDatabase;
import com.khozin.pembelajarankaidah.database.dao.BabDao;
import com.khozin.pembelajarankaidah.utils.SessionManager;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ViewModel untuk Bab/Chapter management
 * Menghandle business logic untuk chapter-based learning system
 */
public class BabViewModel extends AndroidViewModel {

    private final BabDao babDao;
    private ApiService apiService;
    private final SessionManager sessionManager;
    private final Executor executor;

    // LiveData untuk UI
    private final MutableLiveData<List<Bab>> chaptersLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Bab>> unlockedChaptersLiveData = new MutableLiveData<>();
    private final MutableLiveData<Bab> currentChapterLiveData = new MutableLiveData<>();
    private final MutableLiveData<ChapterProgressResponse> progressOverviewLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> successLiveData = new MutableLiveData<>();

    // Local database LiveData
    private final LiveData<List<Bab>> allBabsLiveData;
    private final LiveData<List<Bab>> activeBabsLiveData;

    public BabViewModel(Application application) {
        super(application);

        AppDatabase database = AppDatabase.getDatabase(application);
        babDao = database.babDao();

        // API service would be injected via dependency injection in real app
        apiService = null; // Will be set via setter

        sessionManager = new SessionManager(application);
        executor = Executors.newSingleThreadExecutor();

        // Initialize local LiveData
        allBabsLiveData = babDao.getAllBabs();
        activeBabsLiveData = babDao.getActiveBabs();

        // Load initial data
        loadChaptersFromLocal();
    }

    /**
     * Set API service (for dependency injection)
     */
    public void setApiService(ApiService apiService) {
        this.apiService = apiService;
    }

    /**
     * Load chapters from local database
     */
    public void loadChaptersFromLocal() {
        executor.execute(() -> {
            try {
                List<Bab> chapters = babDao.getActiveBabsSync();
                chaptersLiveData.postValue(chapters);

                // Get unlocked chapters
                List<Bab> unlockedChapters = babDao.getBabsWithProgress().getValue();
                if (unlockedChapters != null) {
                    unlockedChaptersLiveData.postValue(unlockedChapters);
                }
            } catch (Exception e) {
                errorLiveData.postValue("Error loading chapters: " + e.getMessage());
            }
        });
    }

    /**
     * Load chapters from API
     */
    public void loadChaptersFromApi() {
        if (apiService == null) {
            errorLiveData.setValue("API service not available");
            return;
        }

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

                        // Save to local database
                        saveChaptersToLocal(chapters);

                        successLiveData.setValue("Chapters loaded successfully");
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
        if (apiService == null) {
            errorLiveData.setValue("API service not available");
            return;
        }

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

                        successLiveData.setValue("Progress overview loaded successfully");
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
                // Clear existing chapters
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
     * Get chapter by ID
     */
    public void getChapterById(int chapterId) {
        executor.execute(() -> {
            try {
                Bab chapter = babDao.getBabById(chapterId);
                if (chapter != null) {
                    currentChapterLiveData.postValue(chapter);
                } else {
                    errorLiveData.postValue("Chapter not found");
                }
            } catch (Exception e) {
                errorLiveData.postValue("Error getting chapter: " + e.getMessage());
            }
        });
    }

    /**
     * Unlock next chapter based on progress
     */
    public void unlockNextChapter() {
        executor.execute(() -> {
            try {
                // Find the next chapter to unlock
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
                    successLiveData.postValue("Chapter " + nextChapterToUnlock.getNamaBab() + " unlocked!");

                    // Reload chapters
                    loadChaptersFromLocal();
                } else {
                    errorLiveData.postValue("No chapters to unlock");
                }
            } catch (Exception e) {
                errorLiveData.postValue("Error unlocking chapter: " + e.getMessage());
            }
        });
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
                unlockNextChapter();

            } catch (Exception e) {
                errorLiveData.postValue("Error updating progress: " + e.getMessage());
            }
        });
    }

    /**
     * Search chapters by name
     */
    public void searchChapters(String query) {
        executor.execute(() -> {
            try {
                List<Bab> results = babDao.searchBabsSync(query);
                chaptersLiveData.postValue(results);
            } catch (Exception e) {
                errorLiveData.postValue("Error searching chapters: " + e.getMessage());
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

    // Getters for LiveData
    public LiveData<List<Bab>> getChaptersLiveData() {
        return chaptersLiveData;
    }

    public LiveData<List<Bab>> getUnlockedChaptersLiveData() {
        return unlockedChaptersLiveData;
    }

    public LiveData<Bab> getCurrentChapterLiveData() {
        return currentChapterLiveData;
    }

    public LiveData<ChapterProgressResponse> getProgressOverviewLiveData() {
        return progressOverviewLiveData;
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<String> getSuccessLiveData() {
        return successLiveData;
    }

    public LiveData<List<Bab>> getAllBabsLiveData() {
        return allBabsLiveData;
    }

    public LiveData<List<Bab>> getActiveBabsLiveData() {
        return activeBabsLiveData;
    }

    /**
     * Clear error message
     */
    public void clearError() {
        errorLiveData.setValue(null);
    }

    /**
     * Clear success message
     */
    public void clearSuccess() {
        successLiveData.setValue(null);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Cleanup if needed
    }
}