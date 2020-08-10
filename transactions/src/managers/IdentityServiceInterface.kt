package com.example.managers

import com.example.models.LoginPayload
import com.example.models.SessionResponse
import com.example.models.User

interface IdentityServiceInterface {
    fun newToken(user: User): String
    fun login(loginPayload: LoginPayload) : SessionResponse
}