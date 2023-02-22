package com.example.android.january2022.ui.session

import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import com.example.android.january2022.ui.session.actions.*
import com.example.android.january2022.utils.Event

@Composable
fun SessionAppBarExpanded(
  onEvent: (Event) -> Unit,
  timerVisible: Boolean,
  onTimerPress: () -> Unit
) {
  BottomAppBar(
    actions = {
      ActionSpacerStart()
      MenuAction { /* TODO */ }
      ActionSpacer()
      TimerAction(timerVisible = timerVisible) { onTimerPress() }
      ActionSpacer()
      OpenInNewAction { onEvent(SessionEvent.OpenGuide) }
      ActionSpacer()
      OpenStatsAction { /* TODO */ }
    }
  )
}