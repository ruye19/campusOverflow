package com.example.kotline.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotline.ui.api.ApiClient
import com.example.kotline.ui.api.Question
import com.example.kotline.ui.api.QuestionApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class QuestionState {
    object Idle : QuestionState()
    object Loading : QuestionState()
    data class Success(val questions: List<Question>) : QuestionState()
    data class Error(val message: String) : QuestionState()
    object Posting : QuestionState()
    data class PostSuccess(val message: String) : QuestionState()
    data class PostError(val message: String) : QuestionState()
}

class QuestionViewModel : ViewModel() {
    private val _questionState = MutableStateFlow<QuestionState>(QuestionState.Idle)
    val questionState: StateFlow<QuestionState> = _questionState

    private val _singleQuestionState = MutableStateFlow<QuestionState>(QuestionState.Idle)
    val singleQuestionState: StateFlow<QuestionState> = _singleQuestionState

    private val questionApi = ApiClient.create(QuestionApi::class.java)

    fun fetchQuestions() {
        viewModelScope.launch {
            _questionState.value = QuestionState.Loading
            try {
                val response = questionApi.getAllQuestions()
                if (response.isSuccessful) {
                    response.body()?.let {
                        _questionState.value = QuestionState.Success(it.allQuestion)
                    } ?: run {
                        _questionState.value = QuestionState.Error("Empty response from server")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("QuestionViewModel", "Failed to fetch questions: ${errorBody ?: response.message()}")
                    _questionState.value = QuestionState.Error(
                        "Failed to fetch questions: ${errorBody ?: response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("QuestionViewModel", "Network error", e)
                _questionState.value = QuestionState.Error(
                    "Network error: ${e.message ?: "Unknown error occurred"}"
                )
            }
        }
    }

    fun postQuestion(title: String, description: String, tag: String? = null) {
        viewModelScope.launch {
            _questionState.value = QuestionState.Posting
            try {
                val response = questionApi.postQuestion(
                    com.example.kotline.ui.api.AskQuestionRequest(title, description, tag)
                )
                if (response.isSuccessful) {
                    response.body()?.let {
                        _questionState.value = QuestionState.PostSuccess(it.message)
                    } ?: run {
                        _questionState.value = QuestionState.PostError("Empty response from server")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _questionState.value = QuestionState.PostError(
                        "Failed to post question: ${errorBody ?: response.message()}"
                    )
                }
            } catch (e: Exception) {
                _questionState.value = QuestionState.PostError(
                    "Network error: ${e.message ?: "Unknown error occurred"}"
                )
            }
        }
    }

    fun fetchSingleQuestion(questionId: String) {
        viewModelScope.launch {
            _singleQuestionState.value = QuestionState.Loading
            try {
                val response = questionApi.getSingleQuestion(questionId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        val question = it.singleQuestion.firstOrNull()
                        if (question != null) {
                            _singleQuestionState.value = QuestionState.Success(listOf(question))
                        } else {
                            _singleQuestionState.value = QuestionState.Error("Question not found")
                        }
                    } ?: run {
                        _singleQuestionState.value = QuestionState.Error("Empty response from server")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _singleQuestionState.value = QuestionState.Error(
                        "Failed to fetch question: "+ (errorBody ?: response.message())
                    )
                }
            } catch (e: Exception) {
                _singleQuestionState.value = QuestionState.Error(
                    "Network error: "+ (e.message ?: "Unknown error occurred")
                )
            }
        }
    }
} 