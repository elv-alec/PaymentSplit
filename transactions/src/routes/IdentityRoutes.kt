package com.example.routes


import com.example.managers.impl.IdentityService
import com.example.models.LoginPayload

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import java.lang.Exception
import javax.security.auth.login.FailedLoginException

fun Route.identityRoutes(identitySvc: IdentityService) {
    route("/login") {
        post("/") {
            val loginPayload = call.receive<LoginPayload>()
            try {
                call.respond(identitySvc.login(loginPayload))
            } catch (e: FailedLoginException) {
                call.respond(HttpStatusCode.Unauthorized)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Forbidden)
            }
        }
    }
}