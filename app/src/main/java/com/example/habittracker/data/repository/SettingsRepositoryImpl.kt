package com.example.habittracker.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.habittracker.data.local.HabitDao
import com.example.habittracker.domain.models.ENGLISH_MODEL
import com.example.habittracker.domain.repository.LanguageRepository
import com.example.habittracker.domain.repository.SettingsRepository
import com.example.habittracker.domain.repository.ThemeRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    @ApplicationContext private val context: Context,
    private val habitDao: HabitDao,
    private val themeRepository: ThemeRepository,
    private val languageRepository: LanguageRepository
) : SettingsRepository {

    override suspend fun clearAllData(): Boolean {
        return try {
            // Save encryption keys before clearing
            val encryptedKey = sharedPreferences.getString("encrypted_key", null)
            val encryptionIv = sharedPreferences.getString("encryption_iv", null)

            // Try to clear database - delete all habits
            try {
                habitDao.deleteAllHabits()
            } catch (dbException: Exception) {
                // If database is corrupted, we'll handle it by clearing preferences only
            }

            // Clear preferences but preserve encryption keys
            sharedPreferences.edit()
                .clear()
                .apply()

            // Restore encryption keys
            if (encryptedKey != null && encryptionIv != null) {
                sharedPreferences.edit()
                    .putString("encrypted_key", encryptedKey)
                    .putString("encryption_iv", encryptionIv)
                    .apply()
            }

            // Reset theme to light mode
            themeRepository.setDarkMode(false)

            // Reset language to English
            languageRepository.setLanguage(ENGLISH_MODEL)

            // Delete the actual database files
            val dbFile = context.getDatabasePath("habit_database")
            val dbWalFile = context.getDatabasePath("habit_database-wal")
            val dbShmFile = context.getDatabasePath("habit_database-shm")

            dbFile.delete()
            dbWalFile.delete()
            dbShmFile.delete()

            true
        } catch (e: Exception) {
            try {
                // Reset theme and language even if other operations fail
                themeRepository.setDarkMode(false)
                languageRepository.setLanguage(ENGLISH_MODEL)
            } catch (prefException: Exception) {
                // Ignore preference errors
            }
            false
        }
    }
}