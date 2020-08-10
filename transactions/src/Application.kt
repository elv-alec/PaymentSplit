package com.example

import com.auth0.jwt.*
import com.auth0.jwt.algorithms.Algorithm
import com.example.db.TransactionUsers
import com.example.db.Transactions
import com.example.models.Transaction
import com.example.db.Users
import com.example.managers.impl.IdentityService
import com.example.managers.impl.UserService
import com.example.models.Authorized
import com.example.models.TransactionDTO
import com.example.routes.identityRoutes
import com.example.routes.userRoute
import com.example.utils.NetworkUtils.toUUID
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.sessions.*
import io.ktor.features.*
import io.ktor.auth.*
import com.fasterxml.jackson.databind.*
import io.ktor.auth.jwt.jwt
import io.ktor.jackson.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    install(Authentication) {
        jwt {
            verifier(IdentityService.verifier)
            realm = "application"
            validate {
                val userId = toUUID(it.payload.getClaim("id").asString())
                Authorized(userId)
            }
        }
    }

    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header("MyCustomHeader")
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    install(StatusPages) {
        exception<Throwable> {error ->
            call.respondText(error.localizedMessage, ContentType.Text.Plain, HttpStatusCode.InternalServerError)
        }
    }

    install(DefaultHeaders) {
        header("X-Engine", "Ktor") // will send this header with each response
    }

//    install(Authentication) {
//        basic("myBasicAuth") {
//            realm = "Ktor Server"
//            validate { if (it.name == "test" && it.password == "password") UserIdPrincipal(it.name) else null }
//        }
//    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    Database.connect("jdbc:postgresql://localhost:5961/transactions", driver = "org.postgresql.Driver",
        user = "test", password = "example")
    //Create tables

    transaction {
        SchemaUtils.create(Users)
        SchemaUtils.create(Transactions, TransactionUsers)
    }

    routing {
        identityRoutes(IdentityService())
        userRoute(UserService())

//        authenticate {
//            get ("/transactions") {
//                val transaction = transaction {
//                    Transaction.new {
//                        amount = 35
//                        ownerId = UUID.randomUUID()
//                    }
//                }
////                transaction {
////                    val user = User.all().first()
////                    transaction.requestedUsers = SizedCollection(listOf(user))
////                }
//
//                val tran = transaction {
//                    Transaction.all().first()
//                }
//                val tranDTO = TransactionDTO(tran.id.value, tran.amount, tran.ownerId, tran.requestedUsers.toList())
//                println(tranDTO)
//                call.respond(tranDTO)
//            }
//        }

        get("/session/increment") {
            val session = call.sessions.get<MySession>() ?: MySession()
            call.sessions.set(session.copy(count = session.count + 1))
            call.respondText("Counter is ${session.count}. Refresh to increment.")
        }


        get("/json/jackson") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}

data class MySession(val count: Int = 0)

private val algorithm = Algorithm.HMAC256("secret")
private fun makeJwtVerifier(issuer: String, audience: String): JWTVerifier = JWT
    .require(algorithm)
    .withAudience(audience)
    .withIssuer(issuer)
    .build()