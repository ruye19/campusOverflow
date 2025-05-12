package com.example.kotline.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotline.ui.api.AdminStatsApi
import com.example.kotline.ui.api.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AdminDashboardState {
    object Loading : AdminDashboardState()
    data class Success(val questions: Int, val answers: Int, val users: Int) : AdminDashboardState()
    data class Error(val message: String) : AdminDashboardState()
}

class AdminDashboardViewModel : ViewModel() {
    private val _dashboardState = MutableStateFlow<AdminDashboardState>(AdminDashboardState.Loading)
    val dashboardState: StateFlow<AdminDashboardState> = _dashboardState

    private val api = ApiClient.create(AdminStatsApi::class.java)

    fun fetchStats() {
        viewModelScope.launch {
            _dashboardState.value = AdminDashboardState.Loading
            try {
                val q = api.getQuestionStats()
                val a = api.getAnswerStats()
                val u = api.getUserStats()
                if (q.isSuccessful && a.isSuccessful && u.isSuccessful) {
                    val questions = q.body()?.totalQuestions ?: 0
                    val answers = a.body()?.totalAnswers ?: 0
                    val users = u.body()?.totalUsers ?: 0
                    _dashboardState.value = AdminDashboardState.Success(questions, answers, users)
                } else {
                    _dashboardState.value = AdminDashboardState.Error("Failed to fetch stats")
                }
            } catch (e: Exception) {
                _dashboardState.value = AdminDashboardState.Error("Network error: ${e.message}")
            }
        }
    }
} 