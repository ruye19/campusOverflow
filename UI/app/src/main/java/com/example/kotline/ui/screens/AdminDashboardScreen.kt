package com.example.kotline.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotline.ui.viewmodels.AdminDashboardState
import com.example.kotline.ui.viewmodels.AdminDashboardViewModel
import androidx.compose.foundation.border

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onBack: () -> Unit,
    onViewQuestions: () -> Unit,
    onViewUsers: () -> Unit,
    adminName: String = "Abebe Mola",
    adminRole: String = "Admin",
    viewModel: AdminDashboardViewModel = viewModel()
) {
    val dashboardState by viewModel.dashboardState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchStats()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(adminName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(adminRole, color = Color.Gray, fontSize = 12.sp)
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF222222)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Dashboard",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Welcome to your Dashboard\nHere, you can view and manage all users, browse questions and answers, and easily monitor activity across the platform",
            color = Color.White,
            fontSize = 13.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        when (val state = dashboardState) {
            is AdminDashboardState.Loading -> {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFFFF8800))
                }
            }
            is AdminDashboardState.Success -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatCircle(label = "QUESTIONS", value = state.questions)
                    StatCircle(label = "SOLUTIONS", value = state.answers)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    StatCircle(label = "USERS", value = state.users)
                }
            }
            is AdminDashboardState.Error -> {
                Text(
                    text = state.message,
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onViewQuestions,
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8800)),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(vertical = 8.dp)
        ) {
            Text("View Questions", color = Color.White, fontWeight = FontWeight.Bold)
        }
        Button(
            onClick = onViewUsers,
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8800)),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(vertical = 8.dp)
        ) {
            Text("View Users", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun StatCircle(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            // Outer circle
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .border(6.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // Orange arc (simulate progress)
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clip(CircleShape)
                        .background(Color.Transparent)
                )
                Text(
                    text = value.toString(),
                    color = Color(0xFFFF8800),
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
            }
        }
        Text(
            text = label,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
} 