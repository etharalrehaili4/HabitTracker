package com.example.habittracker.domain.repository

import com.example.habittracker.data.repository.AppLanguage
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val isDarkMode: StateFlow<Boolean>
    val language: StateFlow<AppLanguage>
    val isSecureStorageEnabled: StateFlow<Boolean>

    fun setDarkMode(isDark: Boolean)
    fun setLanguage(language: AppLanguage)
    fun refreshSecureStorageState()
    suspend fun clearAllData(): Boolean
}