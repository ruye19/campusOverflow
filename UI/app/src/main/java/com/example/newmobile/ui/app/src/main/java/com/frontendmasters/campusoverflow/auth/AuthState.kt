package com.frontendmasters.campusoverflow.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.frontendmasters.campusoverflow.model.User
import com.frontendmasters.campusoverflow.model.UserRole

object AuthState {
    var currentUser by mutableStateOf<User?>(null)
        private set

    val isAuthenticated: Boolean
        get() = currentUser != null

    fun signIn(user: User) {
        currentUser = user
    }

    fun signOut() {
        currentUser = null
    }
} 