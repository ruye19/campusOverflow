package com.frontendmasters.app.navigation

import androidx.compose.runtime.*
import com.frontendmasters.app.ui.screens.auth.AuthState
import com.frontendmasters.app.ui.screens.auth.SignInScreen
import com.frontendmasters.app.ui.screens.auth.SignUpScreen
import com.frontendmasters.app.ui.screens.dashboard.AdminDashboardScreen
import com.frontendmasters.app.ui.screens.dashboard.UserDashboardScreen
import com.frontendmasters.app.ui.WelcomeScreen
import com.frontendmasters.app.ui.components.LoadingScreen
import com.frontendmasters.app.ui.screens.questions.UserQuestionPage
import com.frontendmasters.app.data.repository.QuestionRepository

@Composable
fun AppNavigation(
    authState: AuthState,
    onSignInSuccess: (String) -> Unit,
    onSignUpSuccess: (String) -> Unit,
    onSignOut: () -> Unit,
    questionRepository: QuestionRepository
) {
    var showSignIn by remember { mutableStateOf(false) }
    var showSignUp by remember { mutableStateOf(false) }

    when {
        authState.isLoading -> {
            LoadingScreen()
        }
        authState.isAuthenticated -> {
            when (authState.userRole) {
                "ADMIN" -> AdminDashboardScreen(onSignOut = onSignOut)
                else -> UserQuestionPage(
                    questionRepository = questionRepository,
                    authState = authState,
                    onSignOut = onSignOut
                )
            }
        }
        showSignIn -> {
            SignInScreen(
                onSignInSuccess = onSignInSuccess,
                onSignUpClick = { showSignIn = false; showSignUp = true },
                onBackClick = { showSignIn = false }
            )
        }
        showSignUp -> {
            SignUpScreen(
                onSignUpSuccess = onSignUpSuccess,
                onSignInClick = { showSignUp = false; showSignIn = true },
                onBackClick = { showSignUp = false }
            )
        }
        else -> {
            WelcomeScreen(
                onContinue = { showSignIn = true }
            )
        }
    }
} 