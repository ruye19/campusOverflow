package com.example.newmobile

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.newmobile.navigation.NavGraph
import com.example.newmobile.ui.theme.theme.NewmobileTheme
import com.example.newmobile.ui.theme.theme.NewmobileTheme

@Composable
fun App() {
    val navController = rememberNavController()
    NewmobileTheme {
        NavGraph(navController = navController)
    }
}