package com.example.utils

import at.favre.lib.crypto.bcrypt.BCrypt
import java.util.*


object NetworkUtils {
    fun toUUID(uuidStr: String?) : UUID {
        return try {
            UUID.fromString(uuidStr)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid UUID")
        }
    }
}