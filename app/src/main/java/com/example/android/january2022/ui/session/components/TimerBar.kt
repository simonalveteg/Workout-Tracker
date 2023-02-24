package com.example.android.january2022.ui.session.components

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.android.january2022.ui.TimerState
import com.example.android.january2022.ui.session.SessionEvent
import com.example.android.january2022.ui.toTimerString
import com.example.android.january2022.utils.Event
import kotlinx.coroutines.channels.consumeEach

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
  val timerTimeText =
    if (timerTime > 0L) timerTime.toTimerString() else timerMaxTime.toTimerString()
  val timerTonalElevation by animateDpAsState(targetValue = if (timerRunning) 140.dp else 14.dp)

  val context = LocalContext.current
  val vibrator = if (Build.VERSION.SDK_INT >= 31) {
    (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator;
  } else {
    context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
  }

  LaunchedEffect(key1 = true) {
    timerState.finishedEvent.consumeEach {
      if (it) {
        val v = 160L
        val vibrationArray = longArrayOf(0, v, 120, v, 120, v)
        val vibrationEffect = VibrationEffect.createWaveform(vibrationArray, -1)
        vibrator.vibrate(vibrationEffect)
      }
    }
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
