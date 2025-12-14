package com.example.habittracker.data.remote.models

import kotlinx.serialization.Serializable

data class GenericResponse<T>(
    val data: T? = null,
    val message: String? = null,
    val error: ErrorData? = null
) {
    data class ErrorData(
        val code: Int,
        val message: String,
        val details: Map<String, List<String>>? = null
    )
}