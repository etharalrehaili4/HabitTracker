package com.example.habittracker.presentation.vm

import com.example.habittracker.presentation.contracts.HabitEffect
import com.example.habittracker.presentation.contracts.HabitIntent
import com.example.habittracker.presentation.contracts.HabitState
import com.example.habittracker.presentation.mvi.BaseMviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(

) : BaseMviViewModel<HabitIntent, HabitState, HabitEffect>(
    initialState = HabitState()
) {
    override fun onEvent(intent: HabitIntent) {
        when (intent) {
            is HabitIntent.fetchHabits -> {
                // No Impl
            }
            is HabitIntent.AddHabit -> {
                // No Impl
            }
            is HabitIntent.EditHabit -> {
                // No Impl
            }
            is HabitIntent.DeleteHabit -> {
                // No Impl
            }
        }
    }
}