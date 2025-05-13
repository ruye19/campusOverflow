import androidx.compose.runtime.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotline.ui.viewmodels.QuestionViewModel
import com.example.kotline.ui.viewmodels.QuestionState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.filled.Delete
import com.example.kotline.AuthManager
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.runtime.rememberCoroutineScope
import com.example.kotline.ui.api.ApiClient
import com.example.kotline.ui.api.QuestionApi
import retrofit2.HttpException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    questionId: String,
    onBack: () -> Unit,
    questionViewModel: QuestionViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val questionState by questionViewModel.singleQuestionState.collectAsState()
    val isAdmin = AuthManager.roleId == 1
    println("DEBUG: AuthManager.roleId = ${AuthManager.roleId}")
    var isDeleting by remember { mutableStateOf(false) }

    LaunchedEffect(questionId) {
        questionViewModel.fetchSingleQuestion(questionId)
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
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Text(
                text = "Question",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            if (isAdmin) {
                IconButton(
                    onClick = {
                        isDeleting = true
                        coroutineScope.launch {
                            try {
                                val api = ApiClient.create(QuestionApi::class.java)
                                val response = api.deleteQuestion(questionId)
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Question deleted", Toast.LENGTH_SHORT).show()
                                    onBack()
                                } else {
                                    Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            } finally {
                                isDeleting = false
                            }
                        }
                    },
                    enabled = !isDeleting
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Question",
                        tint = Color(0xFFFF8800)
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }
        }

        when (val state = questionState) {
            is QuestionState.Loading -> {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFFFF8800))
                }
            }
            is QuestionState.Success -> {
                val question = state.questions.firstOrNull()
                if (question != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1E1E1E)
                        ),
                        border = BorderStroke(1.dp, Color(0xFFFF8800))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = question.title,
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = question.description,
                                color = Color.White,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Asked by ${question.username}",
                                    color = Color(0xFFFF8800),
                                    fontSize = 14.sp
                                )
                                if (question.tag != null) {
                                    Text(
                                        text = "#${question.tag}",
                                        color = Color(0xFFFF8800),
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
            is QuestionState.Error -> {
                Text(
                    text = state.message,
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp)
                )
            }
            else -> {}
        }
    }
} 