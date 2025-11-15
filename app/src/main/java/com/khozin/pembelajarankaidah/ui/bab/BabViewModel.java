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
import com.khozin.pembelajarankaidah.utils.SessionManager;

import java.util.List;
import java.util.ArrayList;
import java.util.ArrayList;
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

    public BabViewModel(Application application) {
        super(application);

        // API service would be injected via dependency injection in real app
        apiService = null; // Will be set via setter

        sessionManager = new SessionManager(application);
        executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Set API service (for dependency injection)
     */
    public void setApiService(ApiService apiService) {
        this.apiService = apiService;
    }

    /**
     * Load chapters from cache (API-only approach)
     */
    public void loadChaptersFromCache() {
        // Chapters are loaded from API and cached in repository
        // This method can be used to trigger repository data loading
        if (apiService != null) {
            loadChaptersFromApi();
        } else {
            errorLiveData.setValue("API service not available");
        }
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

        String authToken = sessionManager.getAuthToken();
        apiService.getChapters("Bearer " + authToken).enqueue(new Callback<BabListResponse>() {
            @Override
            public void onResponse(Call<BabListResponse> call, Response<BabListResponse> response) {
                loadingLiveData.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    BabListResponse babResponse = response.body();
                    if (babResponse.isSuccess()) {
                        List<Bab> chapters = babResponse.getData().getChapters();
                        chaptersLiveData.setValue(chapters);

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

        String authToken = sessionManager.getAuthToken();
        apiService.getProgressOverview("Bearer " + authToken).enqueue(new Callback<ChapterProgressResponse>() {
            @Override
            public void onResponse(Call<ChapterProgressResponse> call, Response<ChapterProgressResponse> response) {
                loadingLiveData.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    ChapterProgressResponse progressResponse = response.body();
                    if (progressResponse.isSuccess()) {
                        progressOverviewLiveData.setValue(progressResponse);

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
     * Get chapter by ID from LiveData
     */
    public void getChapterById(int chapterId) {
        List<Bab> chapters = chaptersLiveData.getValue();
        if (chapters != null) {
            for (Bab chapter : chapters) {
                if (chapter.getIdBab() == chapterId) {
                    currentChapterLiveData.setValue(chapter);
                    return;
                }
            }
        }
        errorLiveData.setValue("Chapter not found");
    }

    /**
     * Search chapters by name in current LiveData
     */
    public void searchChapters(String query) {
        List<Bab> currentChapters = chaptersLiveData.getValue();
        if (currentChapters == null || query == null || query.trim().isEmpty()) {
            chaptersLiveData.setValue(currentChapters);
            return;
        }

        List<Bab> results = new ArrayList<>();
        String searchQuery = query.toLowerCase().trim();

        for (Bab chapter : currentChapters) {
            if (chapter.getNamaBab() != null &&
                chapter.getNamaBab().toLowerCase().contains(searchQuery)) {
                results.add(chapter);
            }
        }

        chaptersLiveData.setValue(results);
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