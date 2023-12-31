package cloud.pablos.overload.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

object WorkoutsRoute {
    const val HOME = "Home"
    const val SETTINGS = "Settings"
    const val SESSION = "Session"
    const val EXERCISE_PICKER = "ExercisePicker"

    fun bottomBarDestinations(): List<String> {
        return listOf(HOME, SETTINGS)
    }
}

data class WorkoutsTopLevelDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconText: String,
)

class WorkoutsNavigationActions(private val navController: NavHostController) {

    fun navigateTo(destination: WorkoutsTopLevelDestination) {
        navController.navigate(destination.route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }
}

val TOP_LEVEL_DESTINATIONS = listOf(
    WorkoutsTopLevelDestination(
        route = WorkoutsRoute.HOME,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        iconText = "Home",
    ),
    WorkoutsTopLevelDestination(
        route = WorkoutsRoute.SETTINGS,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        iconText = "Settings",
    ),
)
