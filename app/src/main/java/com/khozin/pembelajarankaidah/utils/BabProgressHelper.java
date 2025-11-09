package com.khozin.pembelajarankaidah.utils;

import android.content.Context;
import com.khozin.pembelajarankaidah.data.model.MateriKaidah;
import com.khozin.pembelajarankaidah.data.model.RiwayatBelajar;
import com.khozin.pembelajarankaidah.database.dao.RiwayatBelajarDao;
import com.khozin.pembelajarankaidah.database.AppDatabase;
import com.khozin.pembelajarankaidah.utils.SessionManager;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Helper class untuk mengelola progress pembelajaran bab
 */
public class BabProgressHelper {

    private final Context context;
    private final RiwayatBelajarDao riwayatDao;
    private final ExecutorService executorService;

    public BabProgressHelper(Context context) {
        this.context = context;
        this.riwayatDao = AppDatabase.getDatabase(context).riwayatBelajarDao();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Check apakah bab sudah selesai semua materinya
     */
    public void checkBabCompletion(int babId, List<MateriKaidah> materiList,
                                     BabCompletionCallback callback) {
        if (materiList == null || materiList.isEmpty()) {
            callback.onError("Tidak ada materi dalam bab ini");
            return;
        }

        executorService.execute(() -> {
            try {
                // Get siswa ID dari SessionManager
                SessionManager sessionManager = new SessionManager(context);
                if (!sessionManager.isLoggedIn()) {
                    callback.onError("User tidak login");
                    return;
                }

                int siswaId = sessionManager.getUserId();
                if (siswaId == -1) {
                    callback.onError("Invalid siswa ID");
                    return;
                }

                int totalMateri = materiList.size();
                int completedMateri = 0;

                for (MateriKaidah materi : materiList) {
                    RiwayatBelajar riwayat = riwayatDao.getTerakhirBySiswaAndMateri(siswaId, materi.getIdMateri());
                    if (riwayat != null && riwayat.getStatus().equals(RiwayatBelajar.STATUS_SELESAI)) {
                        completedMateri++;
                    }
                }

                boolean isCompleted = completedMateri == totalMateri;
                float completionPercentage = (float) completedMateri / totalMateri * 100;

                // Callback ke main thread
                callback.onProgressChecked(babId, totalMateri, completedMateri, completionPercentage, isCompleted);

            } catch (Exception e) {
                callback.onError("Error checking bab completion: " + e.getMessage());
            }
        });
    }

    /**
     * Update progress materi ketika selesai dibaca
     */
    public void markMateriCompleted(int siswaId, int materiId, MateriCompletionCallback callback) {
        executorService.execute(() -> {
            try {
                // Cek apakah riwayat sudah ada
                RiwayatBelajar existingRiwayat = riwayatDao.getBySiswaAndMateriSync(siswaId, materiId);

                if (existingRiwayat != null) {
                    // Update riwayat yang sudah ada
                    existingRiwayat.setStatus(RiwayatBelajar.STATUS_SELESAI);
                    existingRiwayat.setPersentasePenguasaan(100.0f);
                    existingRiwayat.updateAksesTerakhir();
                    riwayatDao.update(existingRiwayat);
                } else {
                    // Buat riwayat baru
                    RiwayatBelajar newRiwayat = new RiwayatBelajar(siswaId, materiId);
                    newRiwayat.setStatus(RiwayatBelajar.STATUS_SELESAI);
                    newRiwayat.setPersentasePenguasaan(100.0f);
                    riwayatDao.insert(newRiwayat);
                }

                // Callback ke main thread
                callback.onMateriCompleted(materiId);

            } catch (Exception e) {
                callback.onError("Error marking materi as completed: " + e.getMessage());
            }
        });
    }

    /**
     * Get next bab dari list materi (berdasarkan id_bab)
     */
    public MateriKaidah getNextBab(List<MateriKaidah> allMateri, MateriKaidah currentMateri) {
        if (allMateri == null || currentMateri == null) {
            return null;
        }

        // Cari materi dengan id_bab lebih tinggi
        MateriKaidah nextMateri = null;
        for (MateriKaidah materi : allMateri) {
            if (materi.getIdBab() > currentMateri.getIdBab()) {
                if (nextMateri == null || materi.getIdMateri() < nextMateri.getIdMateri()) {
                    nextMateri = materi;
                }
            }
        }

        return nextMateri;
    }

    /**
     * Get previous bab dari list materi
     */
    public MateriKaidah getPreviousBab(List<MateriKaidah> allMateri, MateriKaidah currentMateri) {
        if (allMateri == null || currentMateri == null) {
            return null;
        }

        // Cari materi dengan id_bab lebih rendah
        MateriKaidah prevMateri = null;
        for (int i = allMateri.size() - 1; i >= 0; i--) {
            MateriKaidah materi = allMateri.get(i);
            if (materi.getIdBab() < currentMateri.getIdBab()) {
                if (prevMateri == null || materi.getIdMateri() > prevMateri.getIdMateri()) {
                    prevMateri = materi;
                }
            }
        }

        return prevMateri;
    }

    /**
     * Check apakah materi sudah selesai
     */
    public boolean isMateriCompleted(int siswaId, int materiId) {
        try {
            RiwayatBelajar riwayat = riwayatDao.getBySiswaAndMateriSync(siswaId, materiId);
            return riwayat != null && riwayat.getStatus().equals(RiwayatBelajar.STATUS_SELESAI);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get progress percentage untuk bab
     */
    public void getBabProgress(int babId, List<MateriKaidah> materiList,
                              BabProgressCallback callback) {
        if (materiList == null || materiList.isEmpty()) {
            callback.onError("Tidak ada materi dalam bab ini");
            return;
        }

        executorService.execute(() -> {
            try {
                // Get siswa ID dari SessionManager
                SessionManager sessionManager = new SessionManager(context);
                if (!sessionManager.isLoggedIn()) {
                    callback.onError("User tidak login");
                    return;
                }

                int siswaId = sessionManager.getUserId();
                if (siswaId == -1) {
                    callback.onError("Invalid siswa ID");
                    return;
                }

                int totalMateri = materiList.size();
                int completedMateri = 0;

                for (MateriKaidah materi : materiList) {
                    RiwayatBelajar riwayat = riwayatDao.getTerakhirBySiswaAndMateri(siswaId, materi.getIdMateri());
                    if (riwayat != null && riwayat.getStatus().equals(RiwayatBelajar.STATUS_SELESAI)) {
                        completedMateri++;
                    }
                }

                float progress = totalMateri > 0 ? (float) completedMateri / totalMateri * 100 : 0f;

                // Callback ke main thread
                callback.onProgressCalculated(babId, progress, completedMateri, totalMateri);

            } catch (Exception e) {
                callback.onError("Error calculating bab progress: " + e.getMessage());
            }
        });
    }

    /**
     * Reset semua progress untuk testing
     */
    public void resetAllProgress(ResetProgressCallback callback) {
        executorService.execute(() -> {
            try {
                riwayatDao.deleteAll();
                callback.onProgressReset();
            } catch (Exception e) {
                callback.onError("Error resetting progress: " + e.getMessage());
            }
        });
    }

    /**
     * Interface callback untuk completion check
     */
    public interface BabCompletionCallback {
        void onProgressChecked(int babId, int totalMateri, int completedMateri,
                                  float completionPercentage, boolean isCompleted);
        void onError(String errorMessage);
    }

    /**
     * Interface callback untuk materi completion
     */
    public interface MateriCompletionCallback {
        void onMateriCompleted(int materiId);
        void onError(String errorMessage);
    }

    /**
     * Interface callback untuk progress calculation
     */
    public interface BabProgressCallback {
        void onProgressCalculated(int babId, float progress, int completedMateri, int totalMateri);
        void onError(String errorMessage);
    }

    /**
     * Interface callback untuk reset progress
     */
    public interface ResetProgressCallback {
        void onProgressReset();
        void onError(String errorMessage);
    }
}