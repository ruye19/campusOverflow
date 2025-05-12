package com.example.kotline.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotline.ui.api.ApiClient
import com.example.kotline.ui.api.UserApi
import com.example.kotline.ui.api.UserListItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class UserListState {
    object Loading : UserListState()
    data class Success(val users: List<UserListItem>) : UserListState()
    data class Error(val message: String) : UserListState()
    object Deleting : UserListState()
    data class DeleteError(val message: String) : UserListState()
}

class UserListViewModel : ViewModel() {
    private val _userListState = MutableStateFlow<UserListState>(UserListState.Loading)
    val userListState: StateFlow<UserListState> = _userListState

    private val api = ApiClient.create(UserApi::class.java)

    fun fetchUsers() {
        viewModelScope.launch {
            _userListState.value = UserListState.Loading
            try {
                val response = api.getAllUsers()
                if (response.isSuccessful) {
                    val users = response.body()?.users ?: emptyList()
                    _userListState.value = UserListState.Success(users)
                } else {
                    _userListState.value = UserListState.Error("Failed to fetch users")
                }
            } catch (e: Exception) {
                _userListState.value = UserListState.Error("Network error: ${e.message}")
            }
        }
    }

    fun deleteUser(userId: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            _userListState.value = UserListState.Deleting
            try {
                val response = api.deleteUser(userId)
                if (response.isSuccessful) {
                    fetchUsers()
                    onComplete(true)
                } else {
                    _userListState.value = UserListState.DeleteError("Failed to delete user")
                    onComplete(false)
                }
            } catch (e: Exception) {
                _userListState.value = UserListState.DeleteError("Network error: ${e.message}")
                onComplete(false)
            }
        }
    }
} 