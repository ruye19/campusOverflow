package com.example.jetpackcomposeauthui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcomposeauthui.components.CButton
import com.example.jetpackcomposeauthui.components.CTextField

@Composable
fun SignupScreen(
    navController: NavHostController
) {
    Surface(
        color = Color(0xFF253334), // Dark background
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.bg1),
                contentDescription = "Campus Overflow background",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .align(Alignment.BottomCenter)
            )

            // Content Column
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                // Logo
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Campus Overflow Logo",
                    modifier = Modifier
                        .padding(top = 54.dp)
                        .height(80.dp)
                        .align(Alignment.Start)
                )

                // Title
                Text(
                    text = "Create Account",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.Start)
                )

                // Subtitle
                Text(
                    text = "Join our community to ask questions and share knowledge",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 32.dp)
                        .align(Alignment.Start)
                )

                // Form Fields
                CTextField(
                    hint = "Full Name",
                    value = "",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                CTextField(
                    hint = "Email Address",
                    value = "",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                CTextField(
                    hint = "Password",
                    value = "",
                    isPassword = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Sign Up Button
                CButton(
                    text = "Sign Up",
                    onClick = { /* Handle sign up */ },
                    modifier = Modifier.fillMaxWidth()
                )

                // Sign In Prompt
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp, bottom = 52.dp)
                        .align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Already have an account? ",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Sign In",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            navController.navigate("login")
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun SignupScreenPreview() {
    SignupScreen(rememberNavController())
}