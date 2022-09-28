package com.demo.pomelo.data.repository

import com.demo.pomelo.data.entities.ClientTokenBody
import com.demo.pomelo.data.remote.ClientTokenService

class ClientTokenRepository(private val service: ClientTokenService) {
    suspend fun getClientToken(body: ClientTokenBody) = service.getClientToken(body)
}
