package com.example.habittracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Insert
    suspend fun insertHabit(habit: HabitEntity)

    @Query("SELECT * FROM habit_table")
    fun getHabits(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habit_table WHERE id = :id")
    suspend fun getHabitById(id: String): HabitEntity?

    @Query("DELETE FROM habit_table")
    suspend fun clearAll()
}