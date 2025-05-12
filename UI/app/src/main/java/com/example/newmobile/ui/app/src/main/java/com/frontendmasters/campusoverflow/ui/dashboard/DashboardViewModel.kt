package com.example.assign.ui.dashboard


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.frontendmasters.campusoverflow.model.DashboardStats

class DashboardViewModel : ViewModel() {

    // Backing property for the dashboard state
    private val _dashboardStats = MutableStateFlow(
        DashboardStats(
            questions = 244,
            solutions = 844,
            users = 644
        )
    )

    // Public immutable state
    val dashboardStats: StateFlow<DashboardStats> = _dashboardStats
}
