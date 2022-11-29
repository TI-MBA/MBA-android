package com.gabia.mbaproject.model

data class PresenceResponse(
    val id: Int,
    val type: String,
    val rehearsal: String,
    val user: Member
)