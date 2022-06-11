package com.example.android.january2022.ui.session

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import com.google.accompanist.flowlayout.FlowRow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.SessionExerciseWithExercise
import com.example.android.january2022.ui.theme.Shapes
import com.example.android.january2022.utils.Event
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowMainAxisAlignment


@Composable
fun SessionExerciseCard(
    sessionExercise: SessionExerciseWithExercise,
    viewModel: SessionViewModel,
    selected: Long,
    sets: List<GymSet>,
    onEvent: (Event) -> Unit,
) {

    val haptic = LocalHapticFeedback.current
    val isSelected = sessionExercise.sessionExercise.sessionExerciseId == selected
    val errorColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
    )
    Log.d("SEC", "selected sessionExercise: $selected")


    Surface(
        shape = Shapes.medium,
        tonalElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    delayMillis = 0,
                    easing = LinearOutSlowInEasing
                )
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onEvent(SessionEvent.SetSelectedSessionExercise(sessionExercise))
                    }
                )
            }
    ) {
        Column(Modifier.padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 8.dp)) {
            Row(
                modifier = Modifier
                    .padding(bottom = 0.dp)
            ) {
                Text(
                    text = sessionExercise.exercise.title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                )
                AnimatedVisibility(visible = isSelected) {
                    IconButton(
                        onClick = { onEvent(SessionEvent.OnDeleteSessionExercise(sessionExercise)) },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Remove Exercise from Session"
                        )
                    }
                }
                AnimatedVisibility(visible = !isSelected) {
                    Row {
                        IconButton(
                            onClick = {
                                onEvent(
                                    SessionEvent.OnSessionExerciseInfoClicked(
                                        sessionExercise.exercise.id
                                    )
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = "Show Exercise Options",
                            )
                        }
                        IconButton(
                            onClick = {
                                onEvent(
                                    SessionEvent.OnSessionExerciseHistoryClicked(
                                        sessionExercise.exercise.id
                                    )
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.History,
                                contentDescription = "Show Exercise History",
                            )
                        }
                    }
                }
            }
            FlowRow(
                Modifier
                    .padding(start = 2.dp)
                    .animateContentSize(),
                crossAxisAlignment = FlowCrossAxisAlignment.Start,
                mainAxisSpacing = 12.dp,
                mainAxisAlignment = FlowMainAxisAlignment.Start
            ) {
                sets.forEach { set ->

                    key(set.setId) {
                        AnimatedVisibility(
                            visible = !set.deleted,
                            exit = shrinkHorizontally(
                                animationSpec = tween(
                                    durationMillis = 400,
                                    delayMillis = 25,
                                    easing = LinearOutSlowInEasing
                                )
                            ),
                            enter = expandHorizontally(
                                animationSpec = tween(
                                    durationMillis = 400,
                                    delayMillis = 25,
                                    easing = LinearOutSlowInEasing
                                )
                            )
                        ) {
                            NewSetCard(set, isSelected, errorColor, viewModel::onEvent)
                        }
                    }
                }
                IconButton(
                    onClick = { onEvent(SessionEvent.OnAddSet(sessionExercise)) },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Set to Exercise"
                    )
                }
            }
        }
    }

}