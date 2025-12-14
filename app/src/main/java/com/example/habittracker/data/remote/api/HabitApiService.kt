package com.example.habittracker.data.remote.api

import com.example.habittracker.data.remote.models.BaseResponse
import com.example.habittracker.data.remote.models.AddHabitRequest
import com.example.habittracker.data.remote.models.HabitDto
import com.example.habittracker.data.remote.models.HabitResponseData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface HabitApiService {
    @GET("/habits")
    suspend fun getHabits(): BaseResponse<HabitResponseData>

    @POST("/addHabit")
    suspend fun addHabit(@Body request: AddHabitRequest): BaseResponse<HabitDto>
}