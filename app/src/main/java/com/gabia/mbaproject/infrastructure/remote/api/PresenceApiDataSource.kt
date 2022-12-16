package com.gabia.mbaproject.infrastructure.remote.api

import com.gabia.mbaproject.model.PresenceRequest
import com.gabia.mbaproject.model.PresenceResponse
import retrofit2.Response
import retrofit2.http.*

interface PresenceApiDataSource {

    @GET("api/presence")
    suspend fun getAll(): Response<List<PresenceResponse>>

    @POST("api/presence")
    suspend fun create(@Body request: PresenceRequest): Response<PresenceResponse>

    @PUT("api/presence/{id}")
    suspend fun update(@Path("id") id: Int, @Body request: PresenceRequest): Response<PresenceResponse>

    @DELETE("api/presence/{id}")
    suspend fun delete(@Path("id") id: Int): Response<Unit>
}