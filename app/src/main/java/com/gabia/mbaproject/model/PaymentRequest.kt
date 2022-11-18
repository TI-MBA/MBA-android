package com.gabia.mbaproject.model

data class PaymentRequest(val observation: String, val paymentValue: Float, val date: String, val user: PaymentUserRequest)
data class PaymentUserRequest(val id: Int)