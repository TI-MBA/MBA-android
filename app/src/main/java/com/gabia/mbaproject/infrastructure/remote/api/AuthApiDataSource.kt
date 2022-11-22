package com.gabia.mbaproject.infrastructure.remote.api

import com.gabia.mbaproject.model.AuthRequest
import com.gabia.mbaproject.model.Member
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthApiDataSource {

    @POST("api/user/signin")
    suspend fun login(@Body authRequest: AuthRequest): Response<Member>

    @PUT("api/user/changePassword")
    suspend fun changePassword(@Body authRequest: AuthRequest): Response<Unit>

    @PUT("api/user/resetPassword")
    suspend fun resetPassword(@Body authRequest: AuthRequest): Response<Unit>
}