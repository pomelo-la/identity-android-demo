package com.demo.pomelo.data.entities

import com.squareup.moshi.Json

data class TokenResponse(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "token_type") val tokenType: String
)
