package com.frontendmasters.app.repository

import com.frontendmasters.app.data.remote.CampusOverflowApi
import com.frontendmasters.app.model.LoginRequest
import com.frontendmasters.app.model.LoginResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: CampusOverflowApi
) {
    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = api.login(LoginRequest(email, password))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(name: String, email: String, password: String, profession: String): Result<LoginResponse> {
        return try {
            val response = api.register(name, email, password, profession)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 