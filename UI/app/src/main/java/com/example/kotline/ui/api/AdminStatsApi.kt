package com.example.kotline.ui.api

import retrofit2.Response
import retrofit2.http.GET

// Data classes for stats

data class QuestionStatsResponse(val totalQuestions: Int)
data class AnswerStatsResponse(val totalAnswers: Int)
data class UserStatsResponse(val totalUsers: Int)

interface AdminStatsApi {
    @GET("api/question/countQuestions")
    suspend fun getQuestionStats(): Response<QuestionStatsResponse>

    @GET("api/answers/stats")
    suspend fun getAnswerStats(): Response<AnswerStatsResponse>

    @GET("api/users/getUserStats")
    suspend fun getUserStats(): Response<UserStatsResponse>
} 