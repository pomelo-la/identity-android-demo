package com.demo.pomelo.data.entities

import com.squareup.moshi.Json

data class CreateUserBody(
    val email: String,
    @Json(name = "operation_country") val operationCountry: String
)
