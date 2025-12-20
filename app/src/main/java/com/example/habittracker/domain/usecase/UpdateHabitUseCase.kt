package com.example.habittracker.domain.usecase

import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.repository.HabitRepository
import javax.inject.Inject

class UpdateHabitUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(habit: Habit): Result<Unit> {
        return try {
            repository.updateHabit(habit)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}