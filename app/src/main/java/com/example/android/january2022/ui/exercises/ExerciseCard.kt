package com.example.android.january2022.ui.exercises

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.runtime.Composable
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
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp).fillMaxWidth()) {
            Row(Modifier.padding(bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(exercise.exerciseTitle.uppercase(), style = MaterialTheme.typography.h5)
                Spacer(modifier = Modifier.weight(1f))
                Log.d("EC","Exercise ${exercise.exerciseId} selected $selected")
                Icon(
                    imageVector = if(selected) Icons.Filled.RadioButtonChecked else Icons.Filled.RadioButtonUnchecked,
                    contentDescription = "Exercise Selected Identifier"
                )
            }
            Column(Modifier.padding(start = 8.dp)) {

                Row(Modifier.padding(bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
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