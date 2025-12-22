package com.example.habittracker.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.habittracker.data.local.HabitDao
import com.example.habittracker.domain.models.ENGLISH_MODEL
import com.example.habittracker.domain.repository.DataManagementRepository
import com.example.habittracker.domain.repository.LanguageRepository
import com.example.habittracker.domain.repository.ThemeRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataManagementRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    @ApplicationContext private val context: Context,
    private val habitDao: HabitDao,
    private val themeRepository: ThemeRepository,
    private val languageRepository: LanguageRepository
) : DataManagementRepository {

    companion object {
        private val ENCRYPTED_KEY = stringPreferencesKey("encrypted_key")
        private val ENCRYPTION_IV = stringPreferencesKey("encryption_iv")
    }

    override suspend fun clearAllData(): Boolean {
        return try {
            // Read encryption keys before clearing
            val preferences = dataStore.data.first()
            val encryptedKey = preferences[ENCRYPTED_KEY]
            val encryptionIv = preferences[ENCRYPTION_IV]

            // Try to clear database - delete all habits
            try {
                habitDao.deleteAllHabits()
            } catch (dbException: Exception) {
                // If database is corrupted, we'll handle it by clearing preferences only
            }

            // Clear all DataStore preferences
            dataStore.edit { it.clear() }

            // Restore encryption keys if they existed
            if (encryptedKey != null && encryptionIv != null) {
                dataStore.edit { preferences ->
                    preferences[ENCRYPTED_KEY] = encryptedKey
                    preferences[ENCRYPTION_IV] = encryptionIv
                }
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