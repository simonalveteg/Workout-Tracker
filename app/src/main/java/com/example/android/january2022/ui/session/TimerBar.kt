package com.example.android.january2022.ui.session

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.android.january2022.ui.rework.TimerState
import com.example.android.january2022.ui.rework.toTimerString
import com.example.android.january2022.utils.Event
import timber.log.Timber

@Composable
fun TimerBar(
  timerState: TimerState,
  onEvent: (Event) -> Unit
) {
  val timerTime by timerState.time.collectAsState(initial = 0L)
  val timerRunning by timerState.isRunning.collectAsState(initial = false)
  val timerMaxTime by timerState.maxTime.collectAsState(initial = 1000L)
  val maxWidth = LocalConfiguration.current.screenWidthDp
  val timerWidth by remember {
    derivedStateOf {
      maxWidth.times(timerTime.toFloat().div(timerMaxTime)).toInt().dp
    }
  }
  val timerToggleIcon = if (timerRunning) Icons.Default.Pause else Icons.Default.PlayArrow
  val timerTimeText = if(timerTime > 0L) timerTime.toTimerString() else timerMaxTime.toTimerString()
  val timerTonalElevation by animateDpAsState(targetValue = if(timerRunning) 140.dp else 14.dp)

  LaunchedEffect(key1 = timerState) {
    Timber.d("timerState: ${timerState.maxTime.value}, ${timerState.time.value}, ${timerState.isRunning.value}")
  }

  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .height(50.dp),
    tonalElevation = 8.dp
  ) {
    Box {
      Surface(
        modifier = Modifier
          .width(timerWidth)
          .height(50.dp),
        tonalElevation = timerTonalElevation
      ) {}
      Row(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(onClick = { onEvent(SessionEvent.TimerDecreased) }) {
            Icon(Icons.Default.Remove, "Decrease time")
          }
          Text(
            text = timerTimeText,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(50.dp)
          )
          IconButton(onClick = { onEvent(SessionEvent.TimerIncreased) }) {
            Icon(Icons.Default.Add, "Increase time")
          }
        }
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(onClick = { onEvent(SessionEvent.TimerReset) }) {
            Icon(Icons.Default.Refresh, "Reset Timer")
          }
          IconButton(onClick = { onEvent(SessionEvent.TimerToggled) }) {
            Icon(timerToggleIcon, "Toggle Timer")
          }
        }
      }
    }
  }
}
