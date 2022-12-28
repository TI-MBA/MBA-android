package com.gabia.mbaproject.model

data class PaymentRequest(val observation: String, val paymentValue: Float, val date: String, val paymentDate: String, val user: PaymentUserRequest)
data class PaymentUserRequest(val id: Int)
data class UpdatePaymentRequest(val observation: String, val paymentValue: Float, val date: String, val paymentDate: String,)