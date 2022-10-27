package com.example.android.january2022.rework

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.example.android.january2022.utils.Routes
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavHost(
  navController: NavHostController
) {
  AnimatedNavHost(
    navController = navController,
    startDestination = Routes.HOME_GRAPH
  ) {
    homeGraph(navController)
  }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.homeGraph(
  navController: NavHostController
) {
  navigation(
    startDestination = Routes.HOME_SCREEN,
    route = Routes.HOME_GRAPH
  ) {
    composable(
      route = Routes.HOME_SCREEN
    ) {
      HomeScreen()
    }
  }
}