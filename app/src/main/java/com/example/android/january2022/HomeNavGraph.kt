package com.example.android.january2022

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.android.january2022.ui.exercises.ExercisesScreen
import com.example.android.january2022.ui.exercises.detail.ExerciseDetailScreen
import com.example.android.january2022.ui.home.HomeScreen
import com.example.android.january2022.ui.session.SessionScreen
import com.example.android.january2022.utils.Routes
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.homeNavGraph(
    navController: NavController
) {
    navigation(
        startDestination = Routes.HOME_SCREEN,
        route = Routes.HOME_GRAPH,
        enterTransition = {
            when (initialState.destination.route) {
                Routes.STATISTICS_SCREEN ->
                    slideIntoContainer(AnimatedContentScope.SlideDirection.Left) + fadeIn()
                Routes.PROFILE_SCREEN ->
                    slideIntoContainer(AnimatedContentScope.SlideDirection.Right) + fadeIn()
                else -> fadeIn()
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                Routes.PROFILE_SCREEN ->
                    slideOutOfContainer(AnimatedContentScope.SlideDirection.Left) + fadeOut()
                Routes.STATISTICS_SCREEN ->
                    slideOutOfContainer(AnimatedContentScope.SlideDirection.Right) + fadeOut()
                else -> fadeOut()
            }
        }
    ) {
        composable(route = Routes.HOME_SCREEN) {
            HomeScreen(
                onNavigate = {
                    navController.navigate(it.route)
                }
            )
        }
        composable(
            route = Routes.SESSION_SCREEN + "?sessionId={sessionId}",
            arguments = listOf(
                navArgument(name = "sessionId") {
                    type = NavType.LongType
                    defaultValue = -1
                }
            )
        ) {
            SessionScreen(onNavigate = {
                navController.navigate(it.route)
            })
        }
        composable(Routes.EXERCISE_SCREEN) {
            ExercisesScreen()
        }
        composable(
            route = Routes.EXERCISE_DETAIL_SCREEN + "?exerciseId={exerciseId}",
            arguments = listOf(
                navArgument(name = "exerciseId") {
                    type = NavType.LongType
                    defaultValue = -1
                }
            )
        ) {
            ExerciseDetailScreen()
        }

    }
}