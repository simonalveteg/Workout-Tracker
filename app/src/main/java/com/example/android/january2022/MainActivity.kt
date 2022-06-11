package com.example.android.january2022

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme.colorScheme as colors
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.*
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.android.january2022.nav.homeNavGraph
import com.example.android.january2022.nav.statisticsNavGraph
import com.google.accompanist.navigation.animation.composable
import com.example.android.january2022.ui.profile.ProfileScreen
import com.example.android.january2022.ui.statistics.StatisticsScreen
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
                            BottomBar(navController)
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
        startDestination = Routes.HOME_GRAPH,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() },
        popExitTransition = { fadeOut() }
    ) {
        statisticsNavGraph(navController)
        homeNavGraph(navController)
        composable(
            route = Routes.PROFILE_SCREEN,
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Left) + fadeIn()
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Right) + fadeOut()
            }
        ) {
            ProfileScreen(onNavigate = { navController.navigate(it.route) })
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

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
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.NavigationItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val isSelected = currentDestination?.hierarchy?.any {
        it.route == screen.route
    } == true
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
        onClick = {
            navController.navigate(screen.route) {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                // if the current tab isn't already selected, launchSingleTop and restore previous state
                if (!isSelected) {
                    // Avoid multiple copies of the same destination when
                    // re-selecting the same item
                    launchSingleTop = true
                    // Restore state when re-selecting a previously selected item
                    restoreState = true
                } else {
                    popUpTo(screen.route)
                }
            }
        },
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