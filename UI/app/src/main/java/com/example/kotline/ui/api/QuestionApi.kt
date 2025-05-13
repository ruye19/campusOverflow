package com.example.kotline.ui.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.DELETE

// Data class for a question
// Matches backend: id, questionid, userid, title, description, tag, username, profession

data class Question(
    val id: Int,
    val questionid: String,
    val userid: String,
    val title: String,
    val description: String,
    val tag: String?,
    val username: String,
    val profession: String
)

data class QuestionsResponse(
    val msg: String,
    val allQuestion: List<Question>
)

data class AskQuestionRequest(
    val title: String,
    val description: String,
    val tag: String? = null
)

data class AskQuestionResponse(
    val message: String,
    val questionid: String?
)

data class SingleQuestionResponse(
    val msg: String,
    val singleQuestion: List<Question>
)

interface QuestionApi {
    @GET("api/question/")
    suspend fun getAllQuestions(): Response<QuestionsResponse>

    @POST("api/question/")
    suspend fun postQuestion(@Body request: AskQuestionRequest): Response<AskQuestionResponse>

    @GET("api/question/{question_id}")
    suspend fun getSingleQuestion(@Path("question_id") questionId: String): Response<SingleQuestionResponse>

    @DELETE("api/question/{question_id}")
    suspend fun deleteQuestion(@Path("question_id") questionId: String): Response<Unit>
} 