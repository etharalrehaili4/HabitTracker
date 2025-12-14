package com.example.habittracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habit_table")
data class HabitEntity(
    @PrimaryKey val id: String,
    val name: String,
)