package com.example.models

import java.util.*

abstract class BaseResponse(val status: String)

abstract class BaseRequest(
    val user_id : UUID,
    val token: String
)

abstract class Identifiable(
    val id: UUID
)
