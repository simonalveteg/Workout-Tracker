package com.alveteg.simon.workouts.ui.home.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.alveteg.simon.workouts.ui.home.HomeEvent
import com.alveteg.simon.workouts.ui.session.actions.ActionSpacer
import com.alveteg.simon.workouts.ui.session.actions.ActionSpacerStart
import com.alveteg.simon.workouts.utils.Event

@Composable
fun HomeAppBar(
  onEvent: (Event) -> Unit
) {
  BottomAppBar(
    actions = {
      ActionSpacerStart()
      IconButton(onClick = { /*TODO*/ }) {
        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Options")
      }
      ActionSpacer()
      IconButton(onClick = { onEvent(HomeEvent.OpenSettings) }) {
        Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
      }
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = { onEvent(HomeEvent.NewSession) },
        containerColor = MaterialTheme.colorScheme.primary
      ) {
        Icon(Icons.Default.Add, "Add Session")
      }
    }
  )
}