package com.gabia.mbaproject.model

import java.io.Serializable

data class PaymentResponse(
    val id: Int,
    val observation: String,
    val paymentValue: Float,
    val date: String,
    val createdAt: String,
    val updatedAt: String
): Serializable