package com.example.android.january2022.ui.session

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.SessionWithSessionExerciseWithExercise
import com.example.android.january2022.ui.theme.Shapes
import com.example.android.january2022.utils.Event
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun SessionExerciseHistoryCard(
    sessionExercise: SessionWithSessionExerciseWithExercise,
    onEvent: (Event) -> Unit,
    sets: List<GymSet>,
) {
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
    ) {
        Row(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SessionDate(session = sessionExercise.session)
            FlowRow(
                Modifier
                    .animateContentSize(),
                crossAxisAlignment = FlowCrossAxisAlignment.Start,
                mainAxisSpacing = 12.dp,
                mainAxisAlignment = FlowMainAxisAlignment.Start
            ) {
                sets.forEach { set ->
                    key(set.setId) {
                        SetCard(
                            set = set,
                            isSelected = false,
                            onEvent = onEvent,
                            editable = false
                        )
                    }
                }
            }
        }
    }
}