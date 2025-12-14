package com.example.habittracker.presentation.contracts

import com.example.habittracker.domain.models.Habit
import com.example.habittracker.presentation.mvi.UiState

data class HabitState(
    val isLoading: Boolean = false,
    val habits: List<Habit> = emptyList(),
    val selectedHabit: Habit? = null,
    val error: String? = null,
    val successMessage: String? = null,
    val formState: HabitFormState = HabitFormState()
) : UiState

data class HabitFormState(
    val habitName: String = "",
    val nameError: String? = null,
    val isSubmitting: Boolean = false,
    val editingHabitId: String? = null
)