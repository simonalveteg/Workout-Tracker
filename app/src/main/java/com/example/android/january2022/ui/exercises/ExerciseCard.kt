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
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.ui.theme.Shapes
import com.example.android.january2022.utils.Event

@Composable
fun ExerciseCard(
    exercise: Exercise,
    selected: Boolean,
    inPicker: Boolean,
    onEvent: (Event) -> Unit
) {
    val cornerCutDp by animateDpAsState(targetValue = if (selected) 24.dp else 0.dp)

    Surface(
        onClick = {
            if (inPicker) {
                Log.d("EC", "Exercise clicked: ${exercise.exerciseId}")
                onEvent(ExerciseEvent.ExerciseSelected(exercise))
            }
        },
        tonalElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 4.dp, start = 8.dp, end = 8.dp),
        shape = Shapes.medium
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
            Row(
                modifier = Modifier
                    .padding(14.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        exercise.exerciseTitle.uppercase(),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        exercise.muscleGroups.uppercase() + ", " +
                                exercise.equipment.uppercase(),
                        style = MaterialTheme.typography.bodySmall,
                        color = LocalContentColor.current.copy(alpha = 0.7f)
                    )
                }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = {
                    onEvent(
                        ExerciseEvent.ExerciseInfoClicked(exercise = exercise)
                    )
                }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Show more")
                }
            }
        }
    }
}
