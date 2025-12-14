@file:OptIn(ExperimentalSerializationApi::class)

package com.example.habittracker.data.remote.models

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class BaseResponse<T>(
    @JsonNames("Response", "response")
    val response: GenericResponse<T>? = null
)