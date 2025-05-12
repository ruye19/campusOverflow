package com.example.newmobile.ui.theme.theme



import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.darkColorScheme

private val DarkColorScheme = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color.Black,
    onPrimary = androidx.compose.ui.graphics.Color.White
)

@Composable
fun AssignTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme, // Change this from `colors`
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
