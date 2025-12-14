package com.example.habittracker.data.di

import android.content.Context
import androidx.room.Room
import com.example.habittracker.data.local.HabitDao
import com.example.habittracker.data.local.HabitDatabase
import com.example.habittracker.data.local.SqlCipherKeyManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideHabitDatabase(
        @ApplicationContext context: Context,
        sqlCipherKeyManager: SqlCipherKeyManager
    ): HabitDatabase {

        val dbFile = context.getDatabasePath("habit_database")

        return Room.databaseBuilder(
            context,
            HabitDatabase::class.java,
            dbFile.absolutePath
        )
            .openHelperFactory(sqlCipherKeyManager.getSupportFactory())
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideHabitDao(database: HabitDatabase): HabitDao =
        database.HabitDao()
}
