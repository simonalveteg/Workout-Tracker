package cloud.pablos.overload.ui.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun WorkoutsBottomNavigationBar(
    selectedDestination: String,
    navigateToTopLevelDestination: (WorkoutsTopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        TOP_LEVEL_DESTINATIONS.forEach { workoutsDestination ->
            NavigationBarItem(
                selected = selectedDestination == workoutsDestination.route,
                onClick = { navigateToTopLevelDestination(workoutsDestination) },
                icon = {
                    Icon(
                        imageVector =
                        if (selectedDestination == workoutsDestination.route) {
                            workoutsDestination.selectedIcon
                        } else {
                            workoutsDestination.unselectedIcon
                        },
                        contentDescription = workoutsDestination.iconText,
                    )
                },
                label = {
                    Text(workoutsDestination.iconText)
                },
            )
        }
    }
}
