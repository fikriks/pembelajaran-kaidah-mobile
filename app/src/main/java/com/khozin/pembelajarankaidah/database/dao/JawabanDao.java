package com.khozin.pembelajarankaidah.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Update;

import com.khozin.pembelajarankaidah.data.model.Jawaban;
import com.khozin.pembelajarankaidah.database.entity.JawabanStatistics;
import com.khozin.pembelajarankaidah.database.entity.JawabanUnikStats;
import com.khozin.pembelajarankaidah.database.entity.JawabanPerforma;

import java.util.List;

/**
 * DAO untuk Jawaban entity
 * Menghandle semua database operations untuk jawaban
 */
@Dao
public interface JawabanDao {

    /**
     * Insert jawaban baru
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Jawaban jawaban);

    /**
     * Insert multiple jawaban
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<Jawaban> jawabanList);

    /**
     * Update jawaban
     */
    @Update
    int update(Jawaban jawaban);

    /**
     * Delete jawaban
     */
    @Delete
    int delete(Jawaban jawaban);

    /**
     * Get jawaban by ID
     */
    @Query("SELECT * FROM jawaban WHERE id_pilihan = :id")
    Jawaban getById(int id);

    /**
     * Get jawaban by ID (LiveData)
     */
    @Query("SELECT * FROM jawaban WHERE id_pilihan = :id")
    LiveData<Jawaban> getByIdLive(int id);

    /**
     * Get all jawaban
     */
    @Query("SELECT * FROM jawaban ORDER BY id_pilihan ASC")
    LiveData<List<Jawaban>> getAllJawaban();

    /**
     * Get all jawaban (sync)
     */
    @Query("SELECT * FROM jawaban ORDER BY id_pilihan ASC")
    List<Jawaban> getAllJawabanSync();

    /**
     * Get jawaban by soal ID
     */
    @Query("SELECT * FROM jawaban WHERE id_soal = :soalId ORDER BY urutan ASC")
    LiveData<List<Jawaban>> getBySoalId(int soalId);

    /**
     * Get jawaban by soal ID (sync)
     */
    @Query("SELECT * FROM jawaban WHERE id_soal = :soalId ORDER BY urutan ASC")
    List<Jawaban> getBySoalIdSync(int soalId);

    /**
     * Get jawaban benar untuk soal
     */
    @Query("SELECT * FROM jawaban WHERE id_soal = :soalId AND is_benar = 1 ORDER BY urutan ASC")
    Jawaban getJawabanBenar(int soalId);

    /**
     * Get jawaban salah untuk soal
     */
    @Query("SELECT * FROM jawaban WHERE id_soal = :soalId AND is_benar = 0 ORDER BY urutan ASC")
    LiveData<List<Jawaban>> getJawabanSalah(int soalId);

    /**
     * Get jawaban acak untuk soal
     */
    @Query("SELECT * FROM jawaban WHERE id_soal = :soalId ORDER BY RANDOM()")
    List<Jawaban> getJawabanAcak(int soalId);

    /**
     * Get total count jawaban
     */
    @Query("SELECT COUNT(*) FROM jawaban")
    int getTotalCount();

    /**
     * Get count by soal ID
     */
    @Query("SELECT COUNT(*) FROM jawaban WHERE id_soal = :soalId")
    int getCountBySoal(int soalId);

    /**
     * Get count jawaban benar
     */
    @Query("SELECT COUNT(*) FROM jawaban WHERE is_benar = 1")
    int getCountBenar();

    /**
     * Get count jawaban salah
     */
    @Query("SELECT COUNT(*) FROM jawaban WHERE is_benar = 0")
    int getCountSalah();

    /**
     * Check apakah soal punya jawaban benar
     */
    @Query("SELECT EXISTS(SELECT 1 FROM jawaban WHERE id_soal = :soalId AND is_benar = 1)")
    boolean hasJawabanBenar(int soalId);

    /**
     * Search jawaban by text
     */
    @Query("SELECT * FROM jawaban WHERE jawaban LIKE '%' || :query || '%' ORDER BY id_pilihan ASC")
    LiveData<List<Jawaban>> searchJawaban(String query);

    /**
     * Get jawaban by urutan
     */
    @Query("SELECT * FROM jawaban WHERE id_soal = :soalId AND urutan = :urutan")
    Jawaban getByUrutan(int soalId, int urutan);

    /**
     * Update selection state
     */
    @Query("UPDATE jawaban SET is_selected = :isSelected WHERE id_pilihan = :id")
    int updateSelection(int id, boolean isSelected);

    /**
     * Reset all selection states
     */
    @Query("UPDATE jawaban SET is_selected = 0")
    int resetAllSelections();

    /**
     * Reset selection states untuk soal tertentu
     */
    @Query("UPDATE jawaban SET is_selected = 0 WHERE id_soal = :soalId")
    int resetSelectionsBySoal(int soalId);

    /**
     * Delete jawaban by ID
     */
    @Query("DELETE FROM jawaban WHERE id_pilihan = :id")
    int deleteById(int id);

    /**
     * Delete jawaban by soal ID
     */
    @Query("DELETE FROM jawaban WHERE id_soal = :soalId")
    int deleteBySoalId(int soalId);

    /**
     * Delete all jawaban
     */
    @Query("DELETE FROM jawaban")
    int deleteAll();

    /**
     * Check jawaban exists
     */
    @Query("SELECT EXISTS(SELECT 1 FROM jawaban WHERE id_pilihan = :id)")
    boolean isJawabanExists(int id);

    /**
     * Get jawaban untuk export
     */
    @Query("SELECT * FROM jawaban ORDER BY id_pilihan ASC")
    List<Jawaban> getJawabanForExport();

    /**
     * Batch insert dari API
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAllFromApi(List<Jawaban> jawabanList);

    /**
     * Get jawaban dengan huruf pilihan untuk display
     */
    @Query("SELECT j.*, " +
            "CASE j.urutan " +
            "WHEN 1 THEN 'A' " +
            "WHEN 2 THEN 'B' " +
            "WHEN 3 THEN 'C' " +
            "WHEN 4 THEN 'D' " +
            "ELSE CAST(j.urutan AS TEXT) " +
            "END as huruf_pilihan " +
            "FROM jawaban j WHERE j.id_soal = :soalId ORDER BY j.urutan ASC")
    List<Jawaban> getJawabanWithHuruf(int soalId);

    /**
     * Update jawaban fields dari API response
     */
    @Query("UPDATE jawaban SET " +
            "jawaban = :text, " +
            "is_benar = :isBenar, " +
            "urutan = :urutan " +
            "WHERE id_pilihan = :id")
    int updateFromApi(int id, String text, boolean isBenar, int urutan);

    /**
     * Get jawaban yang sering dipilih
     */
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT j.*, COUNT(djs.id_detail) as selection_count " +
            "FROM jawaban j " +
            "INNER JOIN detail_jawaban_siswa djs ON j.id_pilihan = djs.id_pilihan " +
            "INNER JOIN sesi_latihan sl ON djs.id_sesi = sl.id_sesi " +
            "WHERE sl.id_siswa = :siswaId " +
            "GROUP BY j.id_pilihan " +
            "ORDER BY selection_count DESC")
    List<Jawaban> getJawabanSeringDipilih(int siswaId);

    /**
     * Get jawaban yang jarang dipilih
     */
    @Query("SELECT j.* FROM jawaban j " +
            "LEFT JOIN detail_jawaban_siswa djs ON j.id_pilihan = djs.id_pilihan " +
            "LEFT JOIN sesi_latihan sl ON djs.id_sesi = sl.id_sesi AND sl.id_siswa = :siswaId " +
            "GROUP BY j.id_pilihan " +
            "ORDER BY COUNT(djs.id_detail) ASC")
    List<Jawaban> getJawabanJarangDipilih(int siswaId);

    /**
     * Get jawaban statistics
     */
    @Query("SELECT " +
            "COUNT(*) as total_jawaban, " +
            "COUNT(CASE WHEN is_benar = 1 THEN 1 END) as jawaban_benar, " +
            "COUNT(CASE WHEN is_benar = 0 THEN 1 END) as jawaban_salah, " +
            "AVG(urutan) as rata_rata_urutan " +
            "FROM jawaban")
    JawabanStatistics getJawabanStatistics();

    /**
     * Get jawaban performance untuk siswa
     */
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT j.*, " +
            "COUNT(djs.id_detail) as total_dipilih, " +
            "COUNT(CASE WHEN djs.is_benar = 1 THEN 1 END) as total_benar, " +
            "CAST(COUNT(CASE WHEN djs.is_benar = 1 THEN 1 END) AS FLOAT) / COUNT(djs.id_detail) as benar_rate " +
            "FROM jawaban j " +
            "INNER JOIN detail_jawaban_siswa djs ON j.id_pilihan = djs.id_pilihan " +
            "INNER JOIN sesi_latihan sl ON djs.id_sesi = sl.id_sesi AND sl.id_siswa = :siswaId " +
            "GROUP BY j.id_pilihan " +
            "ORDER BY benar_rate DESC")
    List<Jawaban> getJawabanPerforma(int siswaId);

    /**
     * Batch insert dengan progress tracking
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAllBatch(List<Jawaban> jawabanList);

    /**
     * Get jawaban yang perlu sync dari server
     */
    @Query("SELECT * FROM jawaban WHERE waktu_diubah < :lastSync ORDER BY id_pilihan ASC")
    List<Jawaban> getJawabanNeedingUpdate(long lastSync);

    /**
     * Clean up jawaban tanpa soal
     */
    @Query("DELETE FROM jawaban WHERE id_soal NOT IN (SELECT id_soal FROM soal)")
    int cleanupJawabanTanpaSoal();

    /**
     * Get jawaban untuk multiple soal
     */
    @Query("SELECT * FROM jawaban WHERE id_soal IN (:soalIds) ORDER BY id_soal, urutan")
    List<Jawaban> getJawabanForMultipleSoal(List<Integer> soalIds);

    /**
     * Get jawaban acak untuk practice
     */
    @Query("SELECT j.* FROM jawaban j " +
            "INNER JOIN soal s ON j.id_soal = s.id_soal " +
            "WHERE s.id_materi = :materiId AND j.is_benar = 0 " +
            "ORDER BY RANDOM() LIMIT :limit")
    List<Jawaban> getJawabanSalahAcak(int materiId, int limit);

    /**
     * Get jawaban untuk LCM shuffling
     */
    @Query("SELECT * FROM jawaban WHERE id_soal = :soalId ORDER BY urutan")
    List<Jawaban> getJawabanForShuffling(int soalId);

    /**
     * Update jawaban dengan LCM shuffled indices
     */
    @Query("UPDATE jawaban SET " +
            "urutan = CASE id_pilihan " +
            "WHEN :id1 THEN :urut1 " +
            "WHEN :id2 THEN :urut2 " +
            "WHEN :id3 THEN :urut3 " +
            "WHEN :id4 THEN :urut4 " +
            "ELSE urutan " +
            "END " +
            "WHERE id_soal = :soalId AND id_pilihan IN (:id1, :id2, :id3, :id4)")
    int updateShuffledOrder(int soalId, int id1, int id2, int id3, int id4, int urut1, int urut2, int urut3, int urut4);

    /**
     * Get jawaban yang sering salah dipilih
     */
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT j.*, COUNT(djs.id_detail) as salah_count " +
            "FROM jawaban j " +
            "INNER JOIN detail_jawaban_siswa djs ON j.id_pilihan = djs.id_pilihan " +
            "INNER JOIN sesi_latihan sl ON djs.id_sesi = sl.id_sesi AND sl.id_siswa = :siswaId " +
            "WHERE j.is_benar = 0 OR djs.is_benar = 0 " +
            "GROUP BY j.id_pilihan " +
            "ORDER BY salah_count DESC")
    List<Jawaban> getJawabanSeringSalah(int siswaId);

    /**
     * Get jawaban unik per soal
     */
    @Query("SELECT COUNT(DISTINCT id_soal) as unique_soal, COUNT(*) as total_jawaban FROM jawaban")
    JawabanUnikStats getJawabanUnikStats();
}