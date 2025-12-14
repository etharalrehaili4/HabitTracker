package com.example.habittracker.presentation.contracts

import com.example.habittracker.presentation.mvi.UiEffect

sealed class HabitEffect : UiEffect {
    data class ShowError(val message: String) : HabitEffect()
    data class ShowSuccess(val message: String) : HabitEffect()
    object NavigateBack : HabitEffect()
}