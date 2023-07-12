package com.example.android.january2022.ui.session.components


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Deselect
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.example.android.january2022.ui.session.SessionEvent
import com.example.android.january2022.ui.session.actions.ActionSpacer
import com.example.android.january2022.ui.session.actions.ActionSpacerStart
import com.example.android.january2022.ui.session.actions.MenuAction
import com.example.android.january2022.ui.session.actions.TimerAction
import com.example.android.january2022.utils.Event

@Composable
fun SessionAppBarSelected(
  onEvent: (SessionEvent) -> Unit,
  onDeleteSession: () -> Unit,
  onDeleteExercise: () -> Unit,
  timerVisible: Boolean,
  onTimerPress: () -> Unit,
) {
  BottomAppBar(
    actions = {
      ActionSpacerStart()
      MenuAction(
        onDelete = onDeleteSession,
      )
      ActionSpacer()
      TimerAction(timerVisible = timerVisible) { onTimerPress() }
      ActionSpacer()
      IconButton(onClick = onDeleteExercise) {
        Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Toggle set deletion mode.")
      }
    },
    floatingActionButton = {
      FloatingActionButton(onClick = { onEvent(SessionEvent.DeselectExercises) }) {
        Icon(imageVector = Icons.Default.Deselect, contentDescription = "Deselect selected exercises.")
      }
    }
  )
}