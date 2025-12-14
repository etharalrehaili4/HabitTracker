package com.example.habittracker.data.repository

import com.example.habittracker.data.remote.datasource.HabitRemoteDataSource
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.repository.HabitRepository
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(
    private val remoteDataSource: HabitRemoteDataSource,
) : HabitRepository {

    override suspend fun getHabits(): Result<List<Habit>> = remoteDataSource.getHabits()
    override suspend fun addHabit(habit: Habit): Result<Habit> = remoteDataSource.addHabit(habit)
}