package com.example.managers.impl

import com.example.db.Users
import com.example.managers.UserServiceInterface
import com.example.models.User
import com.example.models.UserDraft
import com.example.utils.Utils
import io.ktor.features.NotFoundException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class UserService : UserServiceInterface {
    override fun createUser(userData: UserDraft) : UUID {
        return transaction {
            Users.insertAndGetId {
                it[name] = userData.name
                it[phone] = userData.phone
                it[password] = Utils.hash(userData.password)
            }.value
        }
    }

    override fun getUser(id: UUID) : User {
        return transaction {
            Users.findById(id) ?: throw NotFoundException("User not found")
        }
    }

    override fun updateUser(id: UUID, user: UserDraft): UUID {
        transaction {
            Users.update({Users.id eq id}) {
                it[name] = user.name
                it[phone] = user.phone
                it[password] = user.password
                it[plaidSecret] = user.plaidSecret
            }
        }
        return id
    }

    override fun deleteUser(id: UUID) {
        transaction {
            val user = Users.deleteWhere { Users.id eq id }
        }
    }
}