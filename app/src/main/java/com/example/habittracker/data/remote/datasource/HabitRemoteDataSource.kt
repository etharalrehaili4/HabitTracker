package com.example.habittracker.data.remote.datasource

import com.example.habittracker.domain.models.Habit

interface HabitRemoteDataSource {
    suspend fun getHabits(): Result<List<Habit>>
    suspend fun addHabit(habit: Habit): Result<Habit>
}