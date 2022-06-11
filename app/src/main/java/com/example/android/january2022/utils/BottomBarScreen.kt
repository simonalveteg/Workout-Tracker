package com.example.android.january2022.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.StackedBarChart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomBarScreen(
        route = Routes.HOME_GRAPH,
        title = "Home",
        icon = Icons.Filled.Home,
    )
    object Statistics : BottomBarScreen(
        route = Routes.STATISTICS_GRAPH,
        title = "Statistics",
        icon = Icons.Filled.BarChart,
    )
    object Profile : BottomBarScreen(
        route = Routes.PROFILE_SCREEN,
        title = "Profile",
        icon = Icons.Filled.Person,
    )
}
