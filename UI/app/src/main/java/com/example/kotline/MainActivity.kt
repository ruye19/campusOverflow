package com.example.kotline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kotline.ui.screens.HomeScreen
import com.example.kotline.ui.screens.LoginScreen
import com.example.kotline.ui.screens.SignupScreen
import com.example.kotline.ui.screens.AskQuestionScreen
import com.example.kotline.ui.screens.AnswersScreen
import com.example.kotline.ui.theme.KotlineTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color
import android.util.Log
import com.google.gson.annotations.SerializedName

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlineTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { roleId ->
                    if (roleId == 1) {
                        navController.navigate("admin_dashboard")
                    } else {
                        navController.navigate("home")
                    }
                },
                onSignupClick = { navController.navigate("signup") }
            )
        }
        composable("signup") {
            SignupScreen(
                onSignupSuccess = { navController.navigate("login") }
            )
        }
        composable("home") {
            @OptIn(ExperimentalMaterial3Api::class)
            HomeScreen(
                userFirstName = AuthManager.firstName ?: "User",
                onAskClick = { navController.navigate("ask") },
                onSeeAnswersClick = { questionId -> navController.navigate("answers/$questionId") }
            )
        }
        composable("ask") {
            AskQuestionScreen(
                onBack = { navController.popBackStack() },
                onPostSuccess = {
                    navController.popBackStack("home", inclusive = false)
                }
            )
        }
        composable("answers/{questionId}") { backStackEntry ->
            val questionId = backStackEntry.arguments?.getString("questionId") ?: ""
            AnswersScreen(
                questionId = questionId,
                onBack = { navController.popBackStack() }
            )
        }
        composable("admin_dashboard") {
            com.example.kotline.ui.screens.AdminDashboardScreen(
                onBack = { navController.popBackStack() },
                onViewQuestions = { navController.navigate("home") },
                onViewUsers = { navController.navigate("admin_users") }
            )
        }
        composable("admin_users") {
            com.example.kotline.ui.screens.UserListScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}

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
