package com.demo.pomelo.data.domain.usecase

import com.demo.pomelo.data.entities.ClientTokenBody
import com.demo.pomelo.data.entities.UserTokenBody
import com.demo.pomelo.data.repository.ClientTokenRepository
import com.demo.pomelo.data.repository.UserTokenRepository
import com.pomelo.networking.auth.AuthToken.token
import com.pomelo.networking.resource.Resource
import com.pomelo.networking.utils.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AuthTokenUseCase(
    private val clientTokenRepository: ClientTokenRepository,
    private val userTokenRepository: UserTokenRepository,
) {
    fun getClientToken(body: ClientTokenBody) = flow {
        when (val resource = safeApiCall {
            clientTokenRepository.getClientToken(body)
        }) {
            is Resource.Success -> {
                emit(resource.data.tokenType + " " + resource.data.accessToken)
            }
            is Resource.Error -> emit("")
        }
    }.flowOn(Dispatchers.IO)


    suspend fun getUserToken(body: UserTokenBody, clientToken: String): String {
        return when (val resource =
            safeApiCall { userTokenRepository.getUserToken(body, clientToken) }) {
            is Resource.Success -> resource.data.accessToken
            is Resource.Error -> ""
        }
    }
}
