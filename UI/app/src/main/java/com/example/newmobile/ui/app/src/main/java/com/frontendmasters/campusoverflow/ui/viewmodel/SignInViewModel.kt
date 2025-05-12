package com.frontendmasters.campusoverflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frontendmasters.campusoverflow.model.User
import com.frontendmasters.campusoverflow.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<SignInUiState>(SignInUiState.Initial)
    val uiState: StateFlow<SignInUiState> = _uiState

    // Mock user data for demonstration
    private val mockUsers = listOf(
        User("admin", "admin@campus.com", UserRole.ADMIN),
        User("user1", "user1@campus.com", UserRole.USER),
        User("user2", "user2@campus.com", UserRole.USER)
    )

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = SignInUiState.Loading
            // Simulate authentication (no real password check for mock)
            val user = mockUsers.find { it.email == email }
            if (user != null) {
                _uiState.value = SignInUiState.Success(user)
            } else {
                _uiState.value = SignInUiState.Error("Invalid email or password")
            }
        }
    }
}

sealed class SignInUiState {
    object Initial : SignInUiState()
    object Loading : SignInUiState()
    data class Success(val user: User) : SignInUiState()
    data class Error(val message: String) : SignInUiState()
} 