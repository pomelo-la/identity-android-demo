package com.demo.pomelo.data.entities

import com.squareup.moshi.Json

data class ClientTokenBody(
    @Json(name = "client_id") val clientId: String,
    @Json(name = "client_secret") val clientSecret: String,
    val audience: String,
    @Json(name = "grant_type") val grantType: String
)
