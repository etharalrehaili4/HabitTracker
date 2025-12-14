package com.example.habittracker.data.remote.datasource

import com.example.habittracker.data.remote.api.HabitApiService
import com.example.habittracker.data.remote.models.mappers.toAddRequest
import com.example.habittracker.data.remote.models.mappers.toDomain
import com.example.habittracker.domain.models.Habit
import javax.inject.Inject

class HabitRemoteDataSourceImpl @Inject constructor(
    private val apiService: HabitApiService,
): HabitRemoteDataSource {

    override suspend fun getHabits(): Result<List<Habit>> {
        return try {
            val response = apiService.getHabits()
            val habits = response.response?.data?.toDomain() ?: emptyList()
            Result.success(habits)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addHabit(habit: Habit): Result<Habit> {
        return try {
            val response = apiService.addHabit(habit.toAddRequest())
            val addedHabit = response.response?.data?.toDomain()
                ?: throw IllegalStateException("Invalid response from server")
            Result.success(addedHabit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}