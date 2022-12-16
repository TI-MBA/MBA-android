package com.gabia.mbaproject.model

import java.io.Serializable

data class PresenceResponse(
    val userId: Int,
    val presenceType: String,
    val presenceId: Int,
    val instrument: String,
    val name: String
): Serializable