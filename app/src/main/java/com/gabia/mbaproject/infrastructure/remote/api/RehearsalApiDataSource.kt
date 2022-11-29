package com.gabia.mbaproject.infrastructure.remote.api

import com.gabia.mbaproject.model.RehearsalRequest
import com.gabia.mbaproject.model.RehearsalResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RehearsalApiDataSource {

    @GET("api/rehearsal")
    suspend fun getAll(): Response<List<RehearsalResponse>>

    @POST("api/rehearsal")
    suspend fun create(@Body request: RehearsalRequest): Response<RehearsalResponse>

    @GET("api/rehearsal/{id}")
    suspend fun getById(@Path("id") id: Int): Response<RehearsalResponse>
}