package com.gabia.mbaproject.model

data class PresenceRequest(val type: String, val rehearsal: CreatePresenceRehearsalRequest, val user: CreatePresenceUserRequest)

data class CreatePresenceRehearsalRequest(val id: Int)

data class CreatePresenceUserRequest(val id: Int)