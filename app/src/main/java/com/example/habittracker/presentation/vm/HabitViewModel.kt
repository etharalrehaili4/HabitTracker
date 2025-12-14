package com.example.habittracker.presentation.vm

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.usecase.AddHabitUseCase
import com.example.habittracker.domain.usecase.GetHabitByIdUseCase
import com.example.habittracker.domain.usecase.GetHabitsUseCase
import com.example.habittracker.presentation.contracts.HabitEffect
import com.example.habittracker.presentation.contracts.HabitFormState
import com.example.habittracker.presentation.contracts.HabitIntent
import com.example.habittracker.presentation.contracts.HabitState
import com.example.habittracker.presentation.mvi.BaseMviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val getHabitsUseCase: GetHabitsUseCase,
    private val getHabitByIdUseCase: GetHabitByIdUseCase,
    private val addHabitUseCase: AddHabitUseCase
) : BaseMviViewModel<HabitIntent, HabitState, HabitEffect>(
    initialState = HabitState()
) {

    companion object {
        private const val TAG = "HabitViewModel"
    }

    init {
        onEvent(HabitIntent.getHabits)
    }

    override fun onEvent(intent: HabitIntent) {
        when (intent) {
            is HabitIntent.getHabits -> handleFetchHabits()
            is HabitIntent.GetHabitById -> handleGetHabitById(intent.id)
            is HabitIntent.AddHabit -> handleAddHabit(intent.name)
        }
    }

    private fun handleFetchHabits() {
        setState { copy(isLoading = true, error = null) }

        viewModelScope.launch {
            getHabitsUseCase()
                .onSuccess { habits ->
                    setState {
                        copy(
                            isLoading = false,
                            habits = habits,
                            error = null
                        )
                    }
                }
                .onFailure { exception ->
                    val errorMessage = exception.message ?: "Failed to fetch habits"
                    setState {
                        copy(
                            isLoading = false,
                            error = errorMessage
                        )
                    }
                }
        }
    }

    private fun handleGetHabitById(id: String) {
        setState { copy(isLoading = true, error = null, selectedHabit = null) }

        viewModelScope.launch {
            getHabitByIdUseCase(id)
                .onSuccess { habit ->
                    setState {
                        copy(
                            isLoading = false,
                            selectedHabit = habit,
                            error = if (habit == null) "Habit not found" else null
                        )
                    }
                }
                .onFailure { exception ->
                    val errorMessage = exception.message ?: "Failed to fetch habit"
                    setState {
                        copy(
                            isLoading = false,
                            selectedHabit = null,
                            error = errorMessage
                        )
                    }
                }
        }
    }

    private fun handleAddHabit(name: String) {
        // Clear previous errors
        setState {
            copy(
                formState = formState.copy(nameError = null)
            )
        }

        // Validate input
        if (name.isBlank()) {
            setState {
                copy(
                    formState = formState.copy(
                        nameError = "Habit name cannot be empty"
                    )
                )
            }
            return
        }

        setState {
            copy(
                formState = formState.copy(
                    isSubmitting = true,
                    nameError = null
                )
            )
        }

        viewModelScope.launch {
            val habit = Habit(
                id = UUID.randomUUID().toString(),
                name = name.trim()
            )

            Log.d(TAG, "Attempting to add habit: id=${habit.id}, name=${habit.name}")

            addHabitUseCase(habit)
                .onSuccess {
                    Log.d(TAG, "Habit added successfully: id=${habit.id}")
                    setState {
                        copy(
                            formState = HabitFormState()
                        )
                    }
                    // Send effect to show success message
                    sendEffect { HabitEffect.ShowSuccess("Habit added successfully!") }
                    // Refresh the list
                    handleFetchHabits()
                }
                .onFailure { exception ->
                    Log.e(TAG, "Failed to add habit", exception)
                    setState {
                        copy(
                            formState = formState.copy(
                                isSubmitting = false,
                                nameError = exception.message ?: "Failed to add habit"
                            )
                        )
                    }
                }
        }
    }
}