package com.khozin.pembelajarankaidah.database.dao;

import androidx.room.*;
import androidx.lifecycle.LiveData;
import java.util.List;

import com.khozin.pembelajarankaidah.data.model.Bab;

/**
 * Data Access Object untuk Bab entity
 * Menyediakan semua operasi database untuk Bab/Chapter
 */
@Dao
public interface BabDao {

    /**
     * Insert single bab
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Bab bab);

    /**
     * Insert multiple bab
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Bab> babList);

    /**
     * Update bab
     */
    @Update
    int update(Bab bab);

    /**
     * Delete bab
     */
    @Delete
    int delete(Bab bab);

    /**
     * Get all bab (sorted by urutan)
     */
    @Query("SELECT * FROM bab ORDER BY urutan ASC")
    LiveData<List<Bab>> getAllBabs();

    /**
     * Get all bab (synchronous)
     */
    @Query("SELECT * FROM bab ORDER BY urutan ASC")
    List<Bab> getAllBabsSync();

    /**
     * Get bab by ID
     */
    @Query("SELECT * FROM bab WHERE id_bab = :idBab")
    Bab getBabById(int idBab);

    /**
     * Get bab by ID (LiveData)
     */
    @Query("SELECT * FROM bab WHERE id_bab = :idBab")
    LiveData<Bab> getBabByIdLiveData(int idBab);

    /**
     * Get active bab only
     */
    @Query("SELECT * FROM bab WHERE is_active = 1 ORDER BY urutan ASC")
    LiveData<List<Bab>> getActiveBabs();

    /**
     * Get active bab only (synchronous)
     */
    @Query("SELECT * FROM bab WHERE is_active = 1 ORDER BY urutan ASC")
    List<Bab> getActiveBabsSync();

    /**
     * Get unlocked bab
     */
    @Query("SELECT * FROM bab WHERE is_unlocked = 1 AND is_active = 1 ORDER BY urutan ASC")
    LiveData<List<Bab>> getUnlockedBabs();

    /**
     * Get bab by chapter code
     */
    @Query("SELECT * FROM bab WHERE chapter_code = :chapterCode LIMIT 1")
    Bab getBabByChapterCode(String chapterCode);

    /**
     * Get bab by urutan
     */
    @Query("SELECT * FROM bab WHERE urutan = :urutan AND is_active = 1 LIMIT 1")
    Bab getBabByUrutan(int urutan);

    /**
     * Get next bab (higher urutan)
     */
    @Query("SELECT * FROM bab WHERE urutan > :currentUrutan AND is_active = 1 ORDER BY urutan ASC LIMIT 1")
    Bab getNextBab(int currentUrutan);

    /**
     * Get previous bab (lower urutan)
     */
    @Query("SELECT * FROM bab WHERE urutan < :currentUrutan AND is_active = 1 ORDER BY urutan DESC LIMIT 1")
    Bab getPreviousBab(int currentUrutan);

    /**
     * Get total count of bab
     */
    @Query("SELECT COUNT(*) FROM bab")
    int getBabCount();

    /**
     * Get total count of active bab
     */
    @Query("SELECT COUNT(*) FROM bab WHERE is_active = 1")
    int getActiveBabCount();

    /**
     * Get total count of unlocked bab
     */
    @Query("SELECT COUNT(*) FROM bab WHERE is_unlocked = 1 AND is_active = 1")
    int getUnlockedBabCount();

    /**
     * Update unlock status
     */
    @Query("UPDATE bab SET is_unlocked = :isUnlocked WHERE id_bab = :idBab")
    int updateUnlockStatus(int idBab, boolean isUnlocked);

    /**
     * Update progress information
     */
    @Query("UPDATE bab SET " +
           "total_materi = :totalMateri, " +
           "completed_materi = :completedMateri, " +
           "in_progress_materi = :inProgressMateri, " +
           "not_started_materi = :notStartedMateri, " +
           "progress_percentage = :progressPercentage, " +
           "status_color = :statusColor, " +
           "next_action = :nextAction " +
           "WHERE id_bab = :idBab")
    int updateProgressInfo(int idBab, int totalMateri, int completedMateri, int inProgressMateri,
                           int notStartedMateri, int progressPercentage, String statusColor, String nextAction);

    /**
     * Get bab with progress statistics
     */
    @Query("SELECT " +
           "b.*, " +
           "CASE " +
           "  WHEN b.progress_percentage >= 100 THEN 'success' " +
           "  WHEN b.progress_percentage > 0 THEN 'warning' " +
           "  ELSE 'secondary' " +
           "END as computed_status_color, " +
           "CASE " +
           "  WHEN b.progress_percentage >= 100 THEN 'review' " +
           "  WHEN b.progress_percentage > 0 THEN 'continue' " +
           "  ELSE 'start' " +
           "END as computed_next_action " +
           "FROM bab b " +
           "WHERE b.is_active = 1 " +
           "ORDER BY b.urutan ASC")
    LiveData<List<Bab>> getBabsWithProgress();

    /**
     * Get bab with materi count (join with materi_kaidah)
     */
    @Query("SELECT " +
           "b.*, " +
           "COALESCE(mk.materi_count, 0) as materi_count " +
           "FROM bab b " +
           "LEFT JOIN (" +
           "  SELECT id_bab, COUNT(*) as materi_count " +
           "  FROM materi_kaidah " +
           "  GROUP BY id_bab" +
           ") mk ON b.id_bab = mk.id_bab " +
           "WHERE b.is_active = 1 " +
           "ORDER BY b.urutan ASC")
    LiveData<List<Bab>> getBabsWithMateriCount();

    /**
     * Search bab by nama
     */
    @Query("SELECT * FROM bab WHERE nama_bab LIKE '%' || :query || '%' AND is_active = 1 ORDER BY urutan ASC")
    LiveData<List<Bab>> searchBabs(String query);

    /**
     * Search bab by nama (synchronous)
     */
    @Query("SELECT * FROM bab WHERE nama_bab LIKE '%' || :query || '%' AND is_active = 1 ORDER BY urutan ASC")
    List<Bab> searchBabsSync(String query);

    /**
     * Get bab for synchronization (with timestamp)
     */
    @Query("SELECT * FROM bab WHERE waktu_diubah > :lastSync ORDER BY waktu_diubah ASC")
    List<Bab> getBabsForSync(String lastSync);

    /**
     * Delete all bab
     */
    @Query("DELETE FROM bab")
    int deleteAllBabs();

    /**
     * Delete bab by ID
     */
    @Query("DELETE FROM bab WHERE id_bab = :idBab")
    int deleteBabById(int idBab);

    /**
     * Check if bab exists
     */
    @Query("SELECT EXISTS(SELECT 1 FROM bab WHERE id_bab = :idBab)")
    boolean babExists(int idBab);

    /**
     * Get max urutan value
     */
    @Query("SELECT MAX(urutan) FROM bab WHERE is_active = 1")
    int getMaxUrutan();

    /**
     * Get bab with minimum progress
     */
    @Query("SELECT * FROM bab WHERE is_active = 1 AND is_unlocked = 1 ORDER BY progress_percentage ASC, urutan ASC LIMIT 1")
    Bab getBabWithMinProgress();

    /**
     * Get bab with maximum progress (but not completed)
     */
    @Query("SELECT * FROM bab WHERE is_active = 1 AND is_unlocked = 1 AND progress_percentage < 100 ORDER BY progress_percentage DESC, urutan ASC LIMIT 1")
    Bab getBabWithMaxProgressNotCompleted();

    /**
     * Update chapter code
     */
    @Query("UPDATE bab SET chapter_code = :chapterCode WHERE id_bab = :idBab")
    int updateChapterCode(int idBab, String chapterCode);

    /**
     * Get bab statistics for dashboard
     */
    @Query("SELECT " +
           "COUNT(*) as total_bab, " +
           "SUM(CASE WHEN is_active = 1 THEN 1 ELSE 0 END) as active_bab, " +
           "SUM(CASE WHEN is_unlocked = 1 THEN 1 ELSE 0 END) as unlocked_bab, " +
           "SUM(CASE WHEN progress_percentage >= 100 THEN 1 ELSE 0 END) as completed_bab, " +
           "AVG(progress_percentage) as avg_progress " +
           "FROM bab")
    BabStatistics getBabStatistics();

    /**
     * Inner class for statistics
     */
    class BabStatistics {
        @ColumnInfo(name = "total_bab")
        public int totalBab;

        @ColumnInfo(name = "active_bab")
        public int activeBab;

        @ColumnInfo(name = "unlocked_bab")
        public int unlockedBab;

        @ColumnInfo(name = "completed_bab")
        public int completedBab;

        @ColumnInfo(name = "avg_progress")
        public float avgProgress;
    }
}