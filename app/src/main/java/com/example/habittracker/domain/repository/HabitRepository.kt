package com.example.habittracker.domain.repository

import com.example.habittracker.domain.models.Habit

interface HabitRepository {
    suspend fun getHabits(): Result<List<Habit>>
    suspend fun getHabitById(id: String): Result<Habit?>
    suspend fun addHabit(habit: Habit): Result<Habit>
    suspend fun updateHabit(habit: Habit)
    suspend fun deleteHabit(habitId: String)
}