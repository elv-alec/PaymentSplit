package com.example.db

import com.example.models.User
import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.Table
import java.util.UUID

object Transactions : UUIDTable("Transactions") {
    val amount: Column<Int> = integer("amount")
    val ownerId: Column<UUID> = uuid("ownerId")
}

object TransactionUsers: Table() {
    val userId = reference("user", Users.id, onDelete = ReferenceOption.CASCADE).primaryKey()
    val movieId = reference("transaction", Transactions.id, onDelete = ReferenceOption.CASCADE).primaryKey()
}