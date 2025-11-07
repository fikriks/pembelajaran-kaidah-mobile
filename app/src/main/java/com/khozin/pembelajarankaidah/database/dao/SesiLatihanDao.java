package com.khozin.pembelajarankaidah.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.khozin.pembelajarankaidah.data.model.SesiLatihan;

import java.util.List;

/**
 * DAO untuk SesiLatihan entity
 * Menghandle semua database operations untuk sesi latihan
 * Penting untuk LCM algorithm tracking
 */
@Dao
public interface SesiLatihanDao {

    /**
     * Insert sesi latihan baru
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(SesiLatihan sesiLatihan);

    /**
     * Insert multiple sesi latihan
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<SesiLatihan> sesiLatihanList);

    /**
     * Update sesi latihan
     */
    @Update
    int update(SesiLatihan sesiLatihan);

    /**
     * Delete sesi latihan
     */
    @Delete
    int delete(SesiLatihan sesiLatihan);

    /**
     * Get sesi latihan by ID
     */
    @Query("SELECT * FROM sesi_latihan WHERE id_sesi = :id")
    SesiLatihan getById(int id);

    /**
     * Get sesi latihan by ID (LiveData)
     */
    @Query("SELECT * FROM sesi_latihan WHERE id_sesi = :id")
    LiveData<SesiLatihan> getByIdLive(int id);

    /**
     * Get sesi latihan by siswa ID
     */
    @Query("SELECT * FROM sesi_latihan WHERE id_siswa = :siswaId ORDER BY id_sesi DESC")
    LiveData<List<SesiLatihan>> getBySiswaId(int siswaId);

    /**
     * Get sesi latihan by siswa ID (sync)
     */
    @Query("SELECT * FROM sesi_latihan WHERE id_siswa = :siswaId ORDER BY id_sesi DESC")
    List<SesiLatihan> getBySiswaIdSync(int siswaId);

    /**
     * Get sesi latihan by materi ID
     */
    @Query("SELECT * FROM sesi_latihan WHERE id_materi = :materiId ORDER BY id_sesi DESC")
    LiveData<List<SesiLatihan>> getByMateriId(int materiId);

    /**
     * Get sesi latihan by siswa dan materi
     */
    @Query("SELECT * FROM sesi_latihan WHERE id_siswa = :siswaId AND id_materi = :materiId ORDER BY id_sesi DESC")
    LiveData<List<SesiLatihan>> getBySiswaAndMateri(int siswaId, int materiId);

    /**
     * Get sesi latihan yang sedang berjalan
     */
    @Query("SELECT * FROM sesi_latihan WHERE status = 'sedang_berjalan' AND id_siswa = :siswaId ORDER BY id_sesi DESC")
    LiveData<List<SesiLatihan>> getSesiSedangBerjalan(int siswaId);

    /**
     * Get sesi latihan yang sudah selesai
     */
    @Query("SELECT * FROM sesi_latihan WHERE status = 'selesai' AND id_siswa = :siswaId ORDER BY id_sesi DESC")
    LiveData<List<SesiLatihan>> getSesiSelesai(int siswaId);

    /**
     * Get sesi latihan aktif (belum selesai) untuk siswa
     */
    @Query("SELECT * FROM sesi_latihan WHERE id_siswa = :siswaId AND status = 'sedang_berjalan' LIMIT 1")
    SesiLatihan getSesiAktif(int siswaId);

    /**
     * Get sesi latihan by seed
     */
    @Query("SELECT * FROM sesi_latihan WHERE seed_digunakan = :seed ORDER BY id_sesi DESC")
    LiveData<List<SesiLatihan>> getBySeed(long seed);

    /**
     * Check apakah seed sudah digunakan
     */
    @Query("SELECT EXISTS(SELECT 1 FROM sesi_latihan WHERE seed_digunakan = :seed)")
    boolean isSeedExists(long seed);

    /**
     * Get sesi latihan terakhir untuk siswa dan materi
     */
    @Query("SELECT * FROM sesi_latihan WHERE id_siswa = :siswaId AND id_materi = :materiId ORDER BY id_sesi DESC LIMIT 1")
    SesiLatihan getSesiTerakhir(int siswaId, int materiId);

    /**
     * Get total sesi untuk siswa
     */
    @Query("SELECT COUNT(*) FROM sesi_latihan WHERE id_siswa = :siswaId")
    int getTotalSesiBySiswa(int siswaId);

    /**
     * Get total sesi selesai untuk siswa
     */
    @Query("SELECT COUNT(*) FROM sesi_latihan WHERE id_siswa = :siswaId AND status = 'selesai'")
    int getTotalSesiSelesai(int siswaId);

    /**
     * Get total sesi untuk materi
     */
    @Query("SELECT COUNT(*) FROM sesi_latihan WHERE id_materi = :materiId")
    int getTotalSesiByMateri(int materiId);

    /**
     * Update status sesi
     */
    @Query("UPDATE sesi_latihan SET status = :status WHERE id_sesi = :id")
    int updateStatus(int id, String status);

    /**
     * Update progress sesi
     */
    @Query("UPDATE sesi_latihan SET soal_benar = :soalBenar, skor = :skor, jumlah_soal_dijawab = :jumlahDijawab WHERE id_sesi = :id")
    int updateProgress(int id, int soalBenar, float skor, int jumlahDijawab);

    /**
     * Increment soal benar
     */
    @Query("UPDATE sesi_latihan SET soal_benar = soal_benar + 1, jumlah_soal_dijawab = jumlah_soal_dijawab + 1 WHERE id_sesi = :id")
    int incrementSoalBenar(int id);

    /**
     * Increment jumlah soal dijawab
     */
    @Query("UPDATE sesi_latihan SET jumlah_soal_dijawab = jumlah_soal_dijawab + 1 WHERE id_sesi = :id")
    int incrementJumlahDijawab(int id);

    /**
     * Selesaikan sesi (update waktu selesai, status, dll)
     */
    @Query("UPDATE sesi_latihan SET " +
            "status = 'selesai', " +
            "waktu_selesai = :waktuSelesai, " +
            "durasi_detik = :durasiDetik, " +
            "skor = :skor " +
            "WHERE id_sesi = :id")
    int selesaikanSesi(int id, String waktuSelesai, Integer durasiDetik, float skor);

    /**
     * Update current question index
     */
    @Query("UPDATE sesi_latihan SET current_question_index = :currentIndex WHERE id_sesi = :id")
    int updateCurrentQuestionIndex(int id, int currentIndex);

    /**
     * Set pause state
     */
    @Query("UPDATE sesi_latihan SET is_paused = :isPaused, last_pause_time = :pauseTime WHERE id_sesi = :id")
    int updatePauseState(int id, boolean isPaused, long pauseTime);

    /**
     * Get sesi latihan untuk statistics
     */
    @Query("SELECT " +
            "COUNT(*) as total_sesi, " +
            "COUNT(CASE WHEN status = 'selesai' THEN 1 END) as selesai, " +
            "AVG(skor) as rata_rata_skor, " +
            "MAX(skor) as skor_tertinggi, " +
            "SUM(durasi_detik) as total_waktu " +
            "FROM sesi_latihan WHERE id_siswa = :siswaId")
    Object[] getSesiStatistics(int siswaId);

    /**
     * Get sesi latihan by date range
     */
    @Query("SELECT * FROM sesi_latihan WHERE id_siswa = :siswaId AND waktu_mulai BETWEEN :startDate AND :endDate ORDER BY waktu_mulai DESC")
    List<SesiLatihan> getSesiByDateRange(int siswaId, long startDate, long endDate);

    /**
     * Get recent sesi untuk dashboard
     */
    @Query("SELECT * FROM sesi_latihan WHERE id_siswa = :siswaId AND status = 'selesai' ORDER BY waktu_selesai DESC LIMIT :limit")
    LiveData<List<SesiLatihan>> getRecentSesi(int siswaId, int limit);

    /**
     * Get sesi latihan dengan rata-rata skor
     */
    @Query("SELECT mk.judul_kaidah, " +
            "COUNT(sl.id_sesi) as total_sesi, " +
            "AVG(sl.skor) as rata_rata_skor, " +
            "MAX(sl.skor) as skor_tertinggi " +
            "FROM sesi_latihan sl " +
            "INNER JOIN materi_kaidah mk ON sl.id_materi = mk.id_materi " +
            "WHERE sl.id_siswa = :siswaId AND sl.status = 'selesai' " +
            "GROUP BY sl.id_materi, mk.judul_kaidah " +
            "ORDER BY rata_rata_skor DESC")
    List<Object[]> getPerformaPerMateri(int siswaId);

    /**
     * Get sesi latihan yang perlu diselesaikan (timeout cleanup)
     */
    @Query("SELECT * FROM sesi_latihan WHERE status = 'sedang_berjalan' AND waktu_mulai < :timeoutThreshold")
    List<SesiLatihan> getSesiTimeout(long timeoutThreshold);

    /**
     * Delete all sesi
     */
    @Query("DELETE FROM sesi_latihan")
    int deleteAll();

    /**
     * Delete sesi by ID
     */
    @Query("DELETE FROM sesi_latihan WHERE id_sesi = :id")
    int deleteById(int id);

    /**
     * Delete sesi lama (cleanup)
     */
    @Query("DELETE FROM sesi_latihan WHERE waktu_mulai < :cutoffTime AND status = 'selesai'")
    int deleteOldSessions(long cutoffTime);

    /**
     * Get sesi untuk export
     */
    @Query("SELECT * FROM sesi_latihan ORDER BY id_sesi ASC")
    List<SesiLatihan> getSesiForExport();

    /**
     * Batch insert dari API
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAllFromApi(List<SesiLatihan> sesiList);

    /**
     * Get sesi dengan materi info
     */
    @Query("SELECT sl.*, mk.judul_kaidah as materi_judul FROM sesi_latihan sl " +
            "INNER JOIN materi_kaidah mk ON sl.id_materi = mk.id_materi " +
            "WHERE sl.id_siswa = :siswaId ORDER BY sl.id_sesi DESC")
    List<SesiLatihan> getSesiWithMateriInfo(int siswaId);

    /**
     * Get learning streak (consecutive days with completed sessions)
     */
    @Query("SELECT COUNT(DISTINCT DATE(waktu_selesai/1000, 'unixepoch')) FROM sesi_latihan " +
            "WHERE id_siswa = :siswaId AND status = 'selesai' AND waktu_selesai >= :sinceDate")
    int getLearningStreak(int siswaId, long sinceDate);

    /**
     * Get sesi hari ini
     */
    @Query("SELECT * FROM sesi_latihan WHERE id_siswa = :siswaId AND DATE(waktu_mulai/1000, 'unixepoch') = DATE('now', 'unixepoch') ORDER BY waktu_mulai ASC")
    List<SesiLatihan> getSesiHariIni(int siswaId);

    /**
     * Get sesi terbaik untuk siswa
     */
    @Query("SELECT * FROM sesi_latihan WHERE id_siswa = :siswaId AND status = 'selesai' ORDER BY skor DESC LIMIT :limit")
    LiveData<List<SesiLatihan>> getSesiTerbaik(int siswaId, int limit);

    /**
     * Get LCM seed statistics untuk analysis
     */
    @Query("SELECT " +
            "COUNT(*) as total_sesi, " +
            "COUNT(DISTINCT seed_digunakan) as unique_seeds, " +
            "MAX(seed_digunakan) as max_seed, " +
            "MIN(seed_digunakan) as min_seed " +
            "FROM sesi_latihan WHERE id_siswa = :siswaId")
    Object[] getSeedStatistics(int siswaId);
}