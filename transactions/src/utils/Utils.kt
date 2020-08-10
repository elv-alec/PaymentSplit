package com.example.utils
import at.favre.lib.crypto.bcrypt.BCrypt

object Utils {
    fun hash(plainText: String) : String {
        return BCrypt.withDefaults().hashToString(12, plainText.toCharArray())
    }

    fun verify(plain: String, hashed: String): Boolean {
        return BCrypt.verifyer().verify(plain.toCharArray(), hashed).verified
    }
}