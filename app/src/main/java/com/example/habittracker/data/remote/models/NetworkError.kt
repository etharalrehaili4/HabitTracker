package com.example.habittracker.data.remote.models

import com.example.habittracker.data.network.NetworkFailure

data class NetworkError(
    val networkFailure: NetworkFailure? = null,
    val networkValidation: NetworkValidationError? = null,
    val localMessage: Int,
    val remoteMessage: String? = null,
    val hasValidationError: Boolean = false
)