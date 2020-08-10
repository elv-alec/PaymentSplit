package com.example.models
import java.util.UUID

data class User(
    val id: UUID,
    val name: String,
    val phone: String,
    val password: String,
    val plaidSecret: String? = null
)

data class UserDraft(
    val name: String,
    val phone: String,
    val password: String,
    val plaidSecret: String? = null
)

data class UserData(
    val id: UUID,
    val name: String,
    val phone: String
) {
    constructor(user: User) : this(
        user.id,
        user.name,
        user.phone
    )
}