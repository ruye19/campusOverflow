package com.frontendmasters.campusoverflow

import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.tooling.preview.Preview
import com.frontendmasters.campusoverflow.ui.theme.CampusOverflowTheme
import org.w3c.dom.Text
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import com.frontendmasters.campusoverflow.model.UserRole

//@Preview(showBackground = true)
//@Composable
//private fun QuestionsPage_Preview() {
//    CampusOverflowTheme {
//        QuestionsPage(onBackClick = {})
//    }
//}

data class Question(
    val id: String,
    val userImage: String? = null,
    val userName: String,
    val userRole: String,
    val questionText: String,
    val hasAnswer: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionsPage(
    navController: NavController,
    onBackClick: () -> Unit,
    userRole: UserRole = UserRole.USER // Default to USER if not specified
) {
    val sampleQuestions = remember {
        listOf(
            Question(
                id = "1",
                userName = "Andile Molo",
                userRole = "Developer",
                questionText = "Lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled to make a type specimen book.",
                hasAnswer = true
            ),
            Question(
                id = "2",
                userName = "Andile Molo",
                userRole = "Developer",
                questionText = "Lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled to make a type specimen book.",
                hasAnswer = false
            ),
            Question(
                id = "3",
                userName = "Andile Molo",
                userRole = "Developer",
                questionText = "Lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled to make a type specimen book.",
                hasAnswer = true
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Questions", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                )
            )
        },
        containerColor = Color.Black
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }
            
            items(sampleQuestions) { question ->
                QuestionCard(
                    question = question,
                    onAnswerClick = { 
                        // Navigate to answer page
                        navController.navigate("answers/${question.id}")
                    },
                    isAdmin = userRole == UserRole.ADMIN
                )
            }
            
            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}

@Composable
fun QuestionCard(
    question: Question,
    onAnswerClick: () -> Unit,
    isAdmin: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onAnswerClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // User info row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // User avatar
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    
                    // User name and role
                    Column {
                        Text(
                            text = question.userName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = question.userRole,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                // More options icon (visible only for admin)
                if (isAdmin) {
                    IconButton(
                        onClick = { /* Handle more options */ }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options",
                            tint = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Question text
            Text(
                text = question.questionText,
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // See answer link
            if (question.hasAnswer) {
                Text(
                    text = "See Answer",
                    color = Color(0xFFFF6D00),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable(onClick = onAnswerClick)
                )
            }
        }
    }
}