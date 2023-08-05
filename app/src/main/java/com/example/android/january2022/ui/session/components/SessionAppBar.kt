package com.example.android.january2022.ui.session.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.android.january2022.ui.TimerState
import com.example.android.january2022.ui.session.actions.ActionSpacer
import com.example.android.january2022.ui.session.actions.ActionSpacerStart
import com.example.android.january2022.ui.session.actions.MenuAction
import com.example.android.january2022.ui.session.actions.TimerAction
import com.example.android.january2022.utils.Event

@Composable
fun SessionAppBar(
  onDeleteSession: () -> Unit,
  timerState: TimerState,
  timerVisible: Boolean,
  onTimerPress: () -> Unit,
  onFAB: () -> Unit
) {
  BottomAppBar(
    actions = {
      ActionSpacerStart()
      MenuAction(
        onDelete = onDeleteSession,
      )
      ActionSpacer()
      TimerAction(timerState, timerVisible, onTimerPress)
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = { onFAB() },
        containerColor = MaterialTheme.colorScheme.primary
      ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Exercise")
      }
    }
  )
}