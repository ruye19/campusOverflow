package com.frontendmasters.campusoverflow.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.frontendmasters.campusoverflow.model.DashboardStats
import com.frontendmasters.campusoverflow.model.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    userName: String,
    role: String,
    stats: DashboardStats,
    onBackClick: () -> Unit,
    onViewQuestions: () -> Unit,
    onViewUsers: () -> Unit
) {
    val userRole = UserRole.valueOf(role)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = userName, color = Color.White, fontSize = 14.sp)
                        Text(text = role, color = Color.Gray, fontSize = 12.sp)
                    }
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(start = 8.dp, end = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color.Black
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Dashboard", fontSize = 28.sp, color = Color.White, fontWeight = FontWeight.Bold)
            Text(
                when (userRole) {
                    UserRole.ADMIN -> "Welcome to Admin Dashboard\nManage users, monitor questions, and oversee platform activity"
                    UserRole.USER -> "Welcome to User Dashboard\nBrowse questions, post answers, and engage with the community"
                },
                color = Color.LightGray,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
            )
            
            StatRow(stats = stats)
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = onViewQuestions,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6D00))
            ) {
                Text("View Questions", color = Color.White)
            }
            
            if (userRole == UserRole.ADMIN) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onViewUsers,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6D00))
                ) {
                    Text("Manage Users", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun StatRow(stats: DashboardStats) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatCircle(label = "QUESTIONS", count = stats.questions)
        StatCircle(label = "SOLUTIONS", count = stats.solutions)
        StatCircle(label = "USERS", count = stats.users)
    }
}

@Composable
fun StatCircle(label: String, count: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color(0xFFFF6D00)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = count.toString(),
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            color = Color.White,
            fontSize = 12.sp
        )
    }
}