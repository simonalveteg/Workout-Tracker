package com.example.android.january2022.ui.exercises

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.utils.Event

@Composable
fun ExerciseCard(
    exercise: Exercise,
    selected: Boolean,
    onEvent: (Event) -> Unit
) {
    val cornerCutDp by animateDpAsState(targetValue = if(selected) 24.dp else 0.dp)
    Box(Modifier.background(color = MaterialTheme.colors.primary)) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = CutCornerShape(topEnd = cornerCutDp)
        ) {
            Column(
                Modifier
                    .padding(start = 16.dp, top = 12.dp, bottom = 4.dp, end = 8.dp)
                    .fillMaxWidth()
            ) {
                Text(exercise.exerciseTitle.uppercase(), style = MaterialTheme.typography.h5)
                Column(Modifier.padding(start = 8.dp, top = 8.dp)) {
                    Row(
                        Modifier.padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("MUSCLE GROUPS", style = MaterialTheme.typography.subtitle2)
                        Spacer(Modifier.width(8.dp))
                        Text(exercise.muscleGroups)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("EQUIPMENT", style = MaterialTheme.typography.subtitle2)
                        Spacer(Modifier.width(8.dp))
                        Text(exercise.equipment)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(Modifier.weight(1f))
                        OutlinedButton(
                            onClick = {
                                onEvent(
                                    ExerciseEvent.ExerciseInfoClicked(exercise = exercise)
                                )
                            }
                        ) {
                            Text("VIEW ON MUSCLEWIKI")
                        }
                    }
                }
            }
        }
    }
}