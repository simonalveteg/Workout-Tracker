package com.example.android.january2022

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.navArgument
import com.example.android.january2022.ui.exercises.detail.ExerciseDetailScreen
import com.example.android.january2022.ui.exercises.ExercisesScreen
import com.example.android.january2022.ui.exercises.picker.ExercisePickerScreen
import com.example.android.january2022.ui.home.HomeScreen
import com.example.android.january2022.ui.session.SessionScreen
import com.example.android.january2022.ui.theme.January2022Theme
import com.example.android.january2022.utils.Routes
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            January2022Theme {
                ProvideWindowInsets {
                    val navController = rememberAnimatedNavController()
                    val colors = MaterialTheme.colorScheme
                    WindowCompat.setDecorFitsSystemWindows(window, false)

                    Scaffold(
                        bottomBar = {
                            var selectedItem by remember { mutableStateOf(1) }
                            NavigationBar(
                                containerColor = colors.surface,
                                modifier = Modifier.height(60.dp)
                            ) {

                                NavigationBarItem(
                                    icon = {
                                        Icon(
                                            Icons.Filled.BarChart, null,
                                            tint = animateColorAsState(
                                                targetValue = if (selectedItem == 2) colors.onPrimary else colors.onSurface
                                            ).value
                                        )
                                    },
                                    selected = selectedItem == 2,
                                    onClick = {
                                        selectedItem = 2
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        indicatorColor = colors.primary
                                    )
                                )
                                NavigationBarItem(
                                    icon = {
                                        Icon(
                                            Icons.Filled.Home, null,
                                            tint = animateColorAsState(
                                                targetValue = if (selectedItem == 1) colors.onPrimary else colors.onSurface
                                            ).value
                                        )
                                    },
                                    selected = selectedItem == 1,
                                    onClick = {
                                        selectedItem = 1
                                        navController.navigate(Routes.HOME_SCREEN) {
                                            popUpTo(Routes.HOME_SCREEN)
                                        }
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        indicatorColor = colors.primary
                                    )
                                )
                                NavigationBarItem(
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Filled.Person,
                                            contentDescription = null,
                                            tint = animateColorAsState(
                                                targetValue = if (selectedItem == 3) colors.onPrimary else colors.onSurface
                                            ).value
                                        )
                                    },
                                    selected = selectedItem == 3,
                                    onClick = { selectedItem = 3 },
                                    colors = NavigationBarItemDefaults.colors(
                                        indicatorColor = colors.primary
                                    )
                                )
                            }
                        }
                    ) {
                        AnimatedNavHost(
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
    }
}


@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    January2022Theme {
        //HomeScreen()
    }
}