package com.demo.pomelo.data.domain.usecase

import com.demo.pomelo.data.entities.CreateUserBody
import com.demo.pomelo.data.entities.Session
import com.demo.pomelo.data.repository.SessionRepository
import com.demo.pomelo.data.repository.UserRepository
import com.pomelo.networking.resource.Resource
import com.pomelo.networking.utils.safeApiCall
import com.demo.pomelo.data.entities.SessionBody
import kotlinx.coroutines.flow.flow

class SessionUseCase(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
) {

    fun getSessionID(
        email: String,
        country: String,
        clientToken: String,
    ) = flow {
        when (val resource = safeApiCall {
            userRepository.getUserIDByEmail(email, clientToken)
        }) {
            is Resource.Success -> {
                val data = resource.data.data ?: listOf()
                val userFromServer = data.firstOrNull { it.operationCountry == country }
                var userID = ""
                if (userFromServer == null) {
                    when (val user = safeApiCall {
                        userRepository.createUser(CreateUserBody(email, country), clientToken)
                    }) {
                        is Resource.Success -> {
                            userID = user.data.data.id
                        }
                        is Resource.Error -> emit(user)
                    }
                } else {
                    userID = userFromServer.userID
                }
                emit(getSessionID(userID, clientToken))
            }
            is Resource.Error -> emit(resource)
        }
    }

    private suspend fun getSessionID(
        userID: String,
        clientToken: String,
    ): Resource<Session> {
        return when (val resource =
            safeApiCall { sessionRepository.createSession(SessionBody(userID), clientToken) }) {
            is Resource.Success -> {
                Resource.Success(Session(userID, resource.data.data.sessionID))
            }
            is Resource.Error -> resource
        }
    }
}