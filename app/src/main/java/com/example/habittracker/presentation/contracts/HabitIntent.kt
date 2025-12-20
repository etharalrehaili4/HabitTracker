package com.example.habittracker.presentation.contracts

import com.example.habittracker.presentation.mvi.UiIntent

sealed class HabitIntent : UiIntent {
    object getHabits : HabitIntent()
    data class GetHabitById(val id: String) : HabitIntent()
    data class AddHabit(val name: String) : HabitIntent()
    data class UpdateHabit(val id: String, val name: String) : HabitIntent()
    data class DeleteHabit(val id: String) : HabitIntent()
}