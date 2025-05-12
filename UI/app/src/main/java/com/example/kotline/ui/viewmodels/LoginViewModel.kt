package com.example.kotline.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotline.AuthManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit
import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val msg: String,
    val token: String,
    val user: UserInfo
)

data class UserInfo(
    val userid: String,
    val username: String,
    val role_id: Int,
    val role: String,
    val firstname: String? // Make nullable for safety
)

interface AuthApi {
    @POST("api/users/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val message: String, val token: String, val firstName: String, val roleId: Int) : LoginState()
    data class Error(val message: String) : LoginState()
}

class LoginViewModel : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val BASE_URL = "http://192.168.4.125:5500/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val authApi = retrofit.create(AuthApi::class.java)

    fun login(email: String, password: String) {
        // Input validation
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Email and password are required")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _loginState.value = LoginState.Error("Invalid email format")
            return
        }

        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                Log.d("LoginViewModel", "Attempting to login with email: $email")
                val response = authApi.login(
                    LoginRequest(
                        email = email,
                        password = password
                    )
                )
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("LoginViewModel", "Login successful")
                        AuthManager.token = it.token
                        AuthManager.firstName = it.user.firstname ?: "User"
                        AuthManager.userId = it.user.userid
                        AuthManager.roleId = it.user.role_id
                        _loginState.value = LoginState.Success(
                            message = it.msg,
                            token = it.token,
                            firstName = it.user.firstname ?: "User",
                            roleId = it.user.role_id
                        )
                    } ?: run {
                        Log.e("LoginViewModel", "Empty response from server")
                        _loginState.value = LoginState.Error("Empty response from server")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("LoginViewModel", "Login failed: ${errorBody ?: response.message()}")
                    _loginState.value = LoginState.Error(
                        "Login failed: ${errorBody ?: response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Network error", e)
                _loginState.value = LoginState.Error(
                    "Network error: ${e.message ?: "Unknown error occurred"}"
                )
            }
        }
    }

    private suspend fun loginUser(username: String, password: String) {
        try {
            _loginState.value = LoginState.Loading
            val response = authApi.login(LoginRequest(username, password))
            
            // Add logging to see the raw response
            Log.d("LoginViewModel", "Raw response: ${response.body()}")
            
            if (response.isSuccessful) {
                val loginResponse = response.body()
                Log.d("LoginViewModel", "Parsed response: $loginResponse")
                
                if (loginResponse != null) {
                    // Store the token
                    AuthManager.token = loginResponse.token
                    // Store the firstname from user object
                    AuthManager.firstName = loginResponse.user.firstname ?: "User"
                    
                    _loginState.value = LoginState.Success(
                        message = loginResponse.msg,
                        token = loginResponse.token,
                        firstName = loginResponse.user.firstname ?: "User",
                        roleId = loginResponse.user.role_id
                    )
                } else {
                    _loginState.value = LoginState.Error("Invalid response from server")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("LoginViewModel", "Error response: $errorBody")
                _loginState.value = LoginState.Error("Login failed: ${errorBody ?: "Unknown error"}")
            }
        } catch (e: Exception) {
            Log.e("LoginViewModel", "Exception during login", e)
            _loginState.value = LoginState.Error("Network error: ${e.message}")
        }
    }
} 