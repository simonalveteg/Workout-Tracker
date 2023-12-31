package com.example.android.january2022.ui.session.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Deselect
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.example.android.january2022.ui.TimerState
import com.example.android.january2022.ui.session.SessionEvent
import com.example.android.january2022.ui.session.actions.ActionSpacer
import com.example.android.january2022.ui.session.actions.ActionSpacerStart
import com.example.android.january2022.ui.session.actions.TimerAction

@Composable
fun SessionAppBarSelected(
    onEvent: (SessionEvent) -> Unit,
    onDeleteSession: () -> Unit,
    onDeleteExercise: () -> Unit,
    timerState: TimerState,
    timerVisible: Boolean,
    onTimerPress: () -> Unit,
) {
    BottomAppBar(
        actions = {
            ActionSpacerStart()
            IconButton(onClick = onDeleteSession) {
                Icon(imageVector = Icons.Outlined.DeleteForever, contentDescription = "Delete Session.")
            }
            ActionSpacer()
            TimerAction(timerState = timerState, timerVisible = timerVisible) { onTimerPress() }
            ActionSpacer()
            IconButton(onClick = onDeleteExercise) {
                Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Toggle set deletion mode.")
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(SessionEvent.DeselectExercises) }) {
                Icon(imageVector = Icons.Default.Deselect, contentDescription = "Deselect selected exercises.")
            }
        },
    )
}
