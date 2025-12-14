package com.example.habittracker.presentation.contracts

import com.example.habittracker.presentation.mvi.UiIntent

sealed class HabitIntent : UiIntent {
    object getHabits : HabitIntent()
    data class AddHabit(val name: String) : HabitIntent()
}