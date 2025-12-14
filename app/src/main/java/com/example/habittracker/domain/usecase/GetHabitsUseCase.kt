package com.example.habittracker.domain.usecase

import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.repository.HabitRepository
import javax.inject.Inject

class GetHabitsUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(): Result<List<Habit>> = repository.getHabits()
}
