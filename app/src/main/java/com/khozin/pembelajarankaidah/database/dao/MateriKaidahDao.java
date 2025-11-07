package com.khozin.pembelajarankaidah.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.khozin.pembelajarankaidah.data.model.MateriKaidah;

import java.util.List;

/**
 * DAO untuk MateriKaidah entity
 * Menghandle semua database operations untuk materi kaidah
 */
@Dao
public interface MateriKaidahDao {

    /**
     * Insert materi kaidah baru
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(MateriKaidah materiKaidah);

    /**
     * Insert multiple materi kaidah
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<MateriKaidah> materiKaidahList);

    /**
     * Update materi kaidah
     */
    @Update
    int update(MateriKaidah materiKaidah);

    /**
     * Delete materi kaidah
     */
    @Delete
    int delete(MateriKaidah materiKaidah);

    /**
     * Get materi kaidah by ID
     */
    @Query("SELECT * FROM materi_kaidah WHERE id_materi = :id")
    MateriKaidah getById(int id);

    /**
     * Get materi kaidah by ID (LiveData)
     */
    @Query("SELECT * FROM materi_kaidah WHERE id_materi = :id")
    LiveData<MateriKaidah> getByIdLive(int id);

    /**
     * Get all materi kaidah
     */
    @Query("SELECT * FROM materi_kaidah ORDER BY urutan ASC, id_materi ASC")
    LiveData<List<MateriKaidah>> getAllMateri();

    /**
     * Get all materi kaidah (sync)
     */
    @Query("SELECT * FROM materi_kaidah ORDER BY urutan ASC, id_materi ASC")
    List<MateriKaidah> getAllMateriSync();

    /**
     * Get materi kaidah by tingkat kesulitan
     */
    @Query("SELECT * FROM materi_kaidah WHERE tingkat_kesulitan = :tingkat ORDER BY urutan ASC")
    LiveData<List<MateriKaidah>> getByTingkatKesulitan(String tingkat);

    /**
     * Get materi kaidah untuk search
     */
    @Query("SELECT * FROM materi_kaidah WHERE judul_kaidah LIKE '%' || :query || '%' OR deskripsi LIKE '%' || :query || '%' ORDER BY urutan ASC")
    LiveData<List<MateriKaidah>> searchMateri(String query);

    /**
     * Get total count materi kaidah
     */
    @Query("SELECT COUNT(*) FROM materi_kaidah")
    int getTotalCount();

    /**
     * Get count by tingkat kesulitan
     */
    @Query("SELECT COUNT(*) FROM materi_kaidah WHERE tingkat_kesulitan = :tingkat")
    int getCountByTingkat(String tingkat);

    /**
     * Get materi statistics
     */
    @Query("SELECT " +
            "COUNT(*) as total, " +
            "COUNT(CASE WHEN tingkat_kesulitan = 'mudah' THEN 1 END) as mudah, " +
            "COUNT(CASE WHEN tingkat_kesulitan = 'sedang' THEN 1 END) as sedang, " +
            "COUNT(CASE WHEN tingkat_kesulitan = 'sulit' THEN 1 END) as sulit " +
            "FROM materi_kaidah")
    Object[] getMateriStatistics();

    /**
     * Update urutan materi kaidah
     */
    @Query("UPDATE materi_kaidah SET urutan = :urutan WHERE id_materi = :id")
    int updateUrutan(int id, int urutan);

    /**
     * Get next urutan
     */
    @Query("SELECT COALESCE(MAX(urutan), 0) + 1 FROM materi_kaidah")
    int getNextUrutan();

    /**
     * Update progress fields
     */
    @Query("UPDATE materi_kaidah SET total_soal = :totalSoal, progress_percentage = :progress, is_completed = :isCompleted WHERE id_materi = :id")
    int updateProgress(int id, int totalSoal, int progress, boolean isCompleted);

    /**
     * Get materi dengan progress untuk siswa tertentu
     */
    @Query("SELECT mk.*, " +
            "CASE WHEN rb.id_riwayat IS NOT NULL THEN rb.persentase_penguasaan ELSE 0 END as progress, " +
            "CASE WHEN rb.id_riwayat IS NOT NULL THEN rb.status ELSE 'belum_dimulai' END as status_belajar " +
            "FROM materi_kaidah mk " +
            "LEFT JOIN riwayat_belajar rb ON mk.id_materi = rb.id_materi AND rb.id_siswa = :siswaId " +
            "ORDER BY mk.urutan ASC")
    LiveData<List<MateriKaidah>> getMateriWithProgress(int siswaId);

    /**
     * Get materi by dibuat_oleh
     */
    @Query("SELECT * FROM materi_kaidah WHERE dibuat_oleh = :dibuatOleh ORDER BY urutan ASC")
    LiveData<List<MateriKaidah>> getByDibuatOleh(int dibuatOleh);

    /**
     * Get materi yang belum dipelajari siswa
     */
    @Query("SELECT mk.* FROM materi_kaidah mk " +
            "LEFT JOIN riwayat_belajar rb ON mk.id_materi = rb.id_materi AND rb.id_siswa = :siswaId " +
            "WHERE rb.id_riwayat IS NULL OR rb.status != 'selesai' " +
            "ORDER BY mk.urutan ASC")
    LiveData<List<MateriKaidah>> getMateriBelumSelesai(int siswaId);

    /**
     * Get materi yang sedang dipelajari siswa
     */
    @Query("SELECT mk.* FROM materi_kaidah mk " +
            "INNER JOIN riwayat_belajar rb ON mk.id_materi = rb.id_materi AND rb.id_siswa = :siswaId " +
            "WHERE rb.status = 'sedang_belajar' " +
            "ORDER BY mk.urutan ASC")
    LiveData<List<MateriKaidah>> getMateriSedangBelajar(int siswaId);

    /**
     * Get materi yang sudah selesai dipelajari siswa
     */
    @Query("SELECT mk.* FROM materi_kaidah mk " +
            "INNER JOIN riwayat_belajar rb ON mk.id_materi = rb.id_materi AND rb.id_siswa = :siswaId " +
            "WHERE rb.status = 'selesai' " +
            "ORDER BY rb.waktu_diubah DESC")
    LiveData<List<MateriKaidah>> getMateriSelesai(int siswaId);

    /**
     * Get materi untuk dashboard (dengan progress summary)
     */
    @Query("SELECT mk.*, " +
            "COUNT(CASE WHEN rb.status = 'selesai' THEN 1 END) as selesai_count, " +
            "COUNT(rb.id_riwayat) as total_progress " +
            "FROM materi_kaidah mk " +
            "LEFT JOIN riwayat_belajar rb ON mk.id_materi = rb.id_materi " +
            "GROUP BY mk.id_materi " +
            "ORDER BY mk.urutan ASC " +
            "LIMIT :limit")
    List<MateriKaidah> getMateriForDashboard(int limit);

    /**
     * Update materi dengan API data
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAllFromApi(List<MateriKaidah> materiList);

    /**
     * Delete all materi
     */
    @Query("DELETE FROM materi_kaidah")
    int deleteAll();

    /**
     * Delete materi by ID
     */
    @Query("DELETE FROM materi_kaidah WHERE id_materi = :id")
    int deleteById(int id);

    /**
     * Check materi exists
     */
    @Query("SELECT EXISTS(SELECT 1 FROM materi_kaidah WHERE id_materi = :id)")
    boolean isMateriExists(int id);

    /**
     * Get materi dengan soal count
     */
    @Query("SELECT mk.*, " +
            "(SELECT COUNT(*) FROM soal s WHERE s.id_materi = mk.id_materi) as soal_count " +
            "FROM materi_kaidah mk " +
            "ORDER BY mk.urutan ASC")
    List<MateriKaidah> getMateriWithSoalCount();

    /**
     * Get materi untuk export
     */
    @Query("SELECT * FROM materi_kaidah ORDER BY urutan ASC")
    List<MateriKaidah> getMateriForExport();

    /**
     * Update materi field dari API response
     */
    @Query("UPDATE materi_kaidah SET " +
            "judul_kaidah = :judul, " +
            "deskripsi = :deskripsi, " +
            "penjelasan = :penjelasan, " +
            "contoh = :contoh, " +
            "tingkat_kesulitan = :tingkat, " +
            "urutan = :urutan, " +
            "waktu_diubah = :waktuDiubah " +
            "WHERE id_materi = :id")
    int updateFromApi(int id, String judul, String deskripsi, String penjelasan, String contoh, String tingkat, int urutan, String waktuDiubah);

    /**
     * Batch insert dengan progress tracking
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAllBatch(List<MateriKaidah> materiList);

    /**
     * Get materi yang perlu diupdate dari server
     */
    @Query("SELECT * FROM materi_kaidah WHERE waktu_diubah < :lastSync ORDER BY id_materi ASC")
    List<MateriKaidah> getMateriNeedingUpdate(long lastSync);

    /**
     * Clean up materi yang tidak valid
     */
    @Query("DELETE FROM materi_kaidah WHERE judul_kaidah IS NULL OR judul_kaidah = ''")
    int cleanupInvalidMateri();

    /**
     * Get materi recommendation untuk siswa
     */
    @Query("SELECT mk.* FROM materi_kaidah mk " +
            "LEFT JOIN riwayat_belajar rb ON mk.id_materi = rb.id_materi AND rb.id_siswa = :siswaId " +
            "WHERE rb.id_riwayat IS NULL OR rb.status = 'belum_dimulai' OR " +
            "(rb.status = 'sedang_belajar' AND rb.persentase_penguasaan < 50) " +
            "ORDER BY mk.urutan ASC " +
            "LIMIT :limit")
    List<MateriKaidah> getRekomendasiMateri(int siswaId, int limit);
}