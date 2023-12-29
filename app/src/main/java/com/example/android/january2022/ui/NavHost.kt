package com.example.android.january2022.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.android.january2022.ui.exercisepicker.ExercisePickerScreen
import com.example.android.january2022.ui.home.HomeScreen
import com.example.android.january2022.ui.session.SessionScreen
import com.example.android.january2022.ui.settings.SettingsScreen
import com.example.android.january2022.utils.Routes
import com.example.android.january2022.utils.UiEvent

@Composable
fun NavHost(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onNavigate = { navController.navigationEvent(event = it) },
            )
        }
        composable(
            route = "${Routes.SESSION}/{session_id}",
            arguments = listOf(
                navArgument("session_id") {
                    type = NavType.LongType
                },
            ),
        ) {
            SessionScreen(
                onNavigate = { navController.navigationEvent(event = it) },
            )
        }
        composable(
            route = "${Routes.EXERCISE_PICKER}/{session_id}",
            arguments = listOf(
                navArgument("session_id") {
                    type = NavType.LongType
                },
            ),
        ) {
            ExercisePickerScreen(
                navController = navController,
            )
        }
        composable(Routes.SETTINGS) {
            SettingsScreen()
        }
    }
}

fun NavController.navigationEvent(event: UiEvent.Navigate) {
    navigate(event.route) {
        if (event.popBackStack) currentDestination?.route?.let { popUpTo(it) { inclusive = true } }
        launchSingleTop = true
    }
}
