package com.demo.pomelo.data.remote

import com.demo.pomelo.data.entities.TokenResponse
import com.demo.pomelo.data.entities.UserTokenBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface UserTokenService {
    @POST("token")
    suspend fun getUserToken(
        @Body body: UserTokenBody,
        @Header("Authorization") clientToken: String
    ): Response<TokenResponse>
}
