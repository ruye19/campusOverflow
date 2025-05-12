package com.frontendmasters.campusoverflow

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.frontendmasters.campusoverflow.model.UserRole
import com.frontendmasters.campusoverflow.navigation.Screen
import com.frontendmasters.campusoverflow.auth.AuthState
import com.frontendmasters.campusoverflow.viewmodel.SignInViewModel
import com.frontendmasters.campusoverflow.viewmodel.SignInUiState

@Composable
fun SignInPage(
    navController: NavController,
    viewModel: SignInViewModel = remember { SignInViewModel() }
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    // Check if user is already authenticated
    LaunchedEffect(Unit) {
        if (AuthState.isAuthenticated) {
            when (AuthState.currentUser?.role) {
                UserRole.ADMIN -> navController.navigate(Screen.Dashboard.createRoute(UserRole.ADMIN)) {
                    popUpTo(Screen.SignIn.route) { inclusive = true }
                }
                UserRole.USER -> navController.navigate(Screen.Questions.createRoute(UserRole.USER, AuthState.currentUser?.username ?: "")) {
                    popUpTo(Screen.SignIn.route) { inclusive = true }
                }
                null -> {} // Do nothing if no role
            }
        }
    }

    // Handle authentication success
    LaunchedEffect(uiState) {
        when (uiState) {
            is SignInUiState.Success -> {
                val user = (uiState as SignInUiState.Success).user
                AuthState.signIn(user)
                when (user.role) {
                    UserRole.ADMIN -> navController.navigate(Screen.Dashboard.createRoute(UserRole.ADMIN)) {
                        popUpTo(Screen.SignIn.route) { inclusive = true }
                    }
                    UserRole.USER -> navController.navigate(Screen.Questions.createRoute(UserRole.USER, user.username)) {
                        popUpTo(Screen.SignIn.route) { inclusive = true }
                    }
                }
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF181824), Color.Black)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(48.dp))
                // Logo
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "Campus Overflow Logo",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.height(48.dp))
                // Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        cursorColor = Color.Black,
                        focusedLabelColor = Color.Gray,
                        unfocusedLabelColor = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Password Field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        cursorColor = Color.Black,
                        focusedLabelColor = Color.Gray,
                        unfocusedLabelColor = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))
                // Sign In Button
                Button(
                    onClick = { 
                        if (email.isNotEmpty() && password.isNotEmpty()) {
                            viewModel.signIn(email, password)
                        }
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF9800),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    enabled = uiState !is SignInUiState.Loading
                ) {
                    if (uiState is SignInUiState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Text("Sign In", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Show error message if any
                if (uiState is SignInUiState.Error) {
                    Text(
                        text = (uiState as SignInUiState.Error).message,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            // Sign Up Button at the bottom
            TextButton(
                onClick = { navController.navigate(Screen.SignUp.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    "Don't have an account? Sign Up",
                    color = Color(0xFFFF9800)
                )
            }
        }
    }
} 