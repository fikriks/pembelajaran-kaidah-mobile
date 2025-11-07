package com.khozin.pembelajarankaidah.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.khozin.pembelajarankaidah.data.model.RiwayatBelajar;

import java.util.List;

/**
 * DAO untuk RiwayatBelajar entity
 * Menghandle semua database operations untuk riwayat pembelajaran siswa
 */
@Dao
public interface RiwayatBelajarDao {

    /**
     * Insert riwayat belajar baru
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(RiwayatBelajar riwayat);

    /**
     * Insert multiple riwayat belajar
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<RiwayatBelajar> riwayatList);

    /**
     * Update riwayat belajar
     */
    @Update
    int update(RiwayatBelajar riwayat);

    /**
     * Delete riwayat belajar
     */
    @Delete
    int delete(RiwayatBelajar riwayat);

    /**
     * Get riwayat belajar by ID
     */
    @Query("SELECT * FROM riwayat_belajar WHERE id_riwayat = :id")
    RiwayatBelajar getById(int id);

    /**
     * Get riwayat belajar by ID (LiveData)
     */
    @Query("SELECT * FROM riwayat_belajar WHERE id_riwayat = :id")
    LiveData<RiwayatBelajar> getByIdLive(int id);

    /**
     * Get all riwayat belajar
     */
    @Query("SELECT * FROM riwayat_belajar ORDER BY waktu_diubah DESC")
    LiveData<List<RiwayatBelajar>> getAllRiwayat();

    /**
     * Get all riwayat belajar (sync)
     */
    @Query("SELECT * FROM riwayat_belajar ORDER BY waktu_diubah DESC")
    List<RiwayatBelajar> getAllRiwayatSync();

    /**
     * Get riwayat belajar by siswa ID
     */
    @Query("SELECT * FROM riwayat_belajar WHERE id_siswa = :siswaId ORDER BY waktu_diubah DESC")
    LiveData<List<RiwayatBelajar>> getBySiswaId(int siswaId);

    /**
     * Get riwayat belajar by siswa ID (sync)
     */
    @Query("SELECT * FROM riwayat_belajar WHERE id_siswa = :siswaId ORDER BY waktu_diubah DESC")
    List<RiwayatBelajar> getBySiswaIdSync(int siswaId);

    /**
     * Get riwayat belajar by materi ID
     */
    @Query("SELECT * FROM riwayat_belajar WHERE id_materi = :materiId ORDER BY waktu_diubah DESC")
    LiveData<List<RiwayatBelajar>> getByMateriId(int materiId);

    /**
     * Get riwayat belajar by siswa dan materi
     */
    @Query("SELECT * FROM riwayat_belajar WHERE id_siswa = :siswaId AND id_materi = :materiId ORDER BY waktu_diubah DESC")
    LiveData<RiwayatBelajar> getBySiswaAndMateri(int siswaId, int materiId);

    /**
     * Get riwayat belajar terakhir untuk siswa dan materi
     */
    @Query("SELECT * FROM riwayat_belajar WHERE id_siswa = :siswaId AND id_materi = :materiId ORDER BY waktu_diubah DESC LIMIT 1")
    RiwayatBelajar getTerakhirBySiswaAndMateri(int siswaId, int materiId);

    /**
     * Get riwayat belajar by status
     */
    @Query("SELECT * FROM riwayat_belajar WHERE status = :status ORDER BY waktu_diubah DESC")
    LiveData<List<RiwayatBelajar>> getByStatus(String status);

    /**
     * Get riwayat belajar yang sudah selesai
     */
    @Query("SELECT * FROM riwayat_belajar WHERE status = 'selesai' ORDER BY waktu_diubah DESC")
    LiveData<List<RiwayatBelajar>> getSelesai();

    /**
     * Get riwayat belajar yang sedang berjalan
     */
    @Query("SELECT * FROM riwayat_belajar WHERE status = 'sedang_belajar' ORDER BY waktu_diubah DESC")
    LiveData<List<RiwayatBelajar>> getSedangBelajar();

    /**
     * Get riwayat belajar yang belum dimulai
     */
    @Query("SELECT * FROM riwayat_belajar WHERE status = 'belum_dimulai' ORDER BY waktu_diubah DESC")
    LiveData<List<RiwayatBelajar>> getBelumDimulai();

    /**
     * Get riwayat belajar yang perlu review (skor < 70%)
     */
    @Query("SELECT * FROM riwayat_belajar WHERE persentase_penguasaan < 70 AND status = 'selesai' ORDER BY persentase_penguasaan ASC")
    LiveData<List<RiwayatBelajar>> getPerluReview();

    /**
     * Get total count riwayat belajar
     */
    @Query("SELECT COUNT(*) FROM riwayat_belajar")
    int getTotalCount();

    /**
     * Get count by siswa ID
     */
    @Query("SELECT COUNT(*) FROM riwayat_belajar WHERE id_siswa = :siswaId")
    int getCountBySiswa(int siswaId);

    /**
     * Get count by materi ID
     */
    @Query("SELECT COUNT(*) FROM riwayat_belajar WHERE id_materi = :materiId")
    int getCountByMateri(int materiId);

    /**
     * Get count by status
     */
    @Query("SELECT COUNT(*) FROM riwayat_belajar WHERE status = :status")
    int getCountByStatus(String status);

    /**
     * Get count riwayat selesai
     */
    @Query("SELECT COUNT(*) FROM riwayat_belajar WHERE status = 'selesai'")
    int getCountSelesai();

    /**
     * Search riwayat belajar
     */
    @Query("SELECT * FROM riwayat_belajar WHERE materi_judul LIKE '%' || :query || '%' ORDER BY waktu_diubah DESC")
    LiveData<List<RiwayatBelajar>> search(String query);

    /**
     * Get riwayat belajar by persentase range
     */
    @Query("SELECT * FROM riwayat_belajar WHERE persentase_penguasaan BETWEEN :min AND :max ORDER BY persentase_penguasaan DESC")
    LiveData<List<RiwayatBelajar>> getByPersentaseRange(float min, float max);

    /**
     * Get riwayat belajar by date range
     */
    @Query("SELECT * FROM riwayat_belajar WHERE waktu_diubah BETWEEN :startDate AND :endDate ORDER BY waktu_diubah DESC")
    List<RiwayatBelajar> getByDateRange(long startDate, long endDate);

    /**
     * Update persentase penguasaan
     */
    @Query("UPDATE riwayat_belajar SET persentase_penguasaan = :persentase, status = :status WHERE id_riwayat = :id")
    int updatePersentase(int id, float persentase, String status);

    /**
     * Update status belajar
     */
    @Query("UPDATE riwayat_belajar SET status = :status WHERE id_riwayat = :id")
    int updateStatus(int id, String status);

    /**
     * Update materi judul
     */
    @Query("UPDATE riwayat_belajar SET materi_judul = :judul WHERE id_riwayat = :id")
    int updateMateriJudul(int id, String judul);

    /**
     * Update streak hari
     */
    @Query("UPDATE riwayat_belajar SET streak_hari = :streak WHERE id_riwayat = :id")
    int updateStreakHari(int id, int streak);

    /**
     * Update total sesi dan rata-rata skor
     */
    @Query("UPDATE riwayat_belajar SET total_sesi_diikuti = :totalSesi, rata_rata_skor = :rataSkor WHERE id_riwayat = :id")
    int updateProgress(int id, int totalSesi, float rataSkor);

    /**
     * Increment progress
     */
    @Query("UPDATE riwayat_belajar SET persentase_penguasaan = persentase_penguasaan + :increment, status = CASE WHEN (persentase_penguasaan + :increment) >= 100 THEN 'selesai' ELSE status END WHERE id_riwayat = :id")
    int incrementProgress(int id, float increment);

    /**
     * Update waktu akses terakhir
     */
    @Query("UPDATE riwayat_belajar SET waktu_akses_terakhir = :waktu WHERE id_riwayat = :id")
    int updateWaktuAkses(int id, String waktu);

    /**
     * Update tanggal selesai terakhir
     */
    @Query("UPDATE riwayat_belajar SET tanggal_selesai_terakhir = :tanggal WHERE id_riwayat = :id")
    int updateTanggalSelesai(int id, String tanggal);

    /**
     * Delete riwayat belajar by ID
     */
    @Query("DELETE FROM riwayat_belajar WHERE id_riwayat = :id")
    int deleteById(int id);

    /**
     * Delete riwayat belajar by siswa ID
     */
    @Query("DELETE FROM riwayat_belajar WHERE id_siswa = :siswaId")
    int deleteBySiswaId(int siswaId);

    /**
     * Delete riwayat belajar by materi ID
     */
    @Query("DELETE FROM riwayat_belajar WHERE id_materi = :materiId")
    int deleteByMateriId(int materiId);

    /**
     * Delete all riwayat belajar
     */
    @Query("DELETE FROM riwayat_belajar")
    int deleteAll();

    /**
     * Check riwayat belajar exists
     */
    @Query("SELECT EXISTS(SELECT 1 FROM riwayat_belajar WHERE id_riwayat = :id)")
    boolean isRiwayatExists(int id);

    /**
     * Check if siswa punya riwayat untuk materi
     */
    @Query("SELECT EXISTS(SELECT 1 FROM riwayat_belajar WHERE id_siswa = :siswaId AND id_materi = :materiId)")
    boolean hasRiwayat(int siswaId, int materiId);

    /**
     * Get riwayat untuk export
     */
    @Query("SELECT * FROM riwayat_belajar ORDER BY id_riwayat ASC")
    List<RiwayatBelajar> getForExport();

    /**
     * Batch insert dari API
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAllFromApi(List<RiwayatBelajar> riwayatList);

    /**
     * Get riwayat belajar dengan materi info
     */
    @Query("SELECT rb.*, mk.judul_kaidah, mk.tingkat_kesulitan " +
            "FROM riwayat_belajar rb " +
            "INNER JOIN materi_kaidah mk ON rb.id_materi = mk.id_materi " +
            "WHERE rb.id_siswa = :siswaId ORDER BY rb.waktu_diubah DESC")
    List<RiwayatBelajar> getRiwayatWithMateriInfo(int siswaId);

    /**
     * Get statistics untuk siswa
     */
    @Query("SELECT " +
            "COUNT(*) as total_materi, " +
            "COUNT(CASE WHEN status = 'selesai' THEN 1 END) as materi_selesai, " +
            "COUNT(CASE WHEN status = 'sedang_belajar' THEN 1 END) as materi_dipelajari, " +
            "AVG(persentase_penguasaan) as rata_rata_progress, " +
            "SUM(total_sesi_diikuti) as total_sesi, " +
            "AVG(rata_rata_skor) as rata_rata_skor " +
            "FROM riwayat_belajar WHERE id_siswa = :siswaId")
    Object[] getStatisticsBySiswa(int siswaId);

    /**
     * Get riwayat belajar dengan performa metrics
     */
    @Query("SELECT " +
            "rb.*, " +
            "CASE WHEN rb.persentase_penguasaan >= 90 THEN 'A' " +
            "WHEN rb.persentase_penguasaan >= 80 THEN 'B' " +
            "WHEN rb.persentase_penguasaan >= 70 THEN 'C' " +
            "WHEN rb.persentase_penguasaan >= 60 THEN 'D' " +
            "ELSE 'E' " +
            "END as grade " +
            "FROM riwayat_belajar rb " +
            "WHERE rb.id_siswa = :siswaId " +
            "ORDER BY rb.persentase_penguasaan DESC")
    List<RiwayatBelajar> getRiwayatWithGrade(int siswaId);

    /**
     * Get materi yang belum dimulai untuk siswa
     */
    @Query("SELECT mk.id_materi, mk.judul_kaidah, mk.tingkat_kesulitan " +
            "FROM materi_kaidah mk " +
            "LEFT JOIN riwayat_belajar rb ON mk.id_materi = rb.id_materi AND rb.id_siswa = :siswaId " +
            "WHERE rb.id_riwayat IS NULL OR rb.status = 'belum_dimulai' " +
            "ORDER BY mk.urutan ASC")
    List<Object[]> getMateriBelumDimulai(int siswaId);

    /**
     * Get learning streak untuk siswa
     */
    @Query("SELECT MAX(streak_hari) as max_streak, COUNT(CASE WHEN streak_hari > 0 THEN 1 END) as total_learning_days " +
            "FROM riwayat_belajar WHERE id_siswa = :siswaId")
    Object[] getLearningStreak(int siswaId);

    /**
     * Batch insert dengan progress tracking
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAllBatch(List<RiwayatBelajar> riwayatList);

    /**
     * Get riwayat belajar yang perlu sync dari server
     */
    @Query("SELECT * FROM riwayat_belajar WHERE waktu_diubah < :lastSync ORDER BY id_riwayat ASC")
    List<RiwayatBelajar> getRiwayatNeedingUpdate(long lastSync);

    /**
     * Clean up old riwayat belajar
     */
    @Query("DELETE FROM riwayat_belajar WHERE waktu_diubah < :cutoffTime AND status = 'selesai'")
    int cleanupOldRiwayat(long cutoffTime);

    /**
     * Get riwayat belajar untuk recommendation system
     */
    @Query("SELECT rb.* FROM riwayat_belajar rb " +
            "WHERE rb.id_siswa = :siswaId AND " +
            "(rb.status = 'belum_dimulai' OR " +
            "(rb.status = 'sedang_belajar' AND rb.persentase_penguasaan < 50) OR " +
            "(rb.status = 'selesai' AND rb.persentase_penguasaan < 70)) " +
            "ORDER BY rb.persentase_penguasaan ASC " +
            "LIMIT :limit")
    List<RiwayatBelajar> getRiwayatForRecommendation(int siswaId, int limit);

    /**
     * Get progress timeline untuk materi tertentu
     */
    @Query("SELECT * FROM riwayat_belajar WHERE id_siswa = :siswaId AND id_materi = :materiId ORDER BY waktu_diubah ASC")
    List<RiwayatBelajar> getProgressTimeline(int siswaId, int materiId);

    /**
     * Update multiple fields
     */
    @Query("UPDATE riwayat_belajar SET " +
            "persentase_penguasaan = :persentase, " +
            "status = :status, " +
            "total_sesi_diikuti = :totalSesi, " +
            "rata_rata_skor = :rataSkor, " +
            "total_waktu_belajar_menit = :totalWaktu, " +
            "waktu_akses_terakhir = :waktuAkses " +
            "WHERE id_riwayat = :id")
    int updateAllFields(int id, float persentase, String status, int totalSesi, float rataSkor, int totalWaktu, String waktuAkses);

    /**
     * Get riwayat dengan last activity
     */
    @Query("SELECT rb.*, " +
            "sl.waktu_mulai as last_activity " +
            "FROM riwayat_belajar rb " +
            "LEFT JOIN sesi_latihan sl ON rb.id_siswa = sl.id_siswa AND rb.id_materi = sl.id_materi " +
            "WHERE rb.id_siswa = :siswaId " +
            "GROUP BY rb.id_riwayat " +
            "HAVING MAX(sl.waktu_mulai) " +
            "ORDER BY MAX(sl.waktu_mulai) DESC")
    List<RiwayatBelajar> getRiwayatWithLastActivity(int siswaId);
}