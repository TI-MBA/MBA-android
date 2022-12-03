package com.gabia.mbaproject.model

import java.io.Serializable

data class PresenceResponse(
    val id: Int,
    val type: String,
    val rehearsal: String,
    val user: Member
): Serializable