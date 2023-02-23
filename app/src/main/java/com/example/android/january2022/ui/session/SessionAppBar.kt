package com.example.android.january2022.ui.session

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.android.january2022.ui.session.actions.ActionSpacer
import com.example.android.january2022.ui.session.actions.ActionSpacerStart
import com.example.android.january2022.ui.session.actions.MenuAction
import com.example.android.january2022.ui.session.actions.TimerAction
import com.example.android.january2022.utils.Event
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun SessionAppBar(
  onEvent: (Event) -> Unit,
  onDeleteSession: () -> Unit,
  timerVisible: Boolean,
  onTimerPress: () -> Unit,
  onTime: () -> Unit,
  onFAB: () -> Unit
) {
  BottomAppBar(
    actions = {
      ActionSpacerStart()
      MenuAction(
        onDelete = onDeleteSession,
        onTime = onTime
      )
      ActionSpacer()
      TimerAction(timerVisible, onTimerPress)
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