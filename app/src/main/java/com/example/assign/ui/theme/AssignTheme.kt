
package com.example.assign.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = androidx.compose.ui.graphics.Color.Black,
    primaryVariant = androidx.compose.ui.graphics.Color.DarkGray,
    onPrimary = androidx.compose.ui.graphics.Color.White
)

@Composable
fun AssignTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = DarkColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
