package com.demo.pomelo.data.entities

import com.squareup.moshi.Json

data class UserTokenBody(
    @Json(name = "user_id") val userId: String
)
