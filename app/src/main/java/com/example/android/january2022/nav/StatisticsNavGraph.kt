package com.example.android.january2022.nav

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.*
import com.example.android.january2022.ui.statistics.ExerciseStatisticsDetailScreen
import com.example.android.january2022.ui.statistics.StatisticsScreen
import com.example.android.january2022.utils.Routes
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.statisticsNavGraph(
    navController: NavController
) {

    navigation(
        startDestination = Routes.STATISTICS_SCREEN,
        route = Routes.STATISTICS_GRAPH,
        enterTransition = {
            when (initialState.destination.route) {
                Routes.PROFILE_SCREEN, Routes.HOME_SCREEN ->
                    slideIntoContainer(AnimatedContentScope.SlideDirection.Right) + fadeIn()
                else -> fadeIn()
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                Routes.PROFILE_SCREEN, Routes.HOME_SCREEN ->
                    slideOutOfContainer(AnimatedContentScope.SlideDirection.Left) + fadeOut()
                else -> fadeOut()
            }
        }
    ) {
        composable(
            route = Routes.STATISTICS_SCREEN
        ) {
            StatisticsScreen(
                onNavigate = {
                    navController.navigate(it.route)
                }
            )
        }
        composable(
            route = Routes.EXERCISE_STATS_DETAIL + "?exerciseId={exerciseId}",
            arguments = listOf(
                navArgument(name = "exerciseId") {
                    type = NavType.LongType
                    defaultValue = -1
                }
            )
        ) {
            ExerciseStatisticsDetailScreen()
        }
    }
}