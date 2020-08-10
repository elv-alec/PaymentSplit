package com.example.routes

import com.example.managers.UserServiceInterface
import com.example.managers.impl.IdentityService
import com.example.models.Authorized
import com.example.utils.NetworkUtils.toUUID
import com.example.models.UserDraft
import io.ktor.application.call
import io.ktor.auth.Principal
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.auth.principal
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import java.util.*

fun Route.userRoute(userSvc: UserServiceInterface) {
    route("/users") {
        //Create a new User
        post("/") {
            val userData = call.receive<UserDraft>()
            val user = userSvc.createUser(userData)
            call.respond(user)
        }

        authenticate {
            //Get user
            get("/{userId}") {
                val id = toUUID(call.parameters["userId"])
                IdentityService.verifyIds(call.principal<Authorized>()!!.id, id)
                val user = userSvc.getUser(id)
                call.respond(user)
            }

            //Update user
            put("/{userId}") {
                val userData = call.receive<UserDraft>()
                val id = toUUID(call.parameters["userId"])
                IdentityService.verifyIds(call.principal<Authorized>()!!.id, id)
                val user = userSvc.updateUser(id, userData)
                call.respond(user)
            }

            //Delete user
            delete("/{userId}") {
                val id = toUUID(call.parameters["userId"])
                IdentityService.verifyIds(call.principal<Authorized>()!!.id, id)
                userSvc.deleteUser(id)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}