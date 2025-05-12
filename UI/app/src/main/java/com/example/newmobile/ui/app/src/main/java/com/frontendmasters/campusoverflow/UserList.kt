package com.frontendmasters.campusoverflow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.frontendmasters.campusoverflow.ui.theme.CampusOverflowTheme
import androidx.navigation.NavController

data class User(
    val id: Int,
    val name: String,
    val job: String
)

//@Preview
//@Composable
//private fun User_List_Preview() {
//    CampusOverflowTheme {
//        User_List()
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun User_List(navController: NavController) {
    val searchQuery = remember { mutableStateOf("") }
    val users = remember {
        mutableStateListOf(
            User(1, "John Doe", "Software Engineer"),
            User(2, "Jane Smith", "Product Manager"),
            User(3, "Robert Johnson", "UX Designer"),
            User(4, "Emily Davis", "Data Scientist"),
            User(5, "Susana Kevin", "Cloud Engineer") // ✅ Unique ID
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // Search Bar and AppBar
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            title = {
                OutlinedTextField(
                    value = searchQuery.value,
                    onValueChange = { searchQuery.value = it },
                    label = { Text("Search") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.Gray
                        )
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().padding(end = 10.dp)
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color.Black,
                navigationIconContentColor = Color.Black
            )
        )

        // User List
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Users",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            val filteredUsers = users.filter {
                it.name.contains(searchQuery.value, ignoreCase = true) ||
                        it.job.contains(searchQuery.value, ignoreCase = true)
            }

            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(filteredUsers, key = { it.id }) { user ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .padding(10.dp)
                    ) {
                        UserListItem(
                            user = user,
                            onDelete = {
                                users.removeAll { it.id == user.id }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserListItem(
    user: User,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // User Icon
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "User",
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // User Info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.Black
            )
            Text(
                text = user.job,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }

        // Delete Button
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete user",
                tint = Color.Black
            )
        }
    }
}
