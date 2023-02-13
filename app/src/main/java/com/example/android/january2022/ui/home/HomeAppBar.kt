package com.example.android.january2022.ui.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.january2022.ui.session.actions.ActionSpacer
import com.example.android.january2022.ui.session.actions.ActionSpacerStart
import com.example.android.january2022.utils.Event

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