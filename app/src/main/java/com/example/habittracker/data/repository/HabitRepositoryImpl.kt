package com.example.habittracker.data.repository

import com.example.habittracker.data.local.HabitDao
import com.example.habittracker.data.remote.datasource.HabitRemoteDataSource
import com.example.habittracker.data.remote.models.mappers.toDomain
import com.example.habittracker.data.remote.models.mappers.toEntity
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.repository.HabitRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(
//    private val remoteDataSource: HabitRemoteDataSource,
    private val habitDao: HabitDao
) : HabitRepository {

//    override suspend fun getHabits(): Result<List<Habit>> = remoteDataSource.getHabits()

    override suspend fun getHabits(): Result<List<Habit>> {
        return Result.success(habitDao.getHabits().first().map { it.toDomain() })
    }

//    override suspend fun addHabit(habit: Habit): Result<Habit> = remoteDataSource.addHabit(habit)

    override suspend fun addHabit(habit: Habit): Result<Habit> {
        return habitDao.insertHabit(habit.toEntity()).let {
            Result.success(habit)
        }
    }
}
