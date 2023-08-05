package com.example.android.january2022.ui.session.actions

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.example.android.january2022.ui.TimerState

@Composable
fun TimerAction(
  timerState: TimerState,
  timerVisible: Boolean,
  onClick: () -> Unit
) {
  val infiniteTransition = rememberInfiniteTransition()
  val activeColor by infiniteTransition.animateColor(
    initialValue = LocalContentColor.current,
    targetValue = MaterialTheme.colorScheme.primary,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 1500),
      repeatMode = RepeatMode.Reverse
    )
  )

  IconButton(onClick = {
    onClick()
  }) {
    Icon(
      imageVector = Icons.Outlined.Timer,
      contentDescription = "Timer",
      tint = if (!timerVisible && timerState.running) activeColor else LocalContentColor.current
    )
  }
}