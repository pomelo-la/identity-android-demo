package com.demo.pomelo.data.repository

import com.demo.pomelo.data.entities.CreateUserBody
import com.demo.pomelo.data.remote.UserService

class UserRepository(private val service: UserService) {

    suspend fun getUserIDByEmail(email: String, clientToken: String) =
        service.getUserIDByEmail(email, clientToken)

    suspend fun createUser(body: CreateUserBody, clientToken: String) =
        service.createUser(body, clientToken)
}
