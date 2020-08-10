package com.example.managers.impl

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.example.db.Users
import com.example.managers.IdentityServiceInterface
import com.example.models.*
import com.example.utils.Utils
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import javax.security.auth.login.FailedLoginException

class IdentityService : IdentityServiceInterface {
    companion object {
        private val secret = "i9Aj9o6R23Hi6enPKwEjdfdMEHxfxHe"
        private val issuer = "com.transactions"
        private val validityInMs = 604800000 // 10 hours
        private val algorithm = Algorithm.HMAC512(secret)

        val verifier: JWTVerifier = JWT
            .require(algorithm)
            .withIssuer(issuer)
            .build()

        fun verifyIds(auth_id: UUID, user_id: UUID) {
            if (auth_id != user_id) throw FailedLoginException()
        }
    }

    override fun newToken(user: User): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("id", user.id.toString())
        .withExpiresAt(getExpiration())
        .sign(algorithm)

    private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)

    override fun login(loginPayload: LoginPayload) : SessionResponse {
        val user = Users.toUser(
            transaction {
                Users.select { Users.phone eq loginPayload.phone }.first()
            }
        )
        if (Utils.verify(loginPayload.password, user.password)) {
            return SessionResponse(
                auth_token = newToken(user),
                user = UserData(user)
            )
        } else { throw FailedLoginException() }
    }
}