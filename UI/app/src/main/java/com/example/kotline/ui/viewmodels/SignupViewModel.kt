package com.example.kotline.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotline.ui.api.AuthApi
import com.example.kotline.ui.api.SignupRequest
import com.example.kotline.ui.api.SignupResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

sealed class SignupState {
    object Idle : SignupState()
    object Loading : SignupState()
    data class Success(val message: String) : SignupState()
    data class Error(val message: String) : SignupState()
}

class SignupViewModel : ViewModel() {
    private val _signupState = MutableStateFlow<SignupState>(SignupState.Idle)
    val signupState: StateFlow<SignupState> = _signupState

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // Use your computer's actual IP address here
    // For example: "http://192.168.1.100:5500/"
     private val BASE_URL = "http://192.168.4.125:5500/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val authApi = retrofit.create(AuthApi::class.java)

    fun signup(
        username: String,
        email: String,
        password: String,
        firstname: String,
        lastname: String,
        profession: String
    ) {
        // Input validation
        if (username.isBlank() || email.isBlank() || password.isBlank() || 
            firstname.isBlank() || lastname.isBlank() || profession.isBlank()) {
            _signupState.value = SignupState.Error("All fields are required")
            return
        }

        if (password.length < 8) {
            _signupState.value = SignupState.Error("Password must be at least 8 characters")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _signupState.value = SignupState.Error("Invalid email format")
            return
        }

        viewModelScope.launch {
            _signupState.value = SignupState.Loading
            try {
                Log.d("SignupViewModel", "Attempting to connect to: $BASE_URL")
                val response = authApi.signup(
                    SignupRequest(
                        username = username,
                        email = email,
                        password = password,
                        firstname = firstname,
                        lastname = lastname,
                        profession = profession
                    )
                )
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("SignupViewModel", "Signup successful: ${it.msg}")
                        _signupState.value = SignupState.Success(it.msg)
                    } ?: run {
                        Log.e("SignupViewModel", "Empty response from server")
                        _signupState.value = SignupState.Error("Empty response from server")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("SignupViewModel", "Signup failed: ${errorBody ?: response.message()}")
                    _signupState.value = SignupState.Error(
                        "Signup failed: ${errorBody ?: response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("SignupViewModel", "Network error", e)
                _signupState.value = SignupState.Error(
                    "Network error: ${e.message ?: "Unknown error occurred"}"
                )
            }
        }
    }
} 