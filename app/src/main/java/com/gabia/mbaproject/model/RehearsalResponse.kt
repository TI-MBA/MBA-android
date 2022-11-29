package com.gabia.mbaproject.model

data class RehearsalResponse(val id: Int, val date: String, val presenceList: List<PresenceResponse>)