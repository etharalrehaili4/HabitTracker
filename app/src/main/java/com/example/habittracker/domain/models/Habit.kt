package com.example.habittracker.domain.models

data class Habit(
    val id: String,
    val name: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)