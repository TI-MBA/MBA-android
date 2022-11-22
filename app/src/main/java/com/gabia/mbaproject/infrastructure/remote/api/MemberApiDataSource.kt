package com.gabia.mbaproject.infrastructure.remote.api

import com.gabia.mbaproject.model.Member
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MemberApiDataSource {

    @POST("api/user/signup")
    suspend fun create()

    @GET("api/user")
    suspend fun getAll(): Response<List<Member>>

    @GET("api/user/{id}")
    suspend fun getById(@Path("id") id: Int)

    // TOOD: MISSING BODY with user data
    @PUT("api/user/{id}")
    suspend fun edit(@Path("id") id: Int)

    @DELETE("api/user/{id}")
    suspend fun delete(@Path("id") id: Int)
}