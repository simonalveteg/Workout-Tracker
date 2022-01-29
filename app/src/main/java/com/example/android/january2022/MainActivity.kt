package com.example.android.january2022

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme.colorScheme as colors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.navArgument
import com.example.android.january2022.ui.exercises.detail.ExerciseDetailScreen
import com.example.android.january2022.ui.exercises.ExercisesScreen
import com.example.android.january2022.ui.exercises.picker.ExercisePickerScreen
import com.example.android.january2022.ui.home.HomeScreen
import com.example.android.january2022.ui.session.SessionScreen
import com.example.android.january2022.ui.theme.January2022Theme
import com.example.android.january2022.utils.BottomBarScreen
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
                    WindowCompat.setDecorFitsSystemWindows(window, false)

                    Scaffold(
                        bottomBar = {
                            BottomBar()
                        }
                    ) {
                        GymNavHost(navController)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun GymNavHost(navController: NavHostController) {
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

@Composable
fun BottomBar() {
    var selectedItem by remember { mutableStateOf<BottomBarScreen>(BottomBarScreen.Home) }
    NavigationBar(
        containerColor = colors.surface,
        modifier = Modifier.height(60.dp)
    ) {
        listOf(
            BottomBarScreen.Statistics,
            BottomBarScreen.Home,
            BottomBarScreen.Profile
        ).forEach { screen ->
            NavigationItem(
                screen = screen,
                selectedScreen = selectedItem,
                onScreenClick = { selectedItem = it }
            )
        }
    }
}

@Composable
fun RowScope.NavigationItem(
    screen: BottomBarScreen,
    selectedScreen: BottomBarScreen,
    onScreenClick: (BottomBarScreen) -> Unit
) {
    val isSelected = selectedScreen == screen
    NavigationBarItem(
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = null,
                tint = animateColorAsState(
                    targetValue = if (isSelected) colors.onPrimary else colors.onSurface
                ).value
            )
        },
        selected = isSelected,
        onClick = { onScreenClick(screen) },
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = colors.primary
        )
    )
}


@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    January2022Theme {
        //HomeScreen()
    }
}