package com.example.android.january2022.ui.session.components

import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import com.example.android.january2022.ui.session.SessionEvent
import com.example.android.january2022.ui.session.actions.*
import com.example.android.january2022.utils.Event

@Composable
fun SessionAppBarExpanded(
  onEvent: (Event) -> Unit,
  onDeleteSession: () -> Unit,
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
      OpenInNewAction { onEvent(SessionEvent.OpenGuide) }
      ActionSpacer()
      OpenStatsAction { /* TODO */ }
    }
  )
}