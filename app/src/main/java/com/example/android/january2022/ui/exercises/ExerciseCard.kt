package com.example.android.january2022.ui.exercises

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.ui.session.SessionEvent
import com.example.android.january2022.utils.Event

@Composable
fun ExerciseCard(
    exercise: Exercise,
    onEvent: (Event) -> Unit
) {
    Card() {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            IconButton(
                onClick = {
                    onEvent(
                        ExerciseEvent.ExerciseInfoClicked(exercise = exercise)
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Show Exercise Info"
                )
            }

            Column() {
                Text(exercise.exerciseTitle)
                Text(exercise.muscleGroups)
                Text(exercise.equipment)
            }
        }
    }
}