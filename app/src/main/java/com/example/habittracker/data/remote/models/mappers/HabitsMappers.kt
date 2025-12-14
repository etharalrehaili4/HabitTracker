package com.example.habittracker.data.remote.models.mappers

import com.example.habittracker.data.local.HabitEntity
import com.example.habittracker.data.remote.models.AddHabitRequest
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.data.remote.models.HabitDto
import com.example.habittracker.data.remote.models.HabitResponseData

fun HabitResponseData.toDomain(): List<Habit> {
    return habits?.mapNotNull { it.toDomain() } ?: emptyList()
}

fun HabitDto.toDomain(): Habit? {
    return if (id != null && name != null) {
        Habit(
            id = id,
            name = name
        )
    } else null
}

fun HabitEntity.toDomain(): Habit {
    return Habit(
        id = id,
        name = name,
        createdAt = createdAt
    )
}

fun Habit.toEntity(): HabitEntity {
    return HabitEntity(
        id = id,
        name = name,
        createdAt = createdAt
    )
}

fun Habit.toAddRequest(): AddHabitRequest {
    return AddHabitRequest(name = name)
}