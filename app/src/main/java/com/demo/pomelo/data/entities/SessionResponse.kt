package com.demo.pomelo.data.entities

import com.squareup.moshi.Json

data class SessionResponse(val data: SessionData)

data class SessionData(
    @Json(name = "user_id") val userId: String?,
    @Json(name = "operation_country") val operationCountry: String?
)
