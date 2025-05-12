package com.frontendmasters.campusoverflow.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.frontendmasters.campusoverflow.*
import com.frontendmasters.campusoverflow.ui.dashboard.DashboardScreen
import com.frontendmasters.campusoverflow.model.UserRole
import com.frontendmasters.campusoverflow.model.DashboardStats
import com.frontendmasters.campusoverflow.auth.AuthState

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object SignIn : Screen("signin")
    object SignUp : Screen("signup")
    object Dashboard : Screen("dashboard/{role}") {
        fun createRoute(role: UserRole) = "dashboard/${role.name}"
    }
    object Questions : Screen("questions/{role}/{userName}") {
        fun createRoute(role: UserRole, userName: String) = "questions/${role.name}/$userName"
    }
    object AdminQuestions : Screen("admin/questions")
    object Answers : Screen("answers/{questionId}")
    object UserList : Screen("admin/users")
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = if (AuthState.isAuthenticated) {
            // If authenticated, determine start destination based on user role
            when (AuthState.currentUser?.role) {
                UserRole.ADMIN -> Screen.Dashboard.createRoute(UserRole.ADMIN)
                UserRole.USER -> Screen.Questions.createRoute(UserRole.USER, AuthState.currentUser?.username ?: "")
                null -> Screen.Welcome.route
            }
        } else {
            Screen.Welcome.route
        }
    ) {
        composable(Screen.Welcome.route) {
            WelcomePage(navController = navController)
        }
        
        composable(Screen.SignIn.route) {
            SignInPage(
                navController = navController,
                onSignInSuccess = { userName, role ->
                    // Create user and sign in
                    val user = User(
                        id = 1, // This should come from your backend
                        firstName = userName,
                        lastName = "",
                        username = userName,
                        email = "",
                        role = role
                    )
                    AuthState.signIn(user)
                    
                    // Navigate to appropriate landing page based on role
                    when (role) {
                        UserRole.ADMIN -> navController.navigate(Screen.Dashboard.createRoute(role)) {
                            popUpTo(Screen.SignIn.route) { inclusive = true }
                        }
                        UserRole.USER -> navController.navigate(Screen.Questions.createRoute(role, userName)) {
                            popUpTo(Screen.SignIn.route) { inclusive = true }
                        }
                    }
                }
            )
        }
        
        composable(Screen.SignUp.route) {
            SignUpPage(
                navController = navController,
                onSignUpSuccess = { userName ->
                    // Create user and sign in
                    val user = User(
                        id = "1", // This should come from your backend
                        firstName = userName,
                        lastName = "",
                        username = userName,
                        email = "",
                        role = UserRole.USER
                    )
                    AuthState.signIn(user)
                    
                    // New users are always regular users
                    navController.navigate(Screen.Questions.createRoute(UserRole.USER, userName)) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Dashboard.route) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role")?.let { 
                UserRole.valueOf(it)
            } ?: UserRole.USER
            
            // Only allow access to dashboard for admins
            if (role == UserRole.ADMIN && AuthState.currentUser?.role == UserRole.ADMIN) {
                val userName = AuthState.currentUser?.username ?: "Admin"
                
                DashboardScreen(
                    userName = userName,
                    role = role.name,
                    stats = DashboardStats(
                        questions = 244,
                        solutions = 844,
                        users = 644
                    ),
                    onBackClick = { navController.popBackStack() },
                    onViewQuestions = { navController.navigate(Screen.AdminQuestions.route) },
                    onViewUsers = { navController.navigate(Screen.UserList.route) }
                )
            } else {
                // Redirect non-admin users to their questions page
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Questions.createRoute(role, AuthState.currentUser?.username ?: "User")) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            }
        }
        
        composable(Screen.Questions.route) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role")?.let { 
                UserRole.valueOf(it)
            } ?: UserRole.USER
            
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            
            // Regular users see UserQuestionsPage
            UserQuestionsPage(
                navController = navController,
                onBackClick = { navController.popBackStack() },
                userName = userName
            )
        }

        // Admin-only questions page
        composable(Screen.AdminQuestions.route) {
            if (AuthState.currentUser?.role == UserRole.ADMIN) {
                QuestionsPage(
                    navController = navController,
                    onBackClick = { navController.popBackStack() },
                    userRole = UserRole.ADMIN
                )
            } else {
                // Redirect non-admin users to their questions page
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Questions.createRoute(UserRole.USER, AuthState.currentUser?.username ?: "User")) {
                        popUpTo(Screen.AdminQuestions.route) { inclusive = true }
                    }
                }
            }
        }
        
        composable(Screen.Answers.route) { backStackEntry ->
            val questionId = backStackEntry.arguments?.getString("questionId") ?: ""
            AnswersPage(navController = navController)
        }
        
        // Admin-only users page
        composable(Screen.UserList.route) {
            if (AuthState.currentUser?.role == UserRole.ADMIN) {
                User_List(navController = navController)
            } else {
                // Redirect non-admin users to their questions page
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Questions.createRoute(UserRole.USER, AuthState.currentUser?.username ?: "User")) {
                        popUpTo(Screen.UserList.route) { inclusive = true }
                    }
                }
            }
        }
    }
} 