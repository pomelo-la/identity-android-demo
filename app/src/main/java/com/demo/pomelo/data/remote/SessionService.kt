package com.demo.pomelo.data.remote

import com.demo.pomelo.data.entities.CreateSessionResponse
import com.demo.pomelo.data.entities.SessionBody
import com.demo.pomelo.data.entities.SessionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface SessionService {

    @POST("sessions")
    suspend fun createSession(
        @Body body: SessionBody,
        @Header("Authorization") clientToken: String
    ): Response<CreateSessionResponse>

    @GET("sessions/{session}")
    suspend fun checkSession(
        @Path("session") session: String,
        @Header("Authorization") clientToken: String
    ): Response<SessionResponse>
}
