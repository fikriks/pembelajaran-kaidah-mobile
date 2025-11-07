package com.khozin.pembelajarankaidah.database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.khozin.pembelajarankaidah.database.dao.*;
import com.khozin.pembelajarankaidah.data.model.*;

/**
 * Main Room Database class
 * Menghubungkan semua entities dan DAOs
 */
@Database(
    entities = {
        Siswa.class,
        MateriKaidah.class,
        Soal.class,
        Jawaban.class,
        SesiLatihan.class,
        DetailJawabanSiswa.class,
        RiwayatBelajar.class
    },
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    // DAOs
    public abstract SiswaDao siswaDao();
    public abstract MateriKaidahDao materiKaidahDao();
    public abstract SoalDao soalDao();
    public abstract JawabanDao jawabanDao();
    public abstract SesiLatihanDao sesiLatihanDao();
    public abstract DetailJawabanSiswaDao detailJawabanSiswaDao();
    public abstract RiwayatBelajarDao riwayatBelajarDao();

    // Singleton instance
    private static volatile AppDatabase INSTANCE;

    // Database name
    private static final String DATABASE_NAME = "pembelajaran_kaidah_db";

    /**
     * Get database instance (singleton)
     */
    @NonNull
    public static AppDatabase getDatabase(@NonNull Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = buildDatabase(context);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Build database instance
     */
    @NonNull
    private static AppDatabase buildDatabase(@NonNull Context context) {
        return Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class,
                DATABASE_NAME
        )
        .addMigrations(MIGRATION_1_2) // Add migrations when needed
        .addCallback(new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                // Database initialization logic
                initializeDatabase(db);
            }

            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
                // Database opened logic
                DatabaseUtils.enableForeignKeys(db);
                DatabaseUtils.configureWAL(db);
            }
        })
        .fallbackToDestructiveMigration() // For development
        .build();
    }

    /**
     * Reset singleton instance (for testing)
     */
    public static void resetInstance() {
        if (INSTANCE != null) {
            INSTANCE.close();
        }
        INSTANCE = null;
    }

    /**
     * Initialize database with default data
     */
    private static void initializeDatabase(@NonNull SupportSQLiteDatabase db) {
        // Enable foreign keys
        db.execSQL("PRAGMA foreign_keys=ON");

        // Create indexes for better performance
        createIndexes(db);

        // Insert default data if needed
        insertDefaultData(db);
    }

    /**
     * Create indexes for better query performance
     */
    private static void createIndexes(@NonNull SupportSQLiteDatabase db) {
        // Siswa indexes
        db.execSQL("CREATE INDEX IF NOT EXISTS index_siswa_nis ON siswa(nis)");
        db.execSQL("CREATE INDEX IF NOT EXISTS index_siswa_status ON siswa(status)");
        db.execSQL("CREATE INDEX IF NOT EXISTS index_siswa_kelas ON siswa(kelas)");

        // MateriKaidah indexes
        db.execSQL("CREATE INDEX IF NOT EXISTS index_materi_kaidah_urutan ON materi_kaidah(urutan)");
        db.execSQL("CREATE INDEX IF NOT EXISTS index_materi_kaidah_tingkat ON materi_kaidah(tingkat_kesulitan)");

        // SesiLatihan indexes
        db.execSQL("CREATE INDEX IF NOT EXISTS index_sesi_siswa ON sesi_latihan(id_siswa)");
        db.execSQL("CREATE INDEX IF NOT EXISTS index_sesi_materi ON sesi_latihan(id_materi)");
        db.execSQL("CREATE INDEX IF NOT EXISTS index_sesi_status ON sesi_latihan(status)");
        db.execSQL("CREATE INDEX IF NOT EXISTS index_sesi_seed ON sesi_latihan(seed_digunakan)");

        // RiwayatBelajar indexes
        db.execSQL("CREATE INDEX IF NOT EXISTS index_riwayat_siswa ON riwayat_belajar(id_siswa)");
        db.execSQL("CREATE INDEX IF NOT EXISTS index_riwayat_materi ON riwayat_belajar(id_materi)");
        db.execSQL("CREATE INDEX IF NOT EXISTS index_riwayat_status ON riwayat_belajar(status)");
    }

    /**
     * Insert default data
     */
    private static void insertDefaultData(@NonNull SupportSQLiteDatabase db) {
        // Insert default materi kaidah if needed
        // This can be implemented later with seeder classes
    }

    /**
     * Database migrations
     */
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Example migration logic
            // Add new columns, tables, or modify existing ones
        }
    };

    /**
     * Utility class for database operations
     */
    public static class DatabaseUtils {

        /**
         * Enable foreign keys
         */
        public static void enableForeignKeys(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("PRAGMA foreign_keys=ON");
        }

        /**
         * Configure WAL mode for better concurrency
         */
        public static void configureWAL(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("PRAGMA journal_mode=WAL");
        }

        /**
         * Get database size
         */
        public static long getDatabaseSize(@NonNull Context context) {
            java.io.File dbFile = context.getDatabasePath(DATABASE_NAME);
            return dbFile.exists() ? dbFile.length() : 0;
        }

        /**
         * Check if database exists
         */
        public static boolean databaseExists(@NonNull Context context) {
            java.io.File dbFile = context.getDatabasePath(DATABASE_NAME);
            return dbFile.exists();
        }

        /**
         * Delete database (for reset functionality)
         */
        public static boolean deleteDatabase(@NonNull Context context) {
            return context.deleteDatabase(DATABASE_NAME);
        }

        /**
         * Backup database
         */
        public static boolean backupDatabase(@NonNull Context context, @NonNull String backupPath) {
            try {
                java.io.File dbFile = context.getDatabasePath(DATABASE_NAME);
                java.io.File backupFile = new java.io.File(backupPath);

                if (dbFile.exists()) {
                    java.nio.file.Files.copy(dbFile.toPath(), backupFile.toPath(),
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        /**
         * Restore database from backup
         */
        public static boolean restoreDatabase(@NonNull Context context, @NonNull String backupPath) {
            try {
                java.io.File backupFile = new java.io.File(backupPath);
                java.io.File dbFile = context.getDatabasePath(DATABASE_NAME);

                if (backupFile.exists()) {
                    resetInstance(); // Reset instance first
                    java.nio.file.Files.copy(backupFile.toPath(), dbFile.toPath(),
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        /**
         * Get database path
         */
        @NonNull
        public static String getDatabasePath(@NonNull Context context) {
            return context.getDatabasePath(DATABASE_NAME).getAbsolutePath();
        }

        /**
         * Get database version
         */
        public static int getDatabaseVersion() {
            return 1; // Current version
        }

        /**
         * Check if database needs upgrade
         */
        public static boolean needsUpgrade(@NonNull Context context, int currentVersion) {
            return currentVersion < getDatabaseVersion();
        }
    }

    /**
     * Database seeder for initial data
     */
    public static class DatabaseSeeder {

        /**
         * Seed initial data
         */
        public static void seedInitialData(@NonNull AppDatabase database) {
            // This can be implemented to insert initial data
            // like default kaidah, sample soal, etc.
        }

        /**
         * Seed sample data for testing
         */
        public static void seedSampleData(@NonNull AppDatabase database) {
            // Insert sample siswa
            Siswa sampleSiswa = new Siswa();
            sampleSiswa.setNis("2021001");
            sampleSiswa.setNamaLengkap("Ahmad Rizki");
            sampleSiswa.setJenisKelamin("L");
            sampleSiswa.setKelas("XII IPA 1");
            sampleSiswa.setKataSandi("password123");
            sampleSiswa.setStatus("AKTIF");
            database.siswaDao().insert(sampleSiswa);

            // Insert sample materi kaidah
            MateriKaidah sampleMateri = new MateriKaidah();
            sampleMateri.setJudulKaidah("Isim Mufrad dan Jamak");
            sampleMateri.setDeskripsi("Pengenalan isim mufrad dan jamak dalam bahasa Arab");
            sampleMateri.setTingkatKesulitan("mudah");
            sampleMateri.setUrutan(1);
            database.materiKaidahDao().insert(sampleMateri);
        }
    }

    /**
     * Database maintenance utilities
     */
    public static class DatabaseMaintenance {

        /**
         * Vacuum database to reclaim space
         */
        public static void vacuum(@NonNull AppDatabase database) {
            database.getOpenHelper().getWritableDatabase().execSQL("VACUUM");
        }

        /**
         * Analyze database for query optimization
         */
        public static void analyze(@NonNull AppDatabase database) {
            database.getOpenHelper().getWritableDatabase().execSQL("ANALYZE");
        }

        /**
         * Check database integrity
         */
        public static boolean checkIntegrity(@NonNull AppDatabase database) {
            try {
                android.database.Cursor cursor = database.getOpenHelper().getReadableDatabase()
                        .rawQuery("PRAGMA integrity_check", null);

                if (cursor.moveToFirst()) {
                    String result = cursor.getString(0);
                    cursor.close();
                    return "ok".equals(result.toLowerCase());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        /**
         * Get database statistics
         */
        @NonNull
        public static String getDatabaseStats(@NonNull AppDatabase database) {
            StringBuilder stats = new StringBuilder();

            try {
                android.database.sqlite.SQLiteDatabase db = database.getOpenHelper().getReadableDatabase();

                // Get table sizes
                android.database.Cursor cursor = db.rawQuery(
                        "SELECT name FROM sqlite_master WHERE type='table'", null);

                while (cursor.moveToNext()) {
                    String tableName = cursor.getString(0);
                    android.database.Cursor countCursor = db.rawQuery(
                            "SELECT COUNT(*) FROM " + tableName, null);

                    if (countCursor.moveToFirst()) {
                        int count = countCursor.getInt(0);
                        stats.append(tableName).append(": ").append(count).append(" records\n");
                    }
                    countCursor.close();
                }
                cursor.close();

            } catch (Exception e) {
                e.printStackTrace();
                stats.append("Error getting statistics");
            }

            return stats.toString();
        }
    }
}