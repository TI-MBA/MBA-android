package com.gabia.mbaproject.model

import java.io.Serializable

data class RehearsalResponse(
    val id: Int,
    val date: String,
    val presenceList: List<PresenceResponse>
    ): Serializable