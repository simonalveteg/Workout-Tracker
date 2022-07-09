package com.example.android.january2022.ui.session.components

import android.app.DatePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.ui.session.sessionTitle
import com.example.android.january2022.utils.Event


@Composable
fun SessionAppBar(
    session: Session,
    timerVisible: MutableState<Boolean>,
    timerColor: Color,
    mDatePickerDialog: DatePickerDialog,
    scrollBehavior: TopAppBarScrollBehavior,
    timerBackground: Color,
    animatedWidth: Dp,
    onEvent: (Event) -> Unit,
    timerText: String
) {
    val dropdownExpanded = remember { mutableStateOf(false) }
    Column {
        LargeTopAppBar(
            title = { Text(text = sessionTitle(session)) },
            colors = TopAppBarDefaults.mediumTopAppBarColors(),
            actions = {
                IconButton(onClick = { timerVisible.value = !timerVisible.value }) {
                    Icon(
                        imageVector = Icons.Outlined.Timer,
                        contentDescription = "Timer",
                        tint = timerColor
                    )
                }
                DropdownMenu(
                    expanded = dropdownExpanded.value,
                    onDismissRequest = { dropdownExpanded.value = false }) {
                    DropdownMenuItem(
                        text = { Text("Change Date") },
                        onClick = {
                            mDatePickerDialog.show()
                            dropdownExpanded.value = false
                        }
                    )
                }
                IconButton(onClick = { dropdownExpanded.value = true }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Localized description"
                    )
                }
            },
            scrollBehavior = scrollBehavior
        )
        AnimatedVisibility(visible = timerVisible.value) {
            WorkoutTimer(timerBackground, animatedWidth, onEvent, timerText)
        }
    }
}