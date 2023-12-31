package com.example.android.january2022.ui.session.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.example.android.january2022.ui.TimerState
import com.example.android.january2022.ui.session.SessionEvent
import com.example.android.january2022.ui.session.actions.ActionSpacer
import com.example.android.january2022.ui.session.actions.ActionSpacerStart
import com.example.android.january2022.ui.session.actions.OpenInNewAction
import com.example.android.january2022.ui.session.actions.OpenStatsAction
import com.example.android.january2022.ui.session.actions.TimerAction

@Composable
fun SessionAppBarExpanded(
    onEvent: (SessionEvent) -> Unit,
    onDeleteSession: () -> Unit,
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
            OpenInNewAction { onEvent(SessionEvent.OpenGuide) }
            ActionSpacer()
            OpenStatsAction { /* TODO */ }
        },
    )
}
