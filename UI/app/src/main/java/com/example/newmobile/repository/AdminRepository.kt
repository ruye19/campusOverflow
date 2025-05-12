package com.frontendmasters.app.repository

import com.frontendmasters.app.data.remote.CampusOverflowApi
import com.frontendmasters.app.model.AdminDashboardData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminRepository @Inject constructor(
    private val api: CampusOverflowApi
) {
    suspend fun getDashboardData(): Result<AdminDashboardData> {
        return try {
            val response = api.getAdminDashboardData()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 