package com.example.habittracker.data.di

import com.example.habittracker.data.remote.datasource.HabitRemoteDataSource
import com.example.habittracker.data.remote.datasource.HabitRemoteDataSourceImpl
import com.example.habittracker.data.repository.HabitRepositoryImpl
import com.example.habittracker.domain.repository.HabitRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindHabitRepository(
        habitRepositoryImpl: HabitRepositoryImpl
    ): HabitRepository

    @Binds
    @Singleton
    abstract fun bindHabitRemoteDataSource(
        habitRemoteDataSourceImpl: HabitRemoteDataSourceImpl
    ): HabitRemoteDataSource
}