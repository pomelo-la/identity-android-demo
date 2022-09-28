package com.demo.pomelo.data.remote

import com.demo.pomelo.data.entities.ClientTokenBody
import com.demo.pomelo.data.entities.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ClientTokenService {
    @POST("oauth/token/")
    suspend fun getClientToken(
        @Body body: ClientTokenBody
    ): Response<TokenResponse>
}
