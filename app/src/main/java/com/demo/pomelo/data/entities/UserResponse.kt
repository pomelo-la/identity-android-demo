package com.demo.pomelo.data.entities

import com.squareup.moshi.Json

data class UserResponse(val data: List<UserData>? = null)

data class UserData(
    @Json(name = "id") val userID: String,
    val email: String,
    @Json(name = "operation_country") val operationCountry: String
)
