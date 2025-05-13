package com.example.kotline.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotline.ui.api.Question
import com.example.kotline.ui.api.QuestionApi
import com.example.kotline.ui.api.QuestionsResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

sealed class QuestionsState {
    object Idle : QuestionsState()
    object Loading : QuestionsState()
    data class Success(val questions: List<Question>) : QuestionsState()
    data class Error(val message: String) : QuestionsState()
}

class QuestionsViewModel : ViewModel() {
    private val _questionsState = MutableStateFlow<QuestionsState>(QuestionsState.Idle)
    val questionsState: StateFlow<QuestionsState> = _questionsState

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val BASE_URL = "http://192.168.1.7:5500/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val questionApi = retrofit.create(QuestionApi::class.java)

    fun fetchQuestions() {
        viewModelScope.launch {
            _questionsState.value = QuestionsState.Loading
            try {
                val response = questionApi.getAllQuestions()
                if (response.isSuccessful) {
                    val questionsResponse = response.body()
                    if (questionsResponse != null) {
                        _questionsState.value = QuestionsState.Success(questionsResponse.allQuestion)
                    } else {
                        _questionsState.value = QuestionsState.Error("Empty response from server")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _questionsState.value = QuestionsState.Error("Failed to fetch questions: ${errorBody ?: response.message()}")
                }
            } catch (e: Exception) {
                _questionsState.value = QuestionsState.Error("Network error: ${e.message ?: "Unknown error occurred"}")
            }
        }
    }
} 