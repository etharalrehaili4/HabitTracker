package com.example.habittracker.data.di

import com.example.habittracker.data.remote.datasource.HabitRemoteDataSource
import com.example.habittracker.data.remote.datasource.HabitRemoteDataSourceImpl
import com.example.habittracker.data.repository.HabitRepositoryImpl
import com.example.habittracker.data.repository.LanguageRepositoryImpl
import com.example.habittracker.data.repository.DataManagementRepositoryImpl
import com.example.habittracker.data.repository.ThemeRepositoryImpl
import com.example.habittracker.domain.repository.HabitRepository
import com.example.habittracker.domain.repository.LanguageRepository
import com.example.habittracker.domain.repository.DataManagementRepository
import com.example.habittracker.domain.repository.ThemeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHabitRepository(
        habitRepositoryImpl: HabitRepositoryImpl
    ): HabitRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: DataManagementRepositoryImpl
    ): DataManagementRepository

     @Binds
     @Singleton
     abstract fun bindHabitRemoteDataSource(
         habitRemoteDataSourceImpl: HabitRemoteDataSourceImpl
     ): HabitRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindLanguageRepository(
        languageRepositoryImpl: LanguageRepositoryImpl
    ): LanguageRepository

    @Binds
    @Singleton
    abstract fun bindThemeRepository(
        themeRepositoryImpl: ThemeRepositoryImpl
    ): ThemeRepository

}