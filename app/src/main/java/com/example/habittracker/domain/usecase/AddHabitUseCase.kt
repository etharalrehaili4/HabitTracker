package com.example.habittracker.domain.usecase

import com.example.habittracker.domain.repository.HabitRepository
import javax.inject.Inject

class AddHabitUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(habitName: String) {
        // Implementation to add habit to the repository
        repository.addHabit(habitName)
    }
}