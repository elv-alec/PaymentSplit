package com.example.models

import com.example.db.TransactionUsers
import com.example.db.Transactions
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import java.util.UUID

data class TransactionDTO(
    val id: UUID = UUID.randomUUID(),
    val amount: Int,
    val ownerId: UUID,
    val requestedUsers: List<User> = listOf()
) {
//    constructor(transactionDraft: TransactionDraft) {
//        Transaction(amount = transactionDraft.amount, ownerId = transactionDraft.ownerId)
//    }
}

data class TransactionDraft(
    val amount: Int,
    val ownerId: UUID
)

class Transaction(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Transaction>(Transactions)

    var amount by Transactions.amount
    var ownerId by Transactions.ownerId

}