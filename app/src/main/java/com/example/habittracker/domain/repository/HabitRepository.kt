package com.example.habittracker.domain.repository

interface HabitRepository {
    suspend fun addHabit(habitName: String)
}