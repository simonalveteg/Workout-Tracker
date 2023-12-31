package com.example.android.january2022.ui.session.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.android.january2022.timer.toTimerString
import com.example.android.january2022.ui.TimerState
import com.example.android.january2022.ui.session.SessionEvent
import com.example.android.january2022.utils.Event

@Composable
fun TimerBar(
    timerState: TimerState,
    onEvent: (Event) -> Unit,
) {
    val timerTime = timerState.time
    val timerRunning = timerState.running
    val timerMaxTime = timerState.maxTime
    val maxWidth = LocalConfiguration.current.screenWidthDp
    val timerWidth = maxWidth.times(timerTime.toFloat() / timerMaxTime).toInt().dp
    val timerToggleIcon = if (timerRunning) Icons.Default.Pause else Icons.Default.PlayArrow
    val timerTimeText =
        if (timerTime > 0L) timerTime.toTimerString() else timerMaxTime.toTimerString()
    val timerTonalElevation by animateDpAsState(targetValue = if (timerRunning) 140.dp else 14.dp)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        tonalElevation = 8.dp,
    ) {
        Box {
            Surface(
                modifier = Modifier
                    .width(timerWidth)
                    .height(50.dp),
                tonalElevation = timerTonalElevation,
            ) {}
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = { onEvent(SessionEvent.TimerDecreased) }) {
                        Icon(Icons.Default.Remove, "Decrease time")
                    }
                    Text(
                        text = timerTimeText,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(50.dp),
                    )
                    IconButton(onClick = { onEvent(SessionEvent.TimerIncreased) }) {
                        Icon(Icons.Default.Add, "Increase time")
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
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
