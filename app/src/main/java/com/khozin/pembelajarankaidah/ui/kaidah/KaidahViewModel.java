package com.khozin.pembelajarankaidah.ui.kaidah;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.khozin.pembelajarankaidah.data.model.MateriKaidah;
import java.util.List;

/**
 * ViewModel untuk mengelola data Kaidah
 * Memisahkan logic dari UI layer
 */
public class KaidahViewModel extends ViewModel {

    private MutableLiveData<List<MateriKaidah>> kaidahList = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<List<MateriKaidah>> getKaidahList() {
        return kaidahList;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Set kaidah list data
     */
    public void setKaidahList(List<MateriKaidah> kaidahList) {
        this.kaidahList.setValue(kaidahList);
    }

    /**
     * Set loading state
     */
    public void setLoading(boolean loading) {
        this.isLoading.setValue(loading);
    }

    /**
     * Set error message
     */
    public void setErrorMessage(String message) {
        this.errorMessage.setValue(message);
    }

    /**
     * Clear error message
     */
    public void clearErrorMessage() {
        this.errorMessage.setValue(null);
    }

    /**
     * Refresh data
     */
    public void refresh() {
        // This will trigger data reload
        setLoading(true);
        clearErrorMessage();
    }

    /**
     * Handle API errors
     */
    public void handleError(String error) {
        setErrorMessage(error);
        setLoading(false);
    }

    /**
     * Handle success
     */
    public void handleSuccess(List<MateriKaidah> data) {
        setKaidahList(data);
        setLoading(false);
        clearErrorMessage();
    }
}