package com.example.managers

import com.example.models.Transaction
import com.example.models.TransactionDraft
import java.util.UUID

interface TransactionServiceInterface {
    fun createTransaction(transactionDraft: TransactionDraft) : Transaction
    fun deleteTransaction(id: UUID)
}