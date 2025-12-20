package com.example.habittracker.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.habittracker.data.local.HabitDao
import com.example.habittracker.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

enum class AppLanguage(val code: String, val displayName: String) {
    ENGLISH("en", "English"),
    ARABIC("ar", "العربية")
}

enum class AppTheme {
    LIGHT,
    DARK,
    SYSTEM
}

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    @ApplicationContext private val context: Context,
    private val habitDao: HabitDao
) : SettingsRepository {

    companion object {
        private const val KEY_THEME = "app_theme"
        private const val KEY_LANGUAGE = "app_language"
        private const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
        private const val KEY_SYNC_ENABLED = "sync_enabled"
    }

    private val _isDarkMode = MutableStateFlow(loadTheme())
    override val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _language = MutableStateFlow(loadLanguage())
    override val language: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _isSecureStorageEnabled = MutableStateFlow(isSecureStorageEnabled())
    override val isSecureStorageEnabled: StateFlow<Boolean> = _isSecureStorageEnabled.asStateFlow()

    private fun isSecureStorageEnabled(): Boolean {
        return sharedPreferences.contains("encrypted_key")
    }

    override fun refreshSecureStorageState() {
        _isSecureStorageEnabled.value = isSecureStorageEnabled()
    }

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

            // Reset state flows to defaults (but keep secure storage enabled)
            _isDarkMode.value = false
            _language.value = AppLanguage.ENGLISH
            _isSecureStorageEnabled.value = isSecureStorageEnabled()

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
                // On error, try to at least reset the UI state
                _isDarkMode.value = loadTheme()
                _language.value = loadLanguage()
                _isSecureStorageEnabled.value = isSecureStorageEnabled()
            } catch (prefException: Exception) {
                // Ignore preference errors
            }
            false
        }
    }

    private fun loadTheme(): Boolean {
        return sharedPreferences.getBoolean(KEY_THEME, false)
    }

    private fun loadLanguage(): AppLanguage {
        val code = sharedPreferences.getString(KEY_LANGUAGE, AppLanguage.ENGLISH.code)
        return AppLanguage.entries.find { it.code == code } ?: AppLanguage.ENGLISH
    }

    override fun setDarkMode(isDark: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_THEME, isDark).apply()
        _isDarkMode.value = isDark
    }

    override fun setLanguage(language: AppLanguage) {
        sharedPreferences.edit().putString(KEY_LANGUAGE, language.code).apply()
        _language.value = language
    }
}