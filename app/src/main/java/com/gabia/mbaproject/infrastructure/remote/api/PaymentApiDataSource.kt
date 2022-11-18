package com.gabia.mbaproject.infrastructure.remote.api

import com.gabia.mbaproject.model.PaymentResponse
import retrofit2.Response
import retrofit2.http.*

interface PaymentApiDataSource {

    @GET("api/payment/user/{userId}")
    suspend fun getByUserId(@Path("userId") id: Int): Response<List<PaymentResponse>>
}