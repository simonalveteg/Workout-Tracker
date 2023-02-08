package com.example.android.january2022.ui.session

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.january2022.utils.Event

@Composable
fun SessionAppBar(
  onEvent: (Event) -> Unit,
  timerVisible: Boolean,
  onTimerPress: () -> Unit
) {
  BottomAppBar(
    actions = {
      Spacer(modifier = Modifier.width(4.dp))
      IconButton(onClick = { /*TODO*/ }) {
        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Options")
      }
      Spacer(modifier = Modifier.width(8.dp))
      IconButton(onClick = {
        onTimerPress()
      }) {
        Icon(
          imageVector = Icons.Outlined.Timer,
          contentDescription = "Timer",
          tint = if (timerVisible) MaterialTheme.colorScheme.primary else LocalContentColor.current
        )
      }
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = { /*TODO*/ },
        containerColor = MaterialTheme.colorScheme.primary
      ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Exercise")
      }
    }
  )
}