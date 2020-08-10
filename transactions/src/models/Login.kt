package com.example.models

import io.ktor.auth.Principal
import java.util.*

data class LoginPayload(
    val phone: String,
    val password: String
)

data class SessionResponse(
    val auth_token: String,
    val user: UserData
)

data class Authorized(val id: UUID) : Principal
