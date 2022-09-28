package com.demo.pomelo.data.remote

import com.demo.pomelo.data.entities.CreateUserBody
import com.demo.pomelo.data.entities.CreateUserResponse
import com.demo.pomelo.data.entities.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {
    @GET(".")
    suspend fun getUserIDByEmail(
        @Query("filter[email]") email: String,
        @Header("Authorization") clientToken: String
    ): Response<UserResponse>

    @POST(".")
    suspend fun createUser(
        @Body body: CreateUserBody,
        @Header("Authorization") clientToken: String
    ): Response<CreateUserResponse>
}
