package com.example.kotline.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotline.ui.api.Answer
import com.example.kotline.ui.api.AnswerApi
import com.example.kotline.ui.api.AnswersResponse
import com.example.kotline.ui.api.PostAnswerRequest
import com.example.kotline.ui.api.PostAnswerResponse
import com.example.kotline.ui.api.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AnswerState {
    object Idle : AnswerState()
    object Loading : AnswerState()
    data class Success(val answers: List<Answer>) : AnswerState()
    data class Error(val message: String) : AnswerState()
    object Posting : AnswerState()
    data class PostSuccess(val message: String) : AnswerState()
    data class PostError(val message: String) : AnswerState()
}

class AnswerViewModel : ViewModel() {
    private val _answerState = MutableStateFlow<AnswerState>(AnswerState.Idle)
    val answerState: StateFlow<AnswerState> = _answerState

    private val answerApi = ApiClient.create(AnswerApi::class.java)

    fun fetchAnswers(questionid: String) {
        viewModelScope.launch {
            _answerState.value = AnswerState.Loading
            try {
                Log.d("AnswerViewModel", "Fetching answers for question: $questionid")
                val response = answerApi.getAnswers(questionid)
                Log.d("AnswerViewModel", "Response received: ${response.body()}")
                
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("AnswerViewModel", "Answers fetched successfully: ${it.answers.size} answers")
                        _answerState.value = AnswerState.Success(it.answers)
                    } ?: run {
                        Log.e("AnswerViewModel", "Empty response from server")
                        _answerState.value = AnswerState.Error("Empty response from server")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("AnswerViewModel", "Failed to fetch answers. Error: $errorBody")
                    _answerState.value = AnswerState.Error(
                        "Failed to fetch answers: ${errorBody ?: response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("AnswerViewModel", "Network error", e)
                _answerState.value = AnswerState.Error(
                    "Network error: ${e.message ?: "Unknown error occurred"}"
                )
            }
        }
    }

    fun postAnswer(questionid: String, answer: String, userid: String) {
        viewModelScope.launch {
            _answerState.value = AnswerState.Posting
            try {
                Log.d("AnswerViewModel", "Posting answer for question: $questionid")
                val response = answerApi.postAnswer(PostAnswerRequest(questionid, answer, userid))
                
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("AnswerViewModel", "Answer posted successfully: ${it.message}")
                        _answerState.value = AnswerState.PostSuccess(it.message)
                        // Refresh answers after successful post
                        fetchAnswers(questionid)
                    } ?: run {
                        Log.e("AnswerViewModel", "Empty response from server")
                        _answerState.value = AnswerState.PostError("Empty response from server")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("AnswerViewModel", "Failed to post answer. Error: $errorBody")
                    _answerState.value = AnswerState.PostError(
                        "Failed to post answer: ${errorBody ?: response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("AnswerViewModel", "Network error while posting answer", e)
                _answerState.value = AnswerState.PostError(
                    "Network error: ${e.message ?: "Unknown error occurred"}"
                )
            }
        }
    }
} 