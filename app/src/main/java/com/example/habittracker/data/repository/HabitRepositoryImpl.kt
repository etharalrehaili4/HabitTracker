package com.example.habittracker.data.repository

import com.example.habittracker.data.local.HabitDao
import com.example.habittracker.data.remote.models.mappers.toDomain
import com.example.habittracker.data.remote.models.mappers.toEntity
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.repository.HabitRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao
) : HabitRepository {

    override suspend fun getHabits(): Result<List<Habit>> {
        return Result.success(habitDao.getHabits().first().map { it.toDomain() })
    }

    override suspend fun getHabitById(id: String): Result<Habit?> {
        return try {
            val habitEntity = habitDao.getHabitById(id)
            Result.success(habitEntity?.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addHabit(habit: Habit): Result<Habit> {
        return habitDao.insertHabit(habit.toEntity()).let {
            Result.success(habit)
        }
    }
}
