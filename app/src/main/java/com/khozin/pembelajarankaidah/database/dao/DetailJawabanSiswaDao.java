package com.khozin.pembelajarankaidah.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.khozin.pembelajarankaidah.data.model.DetailJawabanSiswa;

import java.util.List;

/**
 * DAO untuk DetailJawabanSiswa entity
 * Menghandle semua database operations untuk detail jawaban siswa per sesi
 */
@Dao
public interface DetailJawabanSiswaDao {

    /**
     * Insert detail jawaban baru
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(DetailJawabanSiswa detail);

    /**
     * Insert multiple detail jawaban
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<DetailJawabanSiswa> detailList);

    /**
     * Update detail jawaban
     */
    @Update
    int update(DetailJawabanSiswa detail);

    /**
     * Delete detail jawaban
     */
    @Delete
    int delete(DetailJawabanSiswa detail);

    /**
     * Get detail jawaban by ID
     */
    @Query("SELECT * FROM detail_jawaban_siswa WHERE id_detail = :id")
    DetailJawabanSiswa getById(int id);

    /**
     * Get detail jawaban by ID (LiveData)
     */
    @Query("SELECT * FROM detail_jawaban_siswa WHERE id_detail = :id")
    LiveData<DetailJawabanSiswa> getByIdLive(int id);

    /**
     * Get detail jawaban by sesi ID
     */
    @Query("SELECT * FROM detail_jawaban_siswa WHERE id_sesi = :sesiId ORDER BY urutan_soal ASC")
    LiveData<List<DetailJawabanSiswa>> getBySesiId(int sesiId);

    /**
     * Get detail jawaban by sesi ID (sync)
     */
    @Query("SELECT * FROM detail_jawaban_siswa WHERE id_sesi = :sesiId ORDER BY urutan_soal ASC")
    List<DetailJawabanSiswa> getBySesiIdSync(int sesiId);

    /**
     * Get detail jawaban by siswa ID
     */
    @Query("SELECT * FROM detail_jawaban_siswa WHERE id_siswa = :siswaId ORDER BY waktu_jawab DESC")
    LiveData<List<DetailJawabanSiswa>> getBySiswaId(int siswaId);

    /**
     * Get detail jawaban by soal ID
     */
    @Query("SELECT * FROM detail_jawaban_siswa WHERE id_soal = :soalId ORDER BY waktu_jawab DESC")
    LiveData<List<DetailJawabanSiswa>> getBySoalId(int soalId);

    /**
     * Get detail jawaban benar
     */
    @Query("SELECT * FROM detail_jawaban_siswa WHERE is_benar = 1 ORDER BY waktu_jawab DESC")
    LiveData<List<DetailJawabanSiswa>> getJawabanBenar();

    /**
     * Get detail jawaban salah
     */
    @Query("SELECT * FROM detail_jawaban_siswa WHERE is_benar = 0 ORDER BY waktu_jawab DESC")
    LiveData<List<DetailJawabanSiswa>> getJawabanSalah();

    /**
     * Get detail jawaban untuk sesi dan soal tertentu
     */
    @Query("SELECT * FROM detail_jawaban_siswa WHERE id_sesi = :sesiId AND id_soal = :soalId")
    DetailJawabanSiswa getBySesiAndSoal(int sesiId, int soalId);

    /**
     * Get total count detail jawaban
     */
    @Query("SELECT COUNT(*) FROM detail_jawaban_siswa")
    int getTotalCount();

    /**
     * Get count by sesi ID
     */
    @Query("SELECT COUNT(*) FROM detail_jawaban_siswa WHERE id_sesi = :sesiId")
    int getCountBySesi(int sesiId);

    /**
     * Get count by siswa ID
     */
    @Query("SELECT COUNT(*) FROM detail_jawaban_siswa WHERE id_siswa = :siswaId")
    int getCountBySiswa(int siswaId);

    /**
     * Get count jawaban benar
     */
    @Query("SELECT COUNT(*) FROM detail_jawaban_siswa WHERE is_benar = 1")
    int getCountBenar();

    /**
     * Get count jawaban salah
     */
    @Query("SELECT COUNT(*) FROM detail_jawaban_siswa WHERE is_benar = 0")
    int getCountSalah();

    /**
     * Get count jawaban benar untuk sesi
     */
    @Query("SELECT COUNT(*) FROM detail_jawaban_siswa WHERE id_sesi = :sesiId AND is_benar = 1")
    int getCountBenarBySesi(int sesiId);

    /**
     * Get count jawaban salah untuk sesi
     */
    @Query("SELECT COUNT(*) FROM detail_jawaban_siswa WHERE id_sesi = :sesiId AND is_benar = 0")
    int getCountSalahBySesi(int sesiId);

    /**
     * Get detail jawaban by waktu range
     */
    @Query("SELECT * FROM detail_jawaban_siswa WHERE waktu_jawab BETWEEN :startTime AND :endTime ORDER BY waktu_jawab ASC")
    List<DetailJawabanSiswa> getByTimeRange(long startTime, long endTime);

    /**
     * Search detail jawaban
     */
    @Query("SELECT * FROM detail_jawaban_siswa WHERE pertanyaan_text LIKE '%' || :query || '%' OR jawaban_text LIKE '%' || :query || '%' ORDER BY waktu_jawab DESC")
    LiveData<List<DetailJawabanSiswa>> search(String query);

    /**
     * Get detail jawaban yang sudah direview
     */
    @Query("SELECT * FROM detail_jawaban_siswa WHERE is_reviewed = 1 ORDER BY waktu_jawab DESC")
    LiveData<List<DetailJawabanSiswa>> getReviewed();

    /**
     * Get detail jawaban yang belum direview
     */
    @Query("SELECT * FROM detail_jawaban_siswa WHERE is_reviewed = 0 ORDER BY waktu_jawab DESC")
    LiveData<List<DetailJawabanSiswa>> getUnreviewed();

    /**
     * Update review status
     */
    @Query("UPDATE detail_jawaban_siswa SET is_reviewed = 1 WHERE id_detail = :id")
    int markAsReviewed(int id);

    /**
     * Reset review status
     */
    @Query("UPDATE detail_jawaban_siswa SET is_reviewed = 0")
    int resetAllReviewed();

    /**
     * Delete detail jawaban by ID
     */
    @Query("DELETE FROM detail_jawaban_siswa WHERE id_detail = :id")
    int deleteById(int id);

    /**
     * Delete detail jawaban by sesi ID
     */
    @Query("DELETE FROM detail_jawaban_siswa WHERE id_sesi = :sesiId")
    int deleteBySesiId(int sesiId);

    /**
     * Delete detail jawaban by siswa ID
     */
    @Query("DELETE FROM detail_jawaban_siswa WHERE id_siswa = :siswaId")
    int deleteBySiswaId(int siswaId);

    /**
     * Delete all detail jawaban
     */
    @Query("DELETE FROM detail_jawaban_siswa")
    int deleteAll();

    /**
     * Get statistics untuk siswa
     */
    @Query("SELECT " +
            "COUNT(*) as total_jawaban, " +
            "COUNT(CASE WHEN is_benar = 1 THEN 1 END) as jawaban_benar, " +
            "COUNT(CASE WHEN is_benar = 0 THEN 1 END) as jawaban_salah, " +
            "AVG(waktu_respons_detik) as rata_rata_waktu, " +
            "COUNT(CASE WHEN is_reviewed = 1 THEN 1 END) as sudah_review " +
            "FROM detail_jawaban_siswa WHERE id_siswa = :siswaId")
    Object[] getStatisticsBySiswa(int siswaId);

    /**
     * Get performance per materi
     */
    @Query("SELECT " +
            "mk.id_materi, mk.judul_kaidah, " +
            "COUNT(*) as total_jawaban, " +
            "COUNT(CASE WHEN djs.is_benar = 1 THEN 1 END) as jawaban_benar, " +
            "CAST(COUNT(CASE WHEN djs.is_benar = 1 THEN 1 END) AS FLOAT) / COUNT(*) as benar_rate " +
            "FROM detail_jawaban_siswa djs " +
            "INNER JOIN sesi_latihan sl ON djs.id_sesi = sl.id_sesi " +
            "INNER JOIN materi_kaidah mk ON sl.id_materi = mk.id_materi " +
            "WHERE sl.id_siswa = :siswaId " +
            "GROUP BY mk.id_materi, mk.judul_kaidah " +
            "ORDER BY benar_rate DESC")
    List<Object[]> getPerformaPerMateri(int siswaId);

    /**
     * Get detail jawaban untuk export
     */
    @Query("SELECT * FROM detail_jawaban_siswa ORDER BY id_detail ASC")
    List<DetailJawabanSiswa> getForExport();

    /**
     * Batch insert dari API
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAllFromApi(List<DetailJawabanSiswa> detailList);

    /**
     * Get detail jawaban dengan informasi lengkap
     */
    @Query("SELECT djs.*, s.pertanyaan as soal_pertanyaan, j.jawaban as jawaban_text, " +
            "CASE WHEN j.is_benar = 1 THEN 'Benar' ELSE 'Salah' END as kunci_jawaban " +
            "FROM detail_jawaban_siswa djs " +
            "LEFT JOIN soal s ON djs.id_soal = s.id_soal " +
            "LEFT JOIN jawaban j ON djs.id_pilihan = j.id_pilihan " +
            "WHERE djs.id_sesi = :sesiId ORDER BY djs.urutan_soal ASC")
    List<DetailJawabanSiswa> getDetailWithInfo(int sesiId);

    /**
     * Get detail jawaban yang perlu di-review
     */
    @Query("SELECT * FROM detail_jawaban_siswa WHERE " +
            "(is_benar = 0 OR is_reviewed = 0) AND " +
            "waktu_jawab >= :sinceTime " +
            "ORDER BY waktu_jawab DESC " +
            "LIMIT :limit")
    List<DetailJawabanSiswa> getJawabanNeedingReview(long sinceTime, int limit);

    /**
     * Get jawaban yang paling sering salah
     */
    @Query("SELECT j.jawaban, COUNT(*) as salah_count " +
            "FROM detail_jawaban_siswa djs " +
            "INNER JOIN jawaban j ON djs.id_pilihan = j.id_pilihan " +
            "WHERE djs.is_benar = 0 " +
            "GROUP BY j.id_pilihan, j.jawaban " +
            "ORDER BY salah_count DESC " +
            "LIMIT :limit")
    List<Object[]> getJawabanSeringSalah(int limit);

    /**
     * Update waktu respons
     */
    @Query("UPDATE detail_jawaban_siswa SET waktu_respons_detik = :waktu WHERE id_detail = :id")
    int updateWaktuRespons(int id, int waktu);

    /**
     * Batch insert dengan progress tracking
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAllBatch(List<DetailJawabanSiswa> detailList);

    /**
     * Get detail jawaban dengan soal info
     */
    @Query("SELECT djs.*, s.pertanyaan, s.tingkat_kesulitan, s.poin " +
            "FROM detail_jawaban_siswa djs " +
            "INNER JOIN soal s ON djs.id_soal = s.id_soal " +
            "WHERE djs.id_sesi = :sesiId ORDER BY djs.urutan_soal ASC")
    List<DetailJawabanSiswa> getDetailWithSoalInfo(int sesiId);

    /**
     * Get detail jawaban untuk LCM analysis
     */
    @Query("SELECT djs.*, sl.seed_digunakan " +
            "FROM detail_jawaban_siswa djs " +
            "INNER JOIN sesi_latihan sl ON djs.id_sesi = sl.id_sesi " +
            "WHERE sl.id_siswa = :siswaId " +
            "ORDER BY sl.id_sesi DESC, djs.urutan_soal ASC")
    List<DetailJawabanSiswa> getDetailWithLCMInfo(int siswaId);

    /**
     * Get detail jawaban untuk analytics
     */
    @Query("SELECT " +
            "DATE(waktu_jawab/1000, 'unixepoch') as tanggal, " +
            "COUNT(*) as total_jawaban, " +
            "COUNT(CASE WHEN is_benar = 1 THEN 1 END) as jawaban_benar, " +
            "AVG(waktu_respons_detik) as rata_waktu " +
            "FROM detail_jawaban_siswa " +
            "WHERE id_siswa = :siswaId " +
            "GROUP BY DATE(waktu_jawab/1000, 'unixepoch') " +
            "ORDER BY tanggal DESC")
    List<Object[]> getAnalyticsByDate(int siswaId);

    /**
     * Clean up old detail jawaban
     */
    @Query("DELETE FROM detail_jawaban_siswa WHERE waktu_jawab < :cutoffTime")
    int cleanupOldDetails(long cutoffTime);

    /**
     * Get detail jawaban unik per soal
     */
    @Query("SELECT COUNT(DISTINCT id_soal) as unique_soal, COUNT(*) as total_jawaban FROM detail_jawaban_siswa WHERE id_siswa = :siswaId")
    Object[] getJawabanUnikStats(int siswaId);

    /**
     * Get detail jawaban dengan performa metrics
     */
    @Query("SELECT djs.*, " +
            "CASE WHEN djs.is_benar = 1 THEN 1 ELSE 0 END as skor, " +
            "djs.waktu_respons_detik as waktu " +
            "FROM detail_jawaban_siswa djs " +
            "INNER JOIN sesi_latihan sl ON djs.id_sesi = sl.id_sesi " +
            "WHERE sl.id_siswa = :siswaId " +
            "ORDER BY djs.waktu_jawab DESC")
    List<DetailJawabanSiswa> getDetailWithMetrics(int siswaId);
}