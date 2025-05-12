package com.example.assign

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.assign.ui.dashboard.DashboardScreen
import com.example.assign.ui.dashboard.DashboardViewModel
import com.example.assign.ui.theme.AssignTheme // or your app theme name

class MainActivity : ComponentActivity() {

    private val dashboardViewModel: DashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AssignTheme {
                // Collect state from ViewModel
                val stats by dashboardViewModel.dashboardStats.collectAsState()

                DashboardScreen(
                    userName = "Abebe Mola",
                    role = "Admin",
                    stats = stats,
                    onBackClick = { /* Handle back logic here */ },
                    onViewQuestions = { /* Navigate to questions screen */ },
                    onViewUsers = { /* Navigate to users screen */ }
                )
            }
        }
    }
}
