package com.example.habittracker.domain.repository

interface SettingsRepository {
    suspend fun clearAllData(): Boolean
}