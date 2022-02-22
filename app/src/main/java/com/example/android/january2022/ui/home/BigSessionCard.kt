package com.example.android.january2022.ui.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.room.util.TableInfo
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.db.entities.SessionExerciseWithExercise
import com.example.android.january2022.ui.session.SessionEvent
import com.example.android.january2022.ui.theme.Shapes
import com.example.android.january2022.utils.Event
import java.text.SimpleDateFormat
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import java.util.*

@Composable
fun BigSessionCard(
    session: Session,
    sessionContent: List<SessionExerciseWithExercise>,
    sets: List<GymSet>,
    viewModel: HomeViewModel,
    onEvent: (Event) -> Unit,
    selected: Long
) {
    var expanded by remember { mutableStateOf(false) }
    val startMonth = SimpleDateFormat(
        "MMM", Locale.ENGLISH
    ).format(session.startTimeMilli)
    val startDay = SimpleDateFormat(
        "dd", Locale.ENGLISH
    ).format(session.startTimeMilli)

    val haptic = LocalHapticFeedback.current

    val muscleGroups by
    viewModel.getMuscleGroupsForSession(session.sessionId).collectAsState(initial = emptyList())

    val iconRotation = animateFloatAsState(targetValue = if (expanded) 180f else 0f)
    val isSelected = session.sessionId == selected

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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Text(
                        text = startMonth,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = startDay,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = if (muscleGroups.isNotEmpty()) muscleGroups[0].uppercase() else "",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Row {
                        muscleGroups.forEachIndexed { index, string ->
                            if(index in 1..3) {
                                var newString = string
                                if(index in 1..2 && muscleGroups.size >= index+2) {
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
                AnimatedVisibility(visible = expanded) {
                    SessionContent(
                        it, sets.filter { set -> set.parentSessionExerciseId == it.sessionExercise.sessionExerciseId  }
                    )
                }
            }
        }
    }
}