package com.example.newmobile.model

data class LoginRequest(val email: String, val password: String)

data class LoginResponse(val token: String? = null, val msg: String? = null)