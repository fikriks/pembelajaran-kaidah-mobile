package com.khozin.pembelajarankaidah.ui.quiz;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * ViewModel untuk mengelola state Quiz
 * Memisahkan logic dari UI layer
 */
public class QuizViewModel extends ViewModel {

    private MutableLiveData<Integer> selectedJawabanId = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Integer> currentQuestionIndex = new MutableLiveData<>();
    private MutableLiveData<Integer> correctAnswers = new MutableLiveData<>();
    private MutableLiveData<Integer> totalQuestions = new MutableLiveData<>();
    private MutableLiveData<Long> timeLeft = new MutableLiveData<>();

    public LiveData<Integer> getSelectedJawabanId() {
        return selectedJawabanId;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Integer> getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public LiveData<Integer> getCorrectAnswers() {
        return correctAnswers;
    }

    public LiveData<Integer> getTotalQuestions() {
        return totalQuestions;
    }

    public LiveData<Long> getTimeLeft() {
        return timeLeft;
    }

    /**
     * Set selected jawaban
     */
    public void setSelectedJawabanId(int jawabanId) {
        this.selectedJawabanId.setValue(jawabanId);
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
     * Set current question index
     */
    public void setCurrentQuestionIndex(int index) {
        this.currentQuestionIndex.setValue(index);
    }

    /**
     * Set correct answers count
     */
    public void setCorrectAnswers(int count) {
        this.correctAnswers.setValue(count);
    }

    /**
     * Increment correct answers
     */
    public void incrementCorrectAnswers() {
        Integer current = correctAnswers.getValue();
        if (current == null) {
            current = 0;
        }
        correctAnswers.setValue(current + 1);
    }

    /**
     * Set total questions
     */
    public void setTotalQuestions(int total) {
        this.totalQuestions.setValue(total);
    }

    /**
     * Set time left
     */
    public void setTimeLeft(long timeLeft) {
        this.timeLeft.setValue(timeLeft);
    }

    /**
     * Calculate current score percentage
     */
    public float getCurrentScorePercentage() {
        Integer correct = correctAnswers.getValue();
        Integer total = totalQuestions.getValue();

        if (correct == null || total == null || total == 0) {
            return 0.0f;
        }

        return (correct * 100.0f) / total;
    }

    /**
     * Check if quiz is finished
     */
    public boolean isQuizFinished() {
        Integer current = currentQuestionIndex.getValue();
        Integer total = totalQuestions.getValue();

        return current != null && total != null && current >= total - 1;
    }

    /**
     * Reset all state
     */
    public void reset() {
        selectedJawabanId.setValue(-1);
        isLoading.setValue(false);
        errorMessage.setValue(null);
        currentQuestionIndex.setValue(0);
        correctAnswers.setValue(0);
        totalQuestions.setValue(0);
        timeLeft.setValue(600000L); // 10 minutes
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
    public void handleSuccess() {
        setLoading(false);
        clearErrorMessage();
    }
}