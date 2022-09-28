package com.demo.pomelo.data.entities

import com.squareup.moshi.Json

data class CreateSessionResponse(val data: CreateSessionData)

data class CreateSessionData(
    @Json(name = "id") val sessionID: String
)
