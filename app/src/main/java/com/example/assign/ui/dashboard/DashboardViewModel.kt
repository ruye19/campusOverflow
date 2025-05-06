
package com.example.assign.ui.dashboard

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.assign.model.DashboardStats

class DashboardViewModel : ViewModel() {
    private val _dashboardStats = MutableStateFlow(
        DashboardStats(
            questions = 244,
            solutions = 844,
            users = 644
        )
    )
    val dashboardStats: StateFlow<DashboardStats> = _dashboardStats
}
