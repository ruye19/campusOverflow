package com.example.kotline.ui.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class SignupRequest(
    val username: String,
    val email: String,
    val password: String,
    val firstname: String,
    val lastname: String,
    val profession: String,
    val role_id: Int = 2 // Default role_id for regular users
)

data class SignupResponse(
    val msg: String,
    val role_id: Int
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val msg: String,
    val token: String,
    val role_id: Int
)

interface AuthApi {
    @POST("api/users/register")
    suspend fun signup(@Body request: SignupRequest): Response<SignupResponse>

    @POST("api/users/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
} 