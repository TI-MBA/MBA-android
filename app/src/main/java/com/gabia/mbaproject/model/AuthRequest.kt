package com.gabia.mbaproject.model

data class AuthRequest(
        val email: String,
        val password: String
)

data class EditPasswordRequest(
        val email: String,
        val password: String,
        val newPassword: String
)