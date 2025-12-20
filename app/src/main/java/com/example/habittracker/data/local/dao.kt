package com.example.habittracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Insert
    suspend fun insertHabit(habit: HabitEntity)

    @Update
    suspend fun updateHabit(habit: HabitEntity)

    @Query("SELECT * FROM habit_table")
    fun getHabits(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habit_table WHERE id = :id")
    suspend fun getHabitById(id: String): HabitEntity?

    @Query("DELETE FROM habit_table WHERE id = :id")
    suspend fun deleteHabitById(id: String)

    @Query("DELETE FROM habit_table")
    suspend fun deleteAllHabits()
}