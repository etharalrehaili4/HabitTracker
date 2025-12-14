package com.example.habittracker.data.remote.datasource

import com.example.habittracker.data.remote.api.HabitApiService
import javax.inject.Inject

class HabitRemoteDataSourceImpl @Inject constructor(
    private val apiService: HabitApiService
) {
    suspend fun addHabit(habitName: String) {
        // Implementation to add habit via API service
    }
}