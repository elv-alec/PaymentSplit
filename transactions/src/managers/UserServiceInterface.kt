package com.example.managers
import com.example.models.User
import com.example.models.UserDraft
import java.util.UUID

interface UserServiceInterface {
    fun createUser(userData: UserDraft) : UUID
    fun getUser(id: UUID) : User
    fun updateUser(id: UUID, user: UserDraft): UUID
    fun deleteUser(id: UUID)
}