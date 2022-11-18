package com.gabia.mbaproject.infrastructure.api

import com.gabia.mbaproject.model.AuthRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApiDataSource {

    @POST("api/user/signup")
    suspend fun create()

    @GET("api/user/{id}")
    suspend fun getById(@Path("id") id: Int)

    // TOOD: MISSING BODY with user data
    @PUT("api/user/{id}")
    suspend fun edit(@Path("id") id: Int)

    @DELETE("api/user/{id}")
    suspend fun delete(@Path("id") id: Int)

    @PUT("api/user/resetPassword")
    suspend fun resetPassword(@Body authRequest: AuthRequest)

    @PUT("api/user/changePassword")
    suspend fun changePassword(@Body authRequest: AuthRequest)

}