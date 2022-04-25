package com.example.android.january2022

import android.util.Log
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.android.january2022.ui.exercises.ExerciseViewModel
import com.example.android.january2022.ui.exercises.picker.ExercisePickerScreen
import com.example.android.january2022.ui.exercises.picker.MusclePickerScreen
import com.example.android.january2022.utils.Routes
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.exercisePickerGraph(
    navController: NavController
) {
    navigation(
        startDestination = Routes.MUSCLE_PICKER_SCREEN + "?sessionId={sessionId}",
        route = Routes.EXERCISE_PICKER_GRAPH,
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
        composable(
            route = Routes.MUSCLE_PICKER_SCREEN + "?sessionId={sessionId}",
            arguments = listOf(
                navArgument(name = "sessionId") {
                    type = NavType.LongType
                    defaultValue = -1
                }
            )
        ) {
            Log.d("EPG","$route")
            val id = it.arguments?.getLong("sessionId")
            Log.d("EPG","$id")
            val parentEntry = remember {
                navController.getBackStackEntry(Routes.EXERCISE_PICKER_GRAPH)
            }
            MusclePickerScreen(
                viewModel = hiltViewModel(parentEntry),
                onPopBackStack = { navController.popBackStack(Routes.EXERCISE_PICKER_GRAPH,true) },
                onNavigate = { navController.navigate(it.route) }
            )
        }
        composable(
            route = Routes.EXERCISE_PICKER_SCREEN + "?sessionId={sessionId}",
            arguments = listOf(
                navArgument(name = "sessionId") {
                    type = NavType.LongType
                    defaultValue = -1
                }
            )
        ) {
            Log.d("EPG","$route")
            // get parent entry to make view model scoped to nav graph
            val parentEntry = remember {
                navController.getBackStackEntry(Routes.EXERCISE_PICKER_GRAPH)
            }
            ExercisePickerScreen(
                viewModel = hiltViewModel(parentEntry),
                onPopBackStack = {
                    navController.popBackStack(Routes.EXERCISE_PICKER_GRAPH,true)
                },
                onNavigate = { navController.navigate(it.route) }
            )
        }
    }
}