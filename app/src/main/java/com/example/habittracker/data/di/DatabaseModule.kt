package com.example.habittracker.data.di

import android.content.Context
import androidx.room.Room
import com.example.habittracker.data.local.HabitDao
import com.example.habittracker.data.local.HabitDatabase
import com.example.habittracker.data.local.SqlCipherKeyManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideHabitDatabase(
        @ApplicationContext context: Context,
        sqlCipherKeyManager: SqlCipherKeyManager
    ): HabitDatabase {

        val dbFile = context.getDatabasePath("habit_database")
        val dbWalFile = context.getDatabasePath("habit_database-wal")
        val dbShmFile = context.getDatabasePath("habit_database-shm")

        // Ensure database directory exists
        val dbDir = dbFile.parentFile
        dbDir?.mkdirs()

        // Check if database exists but encryption key doesn't match
        // This happens after clearing data when the key was regenerated
        if (dbFile.exists()) {
            val fileSize = dbFile.length()

            // Delete if empty or suspiciously small
            if (fileSize == 0L || (fileSize > 0 && fileSize < 2048)) {
                try {
                    dbFile.delete()
                    if (dbWalFile.exists()) dbWalFile.delete()
                    if (dbShmFile.exists()) dbShmFile.delete()
                } catch (e: Exception) {
                    try {
                        if (dbFile.exists()) {
                            dbFile.renameTo(java.io.File(dbFile.parent, "${dbFile.name}.corrupted"))
                        }
                    } catch (renameException: Exception) {
                        // Ignore rename errors
                    }
                }
            } else {
                // Try to detect if the database is encrypted with wrong key
                // by attempting a quick validation
                try {
                    val factory = sqlCipherKeyManager.getSupportFactory()
                    val helper = factory.create(
                        androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration.builder(context)
                            .name(dbFile.absolutePath)
                            .callback(object : androidx.sqlite.db.SupportSQLiteOpenHelper.Callback(1) {
                                override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {}
                                override fun onUpgrade(
                                    db: androidx.sqlite.db.SupportSQLiteDatabase,
                                    oldVersion: Int,
                                    newVersion: Int
                                ) {}
                            })
                            .build()
                    )

                    // Try to open and validate
                    try {
                        val db = helper.writableDatabase
                        // Try a simple query to validate the database
                        db.query("SELECT count(*) FROM sqlite_master")
                        db.close()
                    } catch (e: Exception) {
                        // Database is corrupted or wrong key - delete it
                        helper.close()
                        dbFile.delete()
                        if (dbWalFile.exists()) dbWalFile.delete()
                        if (dbShmFile.exists()) dbShmFile.delete()
                    }
                } catch (e: Exception) {
                    // If validation fails, delete the database
                    try {
                        dbFile.delete()
                        if (dbWalFile.exists()) dbWalFile.delete()
                        if (dbShmFile.exists()) dbShmFile.delete()
                    } catch (deleteException: Exception) {
                        // Ignore
                    }
                }
            }
        }

        return Room.databaseBuilder(
            context,
            HabitDatabase::class.java,
            dbFile.absolutePath
        )
            .openHelperFactory(sqlCipherKeyManager.getSupportFactory())
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideHabitDao(database: HabitDatabase): HabitDao =
        database.HabitDao()
}