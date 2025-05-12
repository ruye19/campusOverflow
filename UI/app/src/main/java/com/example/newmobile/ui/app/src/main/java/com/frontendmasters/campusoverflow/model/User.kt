package com.frontendmasters.campusoverflow.model

enum class UserRole {
    ADMIN,
    USER
}

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val role: UserRole = UserRole.USER // Default role is USER
) 