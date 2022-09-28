package com.demo.pomelo.data.entities

data class CreateUserResponse(val data: CreateUserData)

data class CreateUserData(
    val id: String,
    val email: String
)
