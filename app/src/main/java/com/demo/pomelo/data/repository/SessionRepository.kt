package com.demo.pomelo.data.repository

import com.pomelo.networking.utils.safeApiCall
import com.demo.pomelo.data.entities.SessionBody
import com.demo.pomelo.data.remote.SessionService
import kotlinx.coroutines.flow.flow

class SessionRepository(private val service: SessionService) {
    suspend fun createSession(body: SessionBody, clientToken: String) =
        service.createSession(body, clientToken)

    suspend fun checkSession(session: String, clientToken: String) =
        flow { emit(safeApiCall { service.checkSession(session, clientToken) }) }
}
