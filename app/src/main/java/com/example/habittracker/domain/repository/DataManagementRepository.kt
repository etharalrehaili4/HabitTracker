package com.example.habittracker.domain.repository

interface DataManagementRepository {
    suspend fun clearAllData(): Boolean
}