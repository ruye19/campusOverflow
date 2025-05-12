package com.example.newmobile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AnswersPage(navController: NavController) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    val answers = remember {
        mutableStateListOf(
            Answer("Ababa Mola", "Developer", "This is a detailed answer about Jetpack Compose..."),
            Answer("Abebe Mola", "Developer", "Here's another perspective on the question..."),
            Answer("John Doe", "Senior Developer", "Based on my experience, I would recommend...")
        )
    }

    if (isLandscape) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp)
        ) {
            // Left side: Tips
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Tips",
                    color = Color.White,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                TipsList()
            }

            // Right side: Answers
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = "View Answers",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(answers) { answer ->
                        AnswerCard(answer = answer)
                    }
                }
            }
        }
    } else {
        // Portrait layout
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Text(
                text = "Tips",
                color = Color.White,
                fontSize = 24.sp,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            TipsList()

            Text(
                text = "View Answers",
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(answers) { answer ->
                    AnswerCard(answer = answer)
                }
            }
        }
    }
}

@Composable
private fun TipsList() {
    Column {
        TipItem("Read the question carefully before answering.")
        TipItem("Stay on topic — make sure your answer directly addresses the question.")
        TipItem("Be clear and concrete — explain your solution step-by-step.")
    }
}

@Composable
private fun TipItem(text: String) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 16.sp,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun AnswerCard(answer: Answer) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = Color.Gray,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = answer.name, fontSize = 16.sp, color = Color.Black)
                    Text(text = answer.title, fontSize = 14.sp, color = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = answer.content,
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}

data class Answer(
    val name: String,
    val title: String,
    val content: String
)