package com.example.android.january2022.ui.home

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.android.january2022.R
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.db.entities.SessionExerciseWithExercise
import com.example.android.january2022.utils.Event
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun SessionCard(
    session: Session,
    sessionContent: List<SessionExerciseWithExercise>,
    sets: List<GymSet>,
    viewModel: HomeViewModel,
    onEvent: (Event) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val startDate = SimpleDateFormat(
        "dd-MM-yy",
        Locale.ENGLISH
    ).format(session.startTimeMilli)
    val startTime = SimpleDateFormat(
        "HH:mm",
        Locale.ENGLISH
    ).format(session.startTimeMilli)
    val muscleGroups =
        viewModel.getMuscleGroupsForSession(session.sessionId).collectAsState(initial = emptyList())
    val iconRotation = animateFloatAsState(targetValue = if(expanded) 180f else 0f)


    Surface(
        tonalElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable {
                onEvent(HomeEvent.OnSessionClick(session))
            }
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 100,
                    delayMillis = 5,
                    easing = LinearOutSlowInEasing
                )
            )
    ) {
        Column(
            Modifier
                .padding(vertical = 16.dp, horizontal = 16.dp)
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 100,
                        delayMillis = 5,
                        easing = LinearOutSlowInEasing
                    )
                )
        ) {
            Row {
                Column {
                    Text(text = startDate)
                    Row {
                        muscleGroups.value.filter { it.isNotEmpty() }.take(3).forEach {
                            Text(text = it)
                            Spacer(Modifier.width(4.dp))
                        }
                    }
                }
                Spacer(Modifier.weight(1f))
                IconButton(
                    onClick = {
                        expanded = !expanded
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ExpandMore,
                        contentDescription =
                        if (expanded) {
                            stringResource(R.string.show_less)
                        } else {
                            stringResource(
                                R.string.show_more
                            )
                        },
                        modifier = Modifier.rotate(iconRotation.value)
                    )
                }
            }
            if (expanded) {
                Box(Modifier.padding(top = 12.dp, bottom = 8.dp, start = 0.dp, end = 0.dp)) {
                    Divider(color = MaterialTheme.colorScheme.onPrimaryContainer, thickness = 1.dp)
                }
                sessionContent.filter { it.sessionExercise.parentSessionId == session.sessionId }
                    .forEach { sessionExercise ->
                        SessionContent(sessionExercise, sets)
                    }
            }
        }
    }
}