package com.example.habittracker.data.repository

import com.example.habittracker.data.remote.datasource.HabitRemoteDataSource
import com.example.habittracker.domain.repository.HabitRepository
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(
    private val remoteDataSource: HabitRemoteDataSource,
) : HabitRepository {
    override suspend fun addHabit(habitName: String) {
        // Implementation to add habit to the remote data source
    }
}