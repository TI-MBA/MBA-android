package com.gabia.mbaproject.infrastructure.remote.api

import com.gabia.mbaproject.model.PaymentRequest
import com.gabia.mbaproject.model.PaymentResponse
import com.gabia.mbaproject.model.UpdatePaymentRequest
import retrofit2.Response
import retrofit2.http.*

interface PaymentApiDataSource {

    @GET("api/payment/user/{userId}")
    suspend fun getByUserId(@Path("userId") id: Int): Response<List<PaymentResponse>>

    @POST("api/payment")
    suspend fun createPayment(@Body payment: PaymentRequest): Response<PaymentResponse>

    @PUT("api/payment/{paymentId}")
    suspend fun updatePayment(@Path("paymentId") paymentId: Int, @Body payment: UpdatePaymentRequest): Response<PaymentResponse>

    @DELETE("api/payment/{paymentId}")
    suspend fun deletePayment(@Path("paymentId") paymentId: Int): Response<Unit>
}