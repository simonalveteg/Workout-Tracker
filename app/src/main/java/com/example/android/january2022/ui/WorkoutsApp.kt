package com.example.android.january2022.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cloud.pablos.overload.ui.navigation.WorkoutsBottomNavigationBar
import cloud.pablos.overload.ui.navigation.WorkoutsNavigationActions
import cloud.pablos.overload.ui.navigation.WorkoutsRoute
import cloud.pablos.overload.ui.navigation.WorkoutsTopLevelDestination
import com.example.android.january2022.ui.exercisepicker.ExercisePickerScreen
import com.example.android.january2022.ui.home.HomeScreen
import com.example.android.january2022.ui.session.SessionScreen
import com.example.android.january2022.ui.settings.SettingsScreen
import com.example.android.january2022.utils.UiEvent

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun WorkoutsApp() {
    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        WorkoutsNavigationActions(navController)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination =
        navBackStackEntry?.destination?.route ?: WorkoutsRoute.HOME

    WorkoutsAppContent(
        navController = navController,
        selectedDestination = selectedDestination,
        navigateToTopLevelDestination = navigationActions::navigateTo,
    )
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun WorkoutsAppContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    selectedDestination: String,
    navigateToTopLevelDestination: (WorkoutsTopLevelDestination) -> Unit,
) {
    Row(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
        ) {
            val navHostModifier = if (selectedDestination in WorkoutsRoute.bottomBarDestinations()) {
                Modifier
                    .weight(1f)
                    .then(
                        Modifier.consumeWindowInsets(
                            WindowInsets.systemBars.only(
                                WindowInsetsSides.Bottom,
                            ),
                        ),
                    )
            } else {
                Modifier.weight(1f)
            }

            WorkoutsNavHost(
                navController = navController,
                modifier = navHostModifier,
            )

            AnimatedVisibility(selectedDestination in WorkoutsRoute.bottomBarDestinations()) {
                WorkoutsBottomNavigationBar(
                    selectedDestination = selectedDestination,
                    navigateToTopLevelDestination = navigateToTopLevelDestination,
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
private fun WorkoutsNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = WorkoutsRoute.HOME,
    ) {
        composable(WorkoutsRoute.HOME) {
            HomeScreen(
                onNavigate = { navController.navigationEvent(event = it) },
            )
        }
        composable(WorkoutsRoute.SETTINGS) {
            SettingsScreen()
        }
        composable(
            route = "${WorkoutsRoute.SESSION}/{session_id}",
            arguments = listOf(
                navArgument("session_id") {
                    type = NavType.LongType
                },
            ),
        ) {
            SessionScreen(
                onNavigate = { navController.navigationEvent(event = it) },
            )
        }
        composable(
            route = "${WorkoutsRoute.EXERCISE_PICKER}/{session_id}",
            arguments = listOf(
                navArgument("session_id") {
                    type = NavType.LongType
                },
            ),
        ) {
            // Example of passing the onNavigate callback to ExercisePickerScreen
            ExercisePickerScreen(
                navController = navController,
            )
        }
    }
}

fun NavController.navigationEvent(event: UiEvent.Navigate) {
    navigate(event.route) {
        if (event.popBackStack) currentDestination?.route?.let { popUpTo(it) { inclusive = true } }
        launchSingleTop = true
    }
}
