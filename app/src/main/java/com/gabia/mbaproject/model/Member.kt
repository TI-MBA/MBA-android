package com.gabia.mbaproject.model

import java.io.Serializable

data class Member(
    val id: Int,
    val email: String,
    val password: String,
    val adminLevel: Int,
    val name: String,
    val instrument: String,
    val situation: String,
    val createdAt: String,
    val updatedAt: String?,
    val associated: Boolean,
    val active: Boolean
): Serializable