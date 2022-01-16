package com.example.android.january2022

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.android.january2022.ui.exercises.detail.ExerciseDetailScreen
import com.example.android.january2022.ui.theme.January2022Theme
import com.example.android.january2022.ui.exercises.ExercisesScreen
import com.example.android.january2022.ui.exercises.picker.ExercisePickerScreen
import com.example.android.january2022.ui.home.HomeScreen
import com.example.android.january2022.ui.session.SessionScreen
import com.example.android.january2022.utils.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            January2022Theme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Routes.HOME_SCREEN
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
                        route = Routes.EXERCISE_PICKER_SCREEN + "?sessionId={sessionId}",
                        arguments = listOf(
                            navArgument(name = "sessionId") {
                                type = NavType.LongType
                                defaultValue = -1
                            }
                        )
                    ) {
                        ExercisePickerScreen(
                            onPopBackStack = {
                                navController.popBackStack()
                            },
                            onNavigate = { navController.navigate(it.route) }
                        )
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
        }
    }
}


@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    January2022Theme {
        //HomeScreen()
    }
}