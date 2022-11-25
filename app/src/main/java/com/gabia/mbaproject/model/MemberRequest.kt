package com.gabia.mbaproject.model

data class MemberRequest(
    val email: String,
    val adminLevel: Int,
    val name: String,
    val instrument: String,
    val situation: String,
    val active: Boolean,
    val associated: Boolean
)

data class CreateMemberRequest(
    val email: String,
    val password: String,
    val adminLevel: Int,
    val name: String,
    val instrument: String,
    val situation: String,
    val active: Boolean,
    val associated: Boolean
) {
    constructor(member: MemberRequest) : this(
        member.email,
        "baque123",
        member.adminLevel,
        member.name,
        member.instrument,
        member.situation,
        member.active,
        member.associated
    )
}