package com.example.habittracker.presentation.contracts

import com.example.habittracker.presentation.mvi.UiState

data class HabitState(
    val isLoading: Boolean = false,
    val habits: List<String> = emptyList(),
    val error: String? = null
): UiState