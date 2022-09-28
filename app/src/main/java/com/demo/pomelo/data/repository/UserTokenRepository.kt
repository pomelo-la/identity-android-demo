package com.demo.pomelo.data.repository

import com.demo.pomelo.data.entities.UserTokenBody
import com.demo.pomelo.data.remote.UserTokenService

class UserTokenRepository(private val service: UserTokenService) {
    suspend fun getUserToken(body: UserTokenBody, clientToken: String) =
        service.getUserToken(body, clientToken)
}
