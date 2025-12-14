package com.example.habittracker.domain.usecase

import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.repository.HabitRepository
import javax.inject.Inject

class GetHabitByIdUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(id: String): Result<Habit?> {
        return repository.getHabitById(id)
    }
}