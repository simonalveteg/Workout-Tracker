package com.example.android.january2022.ui.session.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.android.january2022.ui.session.SessionEvent
import com.example.android.january2022.utils.Event


@Composable
fun WorkoutTimer(
    timerBackground: Color,
    animatedWidth: Dp,
    onEvent: (Event) -> Unit,
    timerText: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
        color = timerBackground,
    ) {
        Box(Modifier.fillMaxSize()) {
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(animatedWidth),
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
            ) {}
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        onEvent(SessionEvent.TimerChanged(false))
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Remove,
                            contentDescription = "Decrease length of timer"
                        )
                    }
                    Text(
                        text = timerText,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    IconButton(onClick = {
                        onEvent(
                            SessionEvent.TimerChanged(true)
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Increase length of timer"
                        )
                    }
                }
                Row {
                    IconButton(
                        onClick = { onEvent(SessionEvent.TimerReset) },
                        modifier = Modifier
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Replay,
                            contentDescription = "Reset timer"
                        )
                    }
                    IconButton(
                        onClick = { onEvent(SessionEvent.TimerToggled) },
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Pause,
                            contentDescription = "Start or stop timer"
                        )
                    }
                }
            }
        }
    }
}