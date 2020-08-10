package com.example.db

import com.example.models.User
import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.lang.Exception
import java.util.*

object Users : UUIDTable("Users") {
    val name: Column<String> = varchar("name", 255)
    val phone: Column<String> = varchar("phone", 255).uniqueIndex()
    val password: Column<String> = varchar("password", 255)
    val plaidSecret: Column<String?> = varchar("plaid_secret", 255).nullable()

    fun toUser(row: ResultRow) = User(
        id = row[id].value,
        name = row[name],
        phone = row[phone],
        password = row[password],
        plaidSecret = row[plaidSecret]
    )
    /**
     * Find a User row by UUID and return it as a User object.
     * @param [id] Id of the user we want to find
     * @return [User] instance if it exists, otherwise null
     * */
    fun findById(id: UUID) : User? {
        return try {
            toUser(transaction { Users.select { Users.id eq id } }.first())
        } catch (e : Exception) { null }
    }
}
