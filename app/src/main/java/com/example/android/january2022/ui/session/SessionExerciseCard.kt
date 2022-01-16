package com.example.android.january2022.ui.session

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.SessionExerciseWithExercise
import com.example.android.january2022.utils.Event


@ExperimentalComposeUiApi
@OptIn(ExperimentalAnimationApi::class)
@ExperimentalFoundationApi
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

    Card(
        Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 4.dp, start = 8.dp, end = 8.dp)
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
        Column {
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 8.dp, top = 8.dp, end = 2.dp)
            ) {
                Text(
                    text = sessionExercise.exercise.exerciseTitle,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                )
                if(isSelected){
                    IconButton(
                        onClick = {
                            onEvent(
                                SessionEvent.OnSessionExerciseInfoClicked(
                                    sessionExercise.exercise.exerciseId
                                )
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Show Exercise Info"
                        )
                    }
                    IconButton(
                        onClick = { onEvent(SessionEvent.OnDeleteSessionExercise(sessionExercise)) },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Remove Exercise from Session"
                        )
                    }
                } else {
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
            sets.forEach { set ->

                key(set.setId) {
                    AnimatedVisibility(
                        visible = !set.deleted,
                        exit = shrinkVertically(
                            animationSpec = tween(
                                durationMillis = 400,
                                delayMillis = 25,
                                easing = LinearOutSlowInEasing
                            )
                        ),
                        enter = expandVertically(
                            animationSpec = tween(
                                durationMillis = 400,
                                delayMillis = 25,
                                easing = LinearOutSlowInEasing
                            )
                        )
                    ) {
                        SetCard(set, viewModel::onEvent)
                    }


                }
            }

        }
    }

}