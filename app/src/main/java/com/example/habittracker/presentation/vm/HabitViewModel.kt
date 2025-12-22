package com.example.habittracker.presentation.vm

import androidx.lifecycle.viewModelScope
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.usecase.AddHabitUseCase
import com.example.habittracker.domain.usecase.DeleteHabitUseCase
import com.example.habittracker.domain.usecase.GetHabitByIdUseCase
import com.example.habittracker.domain.usecase.GetHabitsUseCase
import com.example.habittracker.domain.usecase.UpdateHabitUseCase
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
    private val addHabitUseCase: AddHabitUseCase,
    private val updateHabitUseCase: UpdateHabitUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase
) : BaseMviViewModel<HabitIntent, HabitState, HabitEffect>(
    initialState = HabitState()
) {

    init {
        onEvent(HabitIntent.getHabits)
    }

    override fun onEvent(intent: HabitIntent) {
        when (intent) {
            is HabitIntent.getHabits -> handleFetchHabits()
            is HabitIntent.GetHabitById -> handleGetHabitById(intent.id)
            is HabitIntent.AddHabit -> handleAddHabit(intent.name)
            is HabitIntent.UpdateHabit -> handleUpdateHabit(intent.id, intent.name)
            is HabitIntent.DeleteHabit -> handleDeleteHabit(intent.id)
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

            addHabitUseCase(habit)
                .onSuccess {
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

    private fun handleUpdateHabit(id: String, name: String) {
        if (name.isBlank()) {
            sendEffect { HabitEffect.ShowError("Habit name cannot be empty") }
            return
        }

        setState { copy(isLoading = true) }

        viewModelScope.launch {
            val habit = Habit(
                id = id,
                name = name.trim(),
                createdAt = uiState.value.selectedHabit?.createdAt ?: System.currentTimeMillis()
            )

            updateHabitUseCase(habit)
                .onSuccess {
                    setState { copy(isLoading = false, selectedHabit = habit) }
                    sendEffect { HabitEffect.ShowSuccess("Habit updated successfully!") }
                    handleFetchHabits()
                }
                .onFailure { exception ->
                    setState { copy(isLoading = false) }
                    sendEffect { HabitEffect.ShowError(exception.message ?: "Failed to update habit") }
                }
        }
    }

    private fun handleDeleteHabit(id: String) {
        setState { copy(isLoading = true) }

        viewModelScope.launch {
            deleteHabitUseCase(id)
                .onSuccess {
                    setState { copy(isLoading = false) }
                    sendEffect { HabitEffect.ShowSuccess("Habit deleted successfully!") }
                    sendEffect { HabitEffect.NavigateBack }
                    handleFetchHabits()
                }
                .onFailure { exception ->
                    setState { copy(isLoading = false) }
                    sendEffect { HabitEffect.ShowError(exception.message ?: "Failed to delete habit") }
                }
        }
    }
}