package com.example.newmobile.ui.theme.dashboard



import androidx.lifecycle.ViewModel
import com.example.newmobile.model.DashboardStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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
