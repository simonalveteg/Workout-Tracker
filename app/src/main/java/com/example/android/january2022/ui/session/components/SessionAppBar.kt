package com.example.android.january2022.ui.session.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.android.january2022.ui.TimerState
import com.example.android.january2022.ui.session.actions.ActionSpacer
import com.example.android.january2022.ui.session.actions.ActionSpacerStart
import com.example.android.january2022.ui.session.actions.TimerAction

@Composable
fun SessionAppBar(
    onDeleteSession: () -> Unit,
    timerState: TimerState,
    timerVisible: Boolean,
    onTimerPress: () -> Unit,
    onFAB: () -> Unit,
) {
    BottomAppBar(
        actions = {
            ActionSpacerStart()
            IconButton(onClick = onDeleteSession) {
                Icon(imageVector = Icons.Outlined.DeleteForever, contentDescription = "Delete Session")
            }
            ActionSpacer()
            TimerAction(timerState, timerVisible, onTimerPress)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onFAB() },
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Exercise")
            }
        },
    )
}
