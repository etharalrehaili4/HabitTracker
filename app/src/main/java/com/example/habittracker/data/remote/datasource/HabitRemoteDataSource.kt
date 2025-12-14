package com.example.habittracker.data.remote.datasource

interface HabitRemoteDataSource {
    suspend fun addHabit(habitName: String)
}