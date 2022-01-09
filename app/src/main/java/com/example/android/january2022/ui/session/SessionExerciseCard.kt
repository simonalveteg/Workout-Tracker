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
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.SessionExerciseWithExercise
import com.example.android.january2022.utils.Event


@OptIn(ExperimentalAnimationApi::class)
@ExperimentalFoundationApi
@Composable
fun SessionExerciseCard(
    sessionExercise: SessionExerciseWithExercise,
    viewModel: SessionViewModel,
    selected: Long,
    allSets: List<GymSet>,
    onEvent: (Event) -> Unit,
) {
    val sets = mutableListOf<GymSet>()
    allSets.forEach { set ->
        if (set.parentSessionExerciseId == sessionExercise.sessionExercise.sessionExerciseId) {
            sets.add(set)
        }
    }

    val removedSet by viewModel.removedSet.observeAsState()

    val isSelected = sessionExercise.sessionExercise.sessionExerciseId == selected

    Card(
        Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 4.dp, start = 8.dp, end = 8.dp)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 100,
                    delayMillis = 0,
                    easing = LinearOutSlowInEasing
                )
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onEvent(SessionEvent.SetSelectedSessionExercise(sessionExercise))
                    }
                )
            }
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(start = 2.dp, bottom = 8.dp, top = 8.dp, end = 2.dp)
            ) {
                IconButton(
                    onClick = { onEvent(SessionEvent.OnSessionExerciseInfoClicked(sessionExercise))}
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Show Exercise Info"
                    )
                }
                Text(
                    text = sessionExercise.exercise.exerciseTitle,
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                )
                IconButton(
                    onClick = { onEvent(SessionEvent.OnAddSet(sessionExercise)) },

                    ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Exercise"
                    )
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