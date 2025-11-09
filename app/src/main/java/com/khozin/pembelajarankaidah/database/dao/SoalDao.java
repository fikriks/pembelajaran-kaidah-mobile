package com.khozin.pembelajarankaidah.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Update;

import com.khozin.pembelajarankaidah.data.model.Soal;
import com.khozin.pembelajarankaidah.database.entity.SoalStatistics;
import com.khozin.pembelajarankaidah.database.entity.SoalWithJawabanCount;
import com.khozin.pembelajarankaidah.database.entity.SoalPerformaPerTingkat;

import java.util.List;

/**
 * DAO untuk Soal entity
 * Menghandle semua database operations untuk soal
 */
@Dao
public interface SoalDao {

    /**
     * Insert soal baru
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Soal soal);

    /**
     * Insert multiple soal
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<Soal> soalList);

    /**
     * Update soal
     */
    @Update
    int update(Soal soal);

    /**
     * Delete soal
     */
    @Delete
    int delete(Soal soal);

    /**
     * Get soal by ID
     */
    @Query("SELECT * FROM soal WHERE id_soal = :id")
    Soal getById(int id);

    /**
     * Get soal by ID (LiveData)
     */
    @Query("SELECT * FROM soal WHERE id_soal = :id")
    LiveData<Soal> getByIdLive(int id);

    /**
     * Get all soal
     */
    @Query("SELECT * FROM soal ORDER BY id_soal ASC")
    LiveData<List<Soal>> getAllSoal();

    /**
     * Get all soal (sync)
     */
    @Query("SELECT * FROM soal ORDER BY id_soal ASC")
    List<Soal> getAllSoalSync();

    /**
     * Get soal by bab ID
     */
    @Query("SELECT * FROM soal WHERE id_bab = :babId ORDER BY id_soal ASC")
    LiveData<List<Soal>> getByBabId(int babId);

    /**
     * Get soal by bab ID (sync)
     */
    @Query("SELECT * FROM soal WHERE id_bab = :babId ORDER BY id_soal ASC")
    List<Soal> getByBabIdSync(int babId);

    /**
     * Get soal by tingkat kesulitan
     */
    @Query("SELECT * FROM soal WHERE tingkat_kesulitan = :tingkat ORDER BY id_soal ASC")
    LiveData<List<Soal>> getByTingkatKesulitan(String tingkat);

    /**
     * Get soal acak untuk quiz
     */
    @Query("SELECT * FROM soal WHERE id_bab = :babId ORDER BY RANDOM() LIMIT :limit")
    List<Soal> getSoalAcak(int babId, int limit);

    /**
     * Get total count soal
     */
    @Query("SELECT COUNT(*) FROM soal")
    int getTotalCount();

    /**
     * Get count by bab ID
     */
    @Query("SELECT COUNT(*) FROM soal WHERE id_bab = :babId")
    int getCountByBab(int babId);

    /**
     * Get count by tingkat kesulitan
     */
    @Query("SELECT COUNT(*) FROM soal WHERE tingkat_kesulitan = :tingkat")
    int getCountByTingkat(String tingkat);

    /**
     * Get soal statistics
     */
    @Query("SELECT " +
            "COUNT(*) as total, " +
            "COUNT(CASE WHEN tingkat_kesulitan = 'mudah' THEN 1 END) as mudah, " +
            "COUNT(CASE WHEN tingkat_kesulitan = 'sedang' THEN 1 END) as sedang, " +
            "COUNT(CASE WHEN tingkat_kesulitan = 'sulit' THEN 1 END) as sulit, " +
            "AVG(poin) as rata_rata_poin " +
            "FROM soal")
    SoalStatistics getSoalStatistics();

    /**
     * Search soal by pertanyaan
     */
    @Query("SELECT * FROM soal WHERE pertanyaan LIKE '%' || :query || '%' ORDER BY id_soal ASC")
    LiveData<List<Soal>> searchSoal(String query);

    /**
     * Get soal by poin range
     */
    @Query("SELECT * FROM soal WHERE poin BETWEEN :minPoin AND :maxPoin ORDER BY poin ASC")
    LiveData<List<Soal>> getByPoinRange(int minPoin, int maxPoin);

    /**
     * Get soal by dibuat_oleh
     */
    @Query("SELECT * FROM soal WHERE dibuat_oleh = :dibuatOleh ORDER BY id_soal DESC")
    LiveData<List<Soal>> getByDibuatOleh(int dibuatOleh);

    /**
     * Delete soal by ID
     */
    @Query("DELETE FROM soal WHERE id_soal = :id")
    int deleteById(int id);

    /**
     * Delete soal by bab ID
     */
    @Query("DELETE FROM soal WHERE id_bab = :babId")
    int deleteByBabId(int babId);

    /**
     * Delete all soal
     */
    @Query("DELETE FROM soal")
    int deleteAll();

    /**
     * Check soal exists
     */
    @Query("SELECT EXISTS(SELECT 1 FROM soal WHERE id_soal = :id)")
    boolean isSoalExists(int id);

    /**
     * Get soal untuk export
     */
    @Query("SELECT * FROM soal ORDER BY id_soal ASC")
    List<Soal> getSoalForExport();

    /**
     * Batch insert dari API
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAllFromApi(List<Soal> soalList);

    /**
     * Get soal dengan jawaban count
     */
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT s.*, " +
            "(SELECT COUNT(*) FROM jawaban j WHERE j.id_soal = s.id_soal) as jawaban_count " +
            "FROM soal s " +
            "ORDER BY s.id_soal ASC")
    List<SoalWithJawabanCount> getSoalWithJawabanCount();

    /**
     * Get soal untuk quiz dengan LCM seed
     */
    @Query("SELECT * FROM soal WHERE id_bab = :babId " +
            "AND id_soal IN (SELECT id_soal FROM detail_jawaban_siswa WHERE id_sesi = :sesiId) " +
            "ORDER BY id_soal ASC")
    List<Soal> getSoalForSesi(int babId, int sesiId);

    /**
     * Get soal yang belum ada jawaban
     */
    @Query("SELECT * FROM soal WHERE id_bab = :babId " +
            "AND id_soal NOT IN (SELECT id_soal FROM detail_jawaban_siswa WHERE id_sesi = :sesiId) " +
            "ORDER BY id_soal ASC")
    List<Soal> getSoalBelumDijawab(int babId, int sesiId);

    /**
     * Update soal fields from API response
     */
    @Query("UPDATE soal SET " +
            "pertanyaan = :pertanyaan, " +
            "tipe_soal = :tipe, " +
            "tingkat_kesulitan = :tingkat, " +
            "poin = :poin, " +
            "waktu_diubah = :waktuDiubah " +
            "WHERE id_soal = :id")
    int updateFromApi(int id, String pertanyaan, String tipe, String tingkat, int poin, String waktuDiubah);

    /**
     * Get soal untuk review
     */
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT s.*, " +
            "COUNT(CASE WHEN djs.is_benar = 1 THEN 1 END) as benar_count, " +
            "COUNT(djs.id_detail) as total_jawaban " +
            "FROM soal s " +
            "INNER JOIN detail_jawaban_siswa djs ON s.id_soal = djs.id_soal " +
            "INNER JOIN sesi_latihan sl ON djs.id_sesi = sl.id_sesi " +
            "WHERE sl.id_siswa = :siswaId " +
            "GROUP BY s.id_soal " +
            "ORDER BY (CAST(COUNT(CASE WHEN djs.is_benar = 1 THEN 1 END) AS FLOAT) / COUNT(djs.id_detail)) ASC")
    List<Soal> getSoalReview(int siswaId);

    /**
     * Get soal yang sulit (benar rate < 50%)
     * Optimized query that only returns soal data
     */
    @Query("SELECT s.id_soal FROM soal s " +
            "INNER JOIN detail_jawaban_siswa djs ON s.id_soal = djs.id_soal " +
            "INNER JOIN sesi_latihan sl ON djs.id_sesi = sl.id_sesi " +
            "WHERE sl.id_siswa = :siswaId " +
            "GROUP BY s.id_soal " +
            "HAVING (CAST(COUNT(CASE WHEN djs.is_benar = 1 THEN 1 END) AS FLOAT) / COUNT(djs.id_detail)) < 0.5 " +
            "ORDER BY (CAST(COUNT(CASE WHEN djs.is_benar = 1 THEN 1 END) AS FLOAT) / COUNT(djs.id_detail)) ASC " +
            "LIMIT 20")
    List<Integer> getSoalSulitIds(int siswaId);

    /**
     * Get random soal untuk practice
     */
    @Query("SELECT * FROM soal WHERE id_bab = :babId AND tingkat_kesulitan = :tingkat ORDER BY RANDOM() LIMIT :limit")
    List<Soal> getSoalPractice(int babId, String tingkat, int limit);

    /**
     * Get soal untuk material completion check
     */
    @Query("SELECT * FROM soal WHERE id_bab = :babId ORDER BY id_soal ASC")
    List<Soal> getSoalForCompletionCheck(int babId);

    /**
     * Get soal performance statistics
     */
    @Query("SELECT s.tingkat_kesulitan, " +
            "COUNT(*) as total_soal, " +
            "COUNT(djs.id_detail) as total_jawaban, " +
            "COUNT(CASE WHEN djs.is_benar = 1 THEN 1 END) as jawaban_benar, " +
            "CAST(COUNT(CASE WHEN djs.is_benar = 1 THEN 1 END) AS FLOAT) / COUNT(djs.id_detail) as benar_rate " +
            "FROM soal s " +
            "LEFT JOIN detail_jawaban_siswa djs ON s.id_soal = djs.id_soal " +
            "LEFT JOIN sesi_latihan sl ON djs.id_sesi = sl.id_sesi AND sl.id_siswa = :siswaId " +
            "GROUP BY s.tingkat_kesulitan")
    List<SoalPerformaPerTingkat> getPerformaPerTingkat(int siswaId);

    /**
     * Batch insert dengan progress tracking
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAllBatch(List<Soal> soalList);

    /**
     * Get soal yang perlu sync dari server
     */
    @Query("SELECT * FROM soal WHERE waktu_diubah < :lastSync ORDER BY id_soal ASC")
    List<Soal> getSoalNeedingUpdate(long lastSync);

    /**
     * Clean up soal tanpa jawaban
     */
    @Query("DELETE FROM soal WHERE id_soal NOT IN (SELECT DISTINCT id_soal FROM jawaban)")
    int cleanupSoalTanpaJawaban();

    /**
     * Get soal dengan multiple filters
     */
    @Query("SELECT * FROM soal WHERE " +
            "(:babId IS NULL OR id_bab = :babId) AND " +
            "(:tingkat IS NULL OR tingkat_kesulitan = :tingkat) AND " +
            "(:minPoin IS NULL OR poin >= :minPoin) AND " +
            "(:maxPoin IS NULL OR poin <= :maxPoin) " +
            "ORDER BY id_soal ASC")
    List<Soal> getSoalWithFilters(Integer babId, String tingkat, Integer minPoin, Integer maxPoin);
}