package com.frontendmasters.campusoverflow

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.frontendmasters.campusoverflow.navigation.NavGraph
import com.frontendmasters.campusoverflow.ui.theme.CampusOverflowTheme

@Composable
fun App() {
    val navController = rememberNavController()
    CampusOverflowTheme {
        NavGraph(navController = navController)
    }
}