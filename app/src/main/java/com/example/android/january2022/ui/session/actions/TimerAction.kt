package com.example.android.january2022.ui.session.actions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun TimerAction(
  timerVisible: Boolean,
  onClick: () -> Unit
) {
  IconButton(onClick = {
    onClick()
  }) {
    Icon(
      imageVector = Icons.Outlined.Timer,
      contentDescription = "Timer",
      tint = if (timerVisible) MaterialTheme.colorScheme.primary else LocalContentColor.current
    )
  }
}