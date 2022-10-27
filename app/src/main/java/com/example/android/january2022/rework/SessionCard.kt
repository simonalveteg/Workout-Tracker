package com.example.android.january2022.rework

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.ui.home.HomeEvent
import com.example.android.january2022.ui.theme.Shapes
import com.example.android.january2022.utils.Event

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SessionCard(
  session: Session,
  onEvent: (Event) -> Unit
) {

  val haptic = LocalHapticFeedback.current

  Surface(
    shape = Shapes.medium,
    tonalElevation = 4.dp,
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 8.dp, vertical = 4.dp)
      .combinedClickable(
        onLongClick = {
          haptic.performHapticFeedback(HapticFeedbackType.LongPress)
          onEvent(HomeEvent.SetSelectedSession(session))
        }
      ) {
        onEvent(HomeEvent.OnSessionClick(session))
      }
  ) {
    Text(text = session.sessionId.toString())
  }
}