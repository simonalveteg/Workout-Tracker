package com.example.android.january2022.ui.rework

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.android.january2022.utils.Routes

@Composable
fun NavHost(
  navController: NavHostController
) {
  NavHost(
    navController = navController,
    startDestination = Routes.SESSION
  ) {

    composable(Routes.SESSION) {
      SessionScreen(
        onNavigate = {
          navController.navigate(it.route)
        }
      )
    }
  }
}
