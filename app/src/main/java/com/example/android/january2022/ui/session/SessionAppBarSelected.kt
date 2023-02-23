package com.example.android.january2022.ui.session


import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Deselect
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.android.january2022.ui.session.actions.ActionSpacer
import com.example.android.january2022.ui.session.actions.ActionSpacerStart
import com.example.android.january2022.ui.session.actions.MenuAction
import com.example.android.january2022.ui.session.actions.TimerAction
import com.example.android.january2022.utils.Event

@Composable
fun SessionAppBarSelected(
  onEvent: (Event) -> Unit,
  onDeleteSession: () -> Unit,
  onDeleteExercise: () -> Unit,
  timerVisible: Boolean,
  onTimerPress: () -> Unit,
  onTime: () -> Unit
) {
  BottomAppBar(
    actions = {
      ActionSpacerStart()
      MenuAction(
        onDelete = onDeleteSession,
        onTime = onTime
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