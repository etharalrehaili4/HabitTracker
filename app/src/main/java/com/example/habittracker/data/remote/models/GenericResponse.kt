package com.example.habittracker.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class GenericResponse<T>(
    @JsonNames("ResponseCode", "responseCode")
    val responseCode: Int? = null,

    @JsonNames("ResponseDesc", "responseDesc")
    val responseDesc: String? = null,

    @SerialName("Pagination")
    val pagination: Pagination? = null,

    @JsonNames("Data", "data")
    val data: T? = null,
) {

    @Serializable
    data class Pagination(

        @SerialName("currentPage")
        val currentPage: Int? = null,

        @SerialName("totalPages")
        val totalPages: Int? = null,

        @SerialName("totalItems")
        val totalItems: Int? = null,

        @SerialName("pageSize")
        val pageSize: Int? = null
    )
}