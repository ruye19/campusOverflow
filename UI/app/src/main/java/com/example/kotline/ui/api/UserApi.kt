package com.example.kotline.ui.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.DELETE
import retrofit2.http.Path

// Data class for a user

data class UserListItem(
    val userid: String?,
    val firstname: String?,
    val lastname: String?,
    val profession: String?
)

data class UserListResponse(
    val users: List<UserListItem>
)

interface UserApi {
    @GET("api/users/getAllUserNamesAndProfessions")
    suspend fun getAllUsers(): Response<UserListResponse>

    @DELETE("api/users/{userid}")
    suspend fun deleteUser(@Path("userid") userId: String): Response<Unit>
} 