package com.example.android.january2022.ui.session.components

import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import com.example.android.january2022.ui.TimerState
import com.example.android.january2022.ui.session.SessionEvent
import com.example.android.january2022.ui.session.actions.ActionSpacer
import com.example.android.january2022.ui.session.actions.ActionSpacerStart
import com.example.android.january2022.ui.session.actions.MenuAction
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
            MenuAction(
                onDelete = onDeleteSession,
            )
            ActionSpacer()
            TimerAction(timerState = timerState, timerVisible = timerVisible) { onTimerPress() }
            ActionSpacer()
            OpenInNewAction { onEvent(SessionEvent.OpenGuide) }
            ActionSpacer()
            OpenStatsAction { /* TODO */ }
        },
    )
}
