package com.example.android.january2022.ui.rework

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.android.january2022.ui.home.HomeScreen
import com.example.android.january2022.ui.session.SessionScreen
import com.example.android.january2022.utils.Routes

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
        onNavigate = {
          navController.navigate(it.route)
        },
        viewModel = viewModel
      )
    }
    composable(Routes.SESSION) {
      SessionScreen(
        onNavigate = {
          navController.navigate(it.route)
        },
        viewModel = viewModel
      )
    }
  }
}
