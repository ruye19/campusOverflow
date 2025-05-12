package com.example.newmobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.newmobile.AnswersPage
import com.example.newmobile.QuestionsPage
import com.example.newmobile.SignInScreen
import com.example.newmobile.SignUpScreen
import com.example.newmobile.UserListScreen
import com.example.newmobile.WelcomeScreen
import com.example.newmobile.model.DashboardStats

import com.example.newmobile.ui.theme.dashboard.DashboardScreen

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object SignIn : Screen("signin")
    object SignUp : Screen("signup")
    object Dashboard : Screen("dashboard")
    object Questions : Screen("questions")
    object Answers : Screen("answers")
    object UserList : Screen("userList")
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(navController = navController)
        }

        composable(Screen.SignIn.route) {
            SignInScreen(navController = navController)
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(navController = navController)
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                userName = "New User",
                role = "User",
                stats = DashboardStats(
                    questions = 0,
                    solutions = 0,
                    users = 0
                ),
                onBackClick = { navController.popBackStack() },
                onViewQuestions = { navController.navigate(Screen.Questions.route) },
                onViewUsers = { navController.navigate(Screen.UserList.route) }
            )
        }

        composable(Screen.Questions.route) {
            QuestionsPage(
                navController = navController,
            )
        }

        composable(Screen.Answers.route) {
            AnswersPage(navController = navController)
        }

        composable(Screen.UserList.route) {
            UserListScreen(navController = navController)
        }
    }
}