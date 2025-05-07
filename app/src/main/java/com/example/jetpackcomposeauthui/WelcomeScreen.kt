package com.example.jetpackcomposeauthui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcomposeauthui.components.CButton
import com.example.jetpackcomposeauthui.components.DontHaveAccountRow

@Composable
fun WelcomeScreen(
    navController: NavHostController,
    onLoginClick: () -> Unit,
    onSignupClick: () -> Unit,
    onGuestClick: () -> Unit
) {
    // Replace direct navController.navigate() calls with:
    // onLoginClick() instead of navController.navigate("login")

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = "Forum background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        // Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // App Name
            Text(
                "Welcome to Campus Overflow",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            // Description Text
            Text(
                "You can be able to launch upcoming families in accordance with " +
                        "the use required by our website for community out-of-home libraries.",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Action Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CButton(
                    text = "Sign In",
                    onClick = { navController.navigate("login") }
                )

                CButton(
                    text = "Sign Up",
                    onClick = { navController.navigate("signup") }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(rememberNavController())
}