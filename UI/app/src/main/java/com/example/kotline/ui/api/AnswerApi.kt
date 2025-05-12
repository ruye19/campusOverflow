package com.example.kotline.ui.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import com.example.kotline.ui.viewmodels.QuestionViewModel
import com.example.kotline.ui.viewmodels.QuestionState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.BorderStroke
import com.example.kotline.AuthManager
import androidx.compose.ui.graphics.Color

// Data class for an answer
// Matches backend: answerid, userid, questionid, answer, username

data class Answer(
    val answerid: String?,
    val userid: String?,
    val questionid: String?,
    val answer: String,
    val username: String?
)

data class AnswersResponse(
    val message: String,
    val answers: List<Answer>
)

data class PostAnswerRequest(
    val questionid: String,
    val answer: String,
    val userid: String
)

data class PostAnswerResponse(
    val message: String
)

interface AnswerApi {
    @GET("api/answers/{questionid}")
    suspend fun getAnswers(@Path("questionid") questionid: String): Response<AnswersResponse>

    @POST("api/answers")
    suspend fun postAnswer(@Body request: PostAnswerRequest): Response<PostAnswerResponse>

    @GET("api/question/{question_id}")
    suspend fun getSingleQuestion(@Path("question_id") questionId: String): Response<SingleQuestionResponse>
} 