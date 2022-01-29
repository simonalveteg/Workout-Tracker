package com.example.android.january2022.ui.exercises

import android.util.Log
import android.view.RoundedCorner
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
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
    inPicker: Boolean,
    onEvent: (Event) -> Unit
) {
    val roundedCornerDp = 20.dp
    val cornerCutDp by animateDpAsState(targetValue = if (selected) 34.dp else 0.dp)

    Surface(
        onClick = {
            if (inPicker) {
                Log.d("EC", "Exercise clicked: ${exercise.exerciseId}")
                onEvent(ExerciseEvent.ExerciseSelected(exercise))
            }
        },
        tonalElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(roundedCornerDp)
    ) {
        Box {
            // row containing the upper-right selection-indicator
            Row(horizontalArrangement = Arrangement.End) {
                Spacer(modifier = Modifier.weight(1f))
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(cornerCutDp),
                    shape = CutCornerShape(bottomStart = 100f)
                ) {}
            }

            Column(
                Modifier
                    .padding(start = 16.dp, top = 12.dp, bottom = 4.dp, end = 8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    exercise.exerciseTitle.uppercase(),
                    style = MaterialTheme.typography.headlineSmall
                )
                Column(Modifier.padding(start = 8.dp, top = 8.dp)) {
                    Row(
                        Modifier.padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("MUSCLE GROUPS:", style = MaterialTheme.typography.labelSmall)
                        Spacer(Modifier.width(8.dp))
                        Text(exercise.muscleGroups.uppercase(), style = MaterialTheme.typography.bodySmall)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("EQUIPMENT:", style = MaterialTheme.typography.labelSmall)
                        Spacer(Modifier.width(8.dp))
                        Text(exercise.equipment.uppercase(), style = MaterialTheme.typography.bodySmall)
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
