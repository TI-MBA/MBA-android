package com.gabia.mbaproject.model

data class User(
    val id: Int,
    val email: String,
    val password: String,
    val adminLevel: Int, // TODO: Update to use enum
    val name: String,
    val instrument: String, // TODO: Update to use enum
    val situation: String, // TODO: Update to use enum
    val createdAt: String,
    val updatedAt: String?,
    val associated: Boolean,
    val active: Boolean
)