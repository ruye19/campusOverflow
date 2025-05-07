package com.example.jetpackcomposeauthui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcomposeauthui.ui.theme.JetpackComposeAuthUITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeAuthUITheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationView()
                }
            }
        }
    }
}

@Composable
fun NavigationView() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {
        composable("welcome") {
            WelcomeScreen(
                navController = navController,
                onLoginClick = { navController.navigate("login") },
                onSignupClick = { navController.navigate("signup") },
                onGuestClick = { navController.navigate("home") } // Add if you have guest mode
            )
        }
        composable("login") {
            LoginScreen(
                navController = navController,
                onLoginSuccess = { navController.navigate("home") }, // Add your home route
                onSignupClick = { navController.navigate("signup") },
                onForgotPassword = { navController.navigate("forgot_password") }
            )
        }
        composable("signup") {
            SignupScreen(
                navController = navController,
                onSignupSuccess = { navController.navigate("home") },
                onLoginClick = { navController.navigate("login") }
            )
        }
        // Add more screens as needed:
        // composable("home") { HomeScreen(navController) }
        // composable("forgot_password") { ForgotPasswordScreen(navController) }
    }
}