package com.example.android.january2022.ui.session


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.example.android.january2022.ui.session.actions.ActionSpacer
import com.example.android.january2022.ui.session.actions.ActionSpacerStart
import com.example.android.january2022.ui.session.actions.OpenInNewAction
import com.example.android.january2022.ui.session.actions.TimerAction
import com.example.android.january2022.utils.Event

@Composable
fun SessionAppBarSelected(
  onEvent: (Event) -> Unit,
  timerVisible: Boolean,
  onTimerPress: () -> Unit
) {
  BottomAppBar(
    actions = {
      ActionSpacerStart()
      IconButton(onClick = { /*TODO*/ }) {
        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Options")
      }
      ActionSpacer()
      TimerAction(timerVisible = timerVisible) { onTimerPress() }
      ActionSpacer()
      IconButton(onClick = { onEvent(SessionEvent.RemoveSelectedExercises) }) {
        Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Toggle set deletion mode")
      }
    }
  )
}