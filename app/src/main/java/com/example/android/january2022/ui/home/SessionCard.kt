package com.example.android.january2022.ui.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.db.entities.SessionExerciseWithExercise
import com.example.android.january2022.ui.session.SessionDate
import com.example.android.january2022.ui.theme.Shapes
import com.example.android.january2022.utils.Event

@Composable
fun SessionCard(
    session: Session,
    sessionContent: List<SessionExerciseWithExercise>,
    sets: List<GymSet>,
    viewModel: HomeViewModel,
    onEvent: (Event) -> Unit,
    selected: Long
) {
    var expanded by remember { mutableStateOf(false) }

    val haptic = LocalHapticFeedback.current

    val muscleGroups by
    viewModel.getMuscleGroupsForSession(session.sessionId).collectAsState(initial = emptyList())

    val iconRotation = animateFloatAsState(targetValue = if (expanded) 180f else 0f)
    val isSelected by derivedStateOf { session.sessionId == selected }

    Log.d("BSC", "selected session: $selected")

    Surface(
        shape = Shapes.medium,
        tonalElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 4.dp, start = 8.dp, end = 8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onEvent(HomeEvent.SetSelectedSession(session))
                    },
                    onTap = {
                        onEvent(HomeEvent.OnSessionClick(session))
                    }
                )
            }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SessionDate(session)
                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = if (muscleGroups.isNotEmpty()) muscleGroups[0].uppercase() else "",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Row {
                        muscleGroups.forEachIndexed { index, string ->
                            if (index in 1..3) {
                                var newString = string
                                if (index in 1..2 && muscleGroups.size >= index + 2) {
                                    newString = "$string, "
                                }
                                Text(
                                    text = newString.uppercase(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = LocalContentColor.current.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.weight(1f))
                AnimatedVisibility(visible = !isSelected) {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = Icons.Default.ExpandMore,
                            contentDescription = "Toggle Size",
                            modifier = Modifier.rotate(iconRotation.value)
                        )
                    }
                }
                AnimatedVisibility(visible = isSelected) {
                    IconButton(onClick = { onEvent(HomeEvent.OnDeleteSession(session)) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove Session",
                            modifier = Modifier.rotate(iconRotation.value)
                        )
                    }
                }
            }
            AnimatedVisibility(visible = expanded) {
                Divider(modifier = Modifier.padding(top = 8.dp, bottom = 4.dp))
            }
            sessionContent.forEach {
                val setsForSession by derivedStateOf {
                    sets.filter { set -> set.parentSessionExerciseId == it.sessionExercise.sessionExerciseId }
                }
                AnimatedVisibility(visible = expanded) {
                    SessionContent(it, setsForSession)
                }
            }
        }
    }
}