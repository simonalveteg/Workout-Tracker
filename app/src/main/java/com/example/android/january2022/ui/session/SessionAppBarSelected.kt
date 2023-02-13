package com.example.android.january2022.ui.session

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.january2022.ui.session.actions.ActionSpacer
import com.example.android.january2022.ui.session.actions.ActionSpacerStart
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
      TimerAction(timerVisible = timerVisible) {
        onTimerPress()
      }
      ActionSpacer()
      IconButton(onClick = { onEvent(SessionEvent.OpenGuide) }) {
        Icon(imageVector = Icons.Outlined.OpenInNew, contentDescription = "Open exercise guide.")
      }
      ActionSpacer()
      IconButton(onClick = { /*TODO*/ }) {
        Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Toggle set deletion mode")
      }
    }
  )
}