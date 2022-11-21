package com.gabia.mbaproject.infrastructure.remote.api

import com.gabia.mbaproject.model.AuthRequest
import com.gabia.mbaproject.model.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthApiDataSource {

    @POST("api/user/signin")
    suspend fun login(@Body authRequest: AuthRequest): Response<User>

    @PUT("api/user/changePassword")
    suspend fun changePassword(@Body authRequest: AuthRequest): Response<Unit>
}