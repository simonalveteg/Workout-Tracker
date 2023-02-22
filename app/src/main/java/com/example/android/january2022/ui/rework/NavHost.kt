package com.example.android.january2022.ui.rework

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.android.january2022.ui.exercisepicker.ExercisePickerScreen
import com.example.android.january2022.ui.home.HomeScreen
import com.example.android.january2022.ui.session.SessionScreen
import com.example.android.january2022.ui.settings.SettingsScreen
import com.example.android.january2022.utils.Routes
import com.example.android.january2022.utils.UiEvent

@Composable
fun NavHost(
  navController: NavHostController
) {
  val viewModel: MainViewModel = hiltViewModel()

  NavHost(
    navController = navController,
    startDestination = Routes.HOME
  ) {

    composable(Routes.HOME) {
      HomeScreen(
        onNavigate = { navController.navigationEvent(event = it) },
        viewModel = viewModel
      )
    }
    composable(Routes.SESSION) {
      SessionScreen(
        onNavigate = { navController.navigationEvent(event = it) },
        viewModel = viewModel
      )
    }
    composable(Routes.EXERCISE_PICKER) {
      ExercisePickerScreen(
        navController = navController,
        viewModel = viewModel
      )
    }
    composable(Routes.SETTINGS) {
      SettingsScreen(viewModel = viewModel)
    }
  }
}

fun NavController.navigationEvent(event: UiEvent.Navigate) {
  navigate(event.route) {
    if (event.popBackStack) currentDestination?.route?.let { popUpTo(it) { inclusive = true } }
    launchSingleTop = true
  }
}
