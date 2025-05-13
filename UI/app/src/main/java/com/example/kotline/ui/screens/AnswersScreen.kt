package com.example.kotline.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotline.ui.api.Answer
import com.example.kotline.ui.viewmodels.AnswerState
import com.example.kotline.ui.viewmodels.AnswerViewModel
import com.example.kotline.AuthManager
import androidx.compose.foundation.border

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswersScreen(
    questionId: String,
    onBack: () -> Unit,
    username: String = AuthManager.firstName ?: "User",
    answerViewModel: AnswerViewModel = viewModel()
) {
    var answerText by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf<String?>(null) }
    val answerState by answerViewModel.answerState.collectAsState()

    // Fetch answers when screen loads
    LaunchedEffect(questionId) {
        answerViewModel.fetchAnswers(questionId)
    }

    // Handle post success/error states
    LaunchedEffect(answerState) {
        when (answerState) {
            is AnswerState.PostSuccess -> {
                answerText = "" // Clear the text field
                showError = null
            }
            is AnswerState.PostError -> {
                showError = (answerState as AnswerState.PostError).message
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }
        // Tips section
        Text(
            text = "Tips",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Column(modifier = Modifier.padding(bottom = 12.dp)) {
            Text(
                text = "Read the question carefully before answering.",
                color = Color.White,
                fontSize = 14.sp
            )
            Text(
                text = "Search online. ",
                color = Color(0xFF2196F3),
                fontSize = 14.sp
            )
            Text(
                text = "Share links to useful resources.",
                color = Color(0xFF2196F3),
                fontSize = 14.sp
            )
            Text(
                text = "Be respectful, concise, and helpful in your response.",
                color = Color.White,
                fontSize = 14.sp
            )
        }
        // View Answers header
        Text(
            text = "View Answers",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        // Answers list
        when (val state = answerState) {
            is AnswerState.Loading -> {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFFFF8800))
                }
            }
            is AnswerState.Success -> {
                if (state.answers.isEmpty()) {
                    Text("No answers yet.", color = Color.Gray, modifier = Modifier.padding(8.dp))
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        state.answers.forEach { answer ->
                            AnswerCardStyled(answer)
                        }
                    }
                }
            }
            is AnswerState.Error -> {
                Text(
                    text = state.message,
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp)
                )
            }
            is AnswerState.Posting -> {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFFFF8800))
                }
            }
            else -> {}
        }
        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = Color.Gray, thickness = 1.dp)
        // Submit an answer section
        Text(
            text = "Submit an answer",
            color = Color.White,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(12.dp))
        ) {
            OutlinedTextField(
                value = answerText,
                onValueChange = { answerText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.White, shape = RoundedCornerShape(12.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF8800),
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color(0xFFFF8800),
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                placeholder = { Text("Type your answer here", color = Color.Gray) },
                maxLines = 5
            )
        }
        if (showError != null) {
            Text(
                text = showError!!,
                color = Color.Red,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    if (answerText.isBlank()) {
                        showError = "Answer cannot be empty."
                    } else {
                        showError = null
                        answerViewModel.postAnswer(questionId, answerText, AuthManager.userId ?: "")
                    }
                },
                enabled = answerState !is AnswerState.Posting,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8800)),
                modifier = Modifier
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = if (answerState is AnswerState.Posting) "Posting..." else "Post",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun AnswerCardStyled(answer: Answer) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEEEEEE)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar",
                    tint = Color.Gray,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = answer.username ?: "User",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = answer.answerid ?: "date",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = answer.answer,
                    color = Color.Black,
                    fontSize = 14.sp
                )
            }
        }
    }
} 