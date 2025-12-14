package com.example.habittracker.presentation.contracts

import com.example.habittracker.presentation.mvi.UiIntent

sealed class HabitIntent : UiIntent {
    object LoadHabits : HabitIntent()
    data class AddHabit(val name: String) : HabitIntent()
    data class DeleteHabit(val id: String) : HabitIntent()
}