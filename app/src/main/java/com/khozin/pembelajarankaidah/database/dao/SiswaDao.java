package com.khozin.pembelajarankaidah.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.khozin.pembelajarankaidah.data.model.Siswa;
import com.khozin.pembelajarankaidah.database.entity.SiswaStatistics;

import java.util.List;

/**
 * DAO untuk Siswa entity
 * Menghandle semua database operations untuk data siswa
 */
@Dao
public interface SiswaDao {

    /**
     * Insert siswa baru
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Siswa siswa);

    /**
     * Insert multiple siswa
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<Siswa> siswaList);

    /**
     * Update siswa
     */
    @Update
    int update(Siswa siswa);

    /**
     * Delete siswa
     */
    @Delete
    int delete(Siswa siswa);

    /**
     * Get siswa by ID
     */
    @Query("SELECT * FROM siswa WHERE id = :id")
    Siswa getById(int id);

    /**
     * Get siswa by NIS
     */
    @Query("SELECT * FROM siswa WHERE nis = :nis LIMIT 1")
    Siswa getByNis(String nis);

    /**
     * Get all siswa
     */
    @Query("SELECT * FROM siswa ORDER BY nama_lengkap ASC")
    LiveData<List<Siswa>> getAllSiswa();

    /**
     * Get all siswa (non-LiveData)
     */
    @Query("SELECT * FROM siswa ORDER BY nama_lengkap ASC")
    List<Siswa> getAllSiswaSync();

    /**
     * Get siswa aktif
     */
    @Query("SELECT * FROM siswa WHERE status = 'AKTIF' ORDER BY nama_lengkap ASC")
    LiveData<List<Siswa>> getActiveSiswa();

    /**
     * Get siswa nonaktif
     */
    @Query("SELECT * FROM siswa WHERE status = 'NONAKTIF' ORDER BY nama_lengkap ASC")
    LiveData<List<Siswa>> getInactiveSiswa();

    /**
     * Get siswa by kelas
     */
    @Query("SELECT * FROM siswa WHERE kelas = :kelas AND status = 'AKTIF' ORDER BY nama_lengkap ASC")
    LiveData<List<Siswa>> getSiswaByKelas(String kelas);

    /**
     * Get siswa by jenis kelamin
     */
    @Query("SELECT * FROM siswa WHERE jenis_kelamin = :jenisKelamin AND status = 'AKTIF' ORDER BY nama_lengkap ASC")
    LiveData<List<Siswa>> getSiswaByJenisKelamin(String jenisKelamin);

    /**
     * Search siswa by nama
     */
    @Query("SELECT * FROM siswa WHERE nama_lengkap LIKE '%' || :query || '%' AND status = 'AKTIF' ORDER BY nama_lengkap ASC")
    LiveData<List<Siswa>> searchSiswa(String query);

    /**
     * Search siswa by NIS atau nama
     */
    @Query("SELECT * FROM siswa WHERE (nis LIKE '%' || :query || '%' OR nama_lengkap LIKE '%' || :query || '%') AND status = 'AKTIF' ORDER BY nama_lengkap ASC")
    LiveData<List<Siswa>> searchSiswaByNisOrNama(String query);

    /**
     * Get total count siswa
     */
    @Query("SELECT COUNT(*) FROM siswa")
    int getTotalCount();

    /**
     * Get total siswa aktif
     */
    @Query("SELECT COUNT(*) FROM siswa WHERE status = 'AKTIF'")
    int getActiveCount();

    /**
     * Get total siswa nonaktif
     */
    @Query("SELECT COUNT(*) FROM siswa WHERE status = 'NONAKTIF'")
    int getInactiveCount();

    /**
     * Get count by kelas
     */
    @Query("SELECT COUNT(*) FROM siswa WHERE kelas = :kelas AND status = 'AKTIF'")
    int getCountByKelas(String kelas);

    /**
     * Get count by jenis kelamin
     */
    @Query("SELECT COUNT(*) FROM siswa WHERE jenis_kelamin = :jenisKelamin AND status = 'AKTIF'")
    int getCountByJenisKelamin(String jenisKelamin);

    /**
     * Get all unique kelas
     */
    @Query("SELECT DISTINCT kelas FROM siswa WHERE status = 'AKTIF' ORDER BY kelas ASC")
    List<String> getAllKelas();

    /**
     * Check NIS exists
     */
    @Query("SELECT EXISTS(SELECT 1 FROM siswa WHERE nis = :nis)")
    boolean isNisExists(String nis);

    /**
     * Check NIS exists (excluding current ID)
     */
    @Query("SELECT EXISTS(SELECT 1 FROM siswa WHERE nis = :nis AND id != :excludeId)")
    boolean isNisExistsExcluding(String nis, int excludeId);

    /**
     * Update status siswa
     */
    @Query("UPDATE siswa SET status = :status WHERE id = :id")
    int updateStatus(int id, String status);

    /**
     * Update status multiple siswa
     */
    @Query("UPDATE siswa SET status = :status WHERE id IN (:ids)")
    int updateStatusMultiple(List<Integer> ids, String status);

    /**
     * Update kelas siswa
     */
    @Query("UPDATE siswa SET kelas = :kelas WHERE id = :id")
    int updateKelas(int id, String kelas);

    /**
     * Update kata sandi
     */
    @Query("UPDATE siswa SET kata_sandi = :kataSandi WHERE id = :id")
    int updateKataSandi(int id, String kataSandi);

    /**
     * Delete all siswa
     */
    @Query("DELETE FROM siswa")
    int deleteAll();

    /**
     * Delete siswa by ID
     */
    @Query("DELETE FROM siswa WHERE id = :id")
    int deleteById(int id);

    /**
     * Get siswa for export
     */
    @Query("SELECT id, nis, nama_lengkap, jenis_kelamin, kelas, status, kata_sandi, waktu_dibuat, waktu_diubah FROM siswa ORDER BY id ASC")
    List<Siswa> getSiswaForExport();

    /**
     * Get latest login activity (jika ada tracking login)
     */
    @Query("SELECT * FROM siswa WHERE status = 'AKTIF' ORDER BY id DESC LIMIT 1")
    Siswa getLatestActiveSiswa();

    /**
     * Get siswa statistics
     */
    @Query("SELECT " +
            "COUNT(*) as total, " +
            "COUNT(CASE WHEN status = 'AKTIF' THEN 1 END) as aktif, " +
            "COUNT(CASE WHEN status = 'NONAKTIF' THEN 1 END) as nonaktif, " +
            "COUNT(CASE WHEN jenis_kelamin = 'L' THEN 1 END) as laki_laki, " +
            "COUNT(CASE WHEN jenis_kelamin = 'P' THEN 1 END) as perempuan " +
            "FROM siswa")
    SiswaStatistics getSiswaStatistics();

    /**
     * Batch insert dengan progress callback
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAllBatch(List<Siswa> siswaList);

    /**
     * Get siswa by status untuk background processing
     */
    @Query("SELECT * FROM siswa WHERE status = :status ORDER BY id ASC")
    List<Siswa> getSiswaByStatusSync(String status);

    /**
     * Cleanup old data (hapus siswa yang sudah lama nonaktif)
     */
    @Query("DELETE FROM siswa WHERE status = 'NONAKTIF' AND waktu_diubah < :cutoffTime")
    int cleanupOldInactiveData(long cutoffTime);
}