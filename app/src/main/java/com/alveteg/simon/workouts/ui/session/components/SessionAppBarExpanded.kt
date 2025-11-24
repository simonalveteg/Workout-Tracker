package com.alveteg.simon.workouts.ui.session.components

import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import com.alveteg.simon.workouts.ui.TimerState
import com.alveteg.simon.workouts.ui.session.SessionEvent
import com.alveteg.simon.workouts.ui.session.actions.*

@Composable
fun SessionAppBarExpanded(
  onEvent: (SessionEvent) -> Unit,
  onDeleteSession: () -> Unit,
  timerState: TimerState,
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
      TimerAction(timerState = timerState, timerVisible = timerVisible) { onTimerPress() }
      ActionSpacer()
      OpenInNewAction { onEvent(SessionEvent.OpenGuide) }
      ActionSpacer()
      OpenStatsAction { /* TODO */ }
    }
  )
}