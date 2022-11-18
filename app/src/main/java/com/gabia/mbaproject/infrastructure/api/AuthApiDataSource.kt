package com.gabia.mbaproject.infrastructure.api

import com.gabia.mbaproject.model.AuthRequest
import com.gabia.mbaproject.model.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiDataSource {

    @POST("api/user/signin")
    suspend fun login(@Body authRequest: AuthRequest): Response<User>
}