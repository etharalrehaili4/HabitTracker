package com.example.habittracker.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HabitResponseData(
    @SerialName("habits")
    val habits: List<HabitDto>? = null
)

@Serializable
data class HabitDto(
    @SerialName("Id")
    val id: String? = null,
    @SerialName("Name")
    val name: String? = null,
    @SerialName("CreatedAt")
    val createdAt: Long? = null
)

@Serializable
data class AddHabitRequest(
    @SerialName("Name")
    val name: String?
)