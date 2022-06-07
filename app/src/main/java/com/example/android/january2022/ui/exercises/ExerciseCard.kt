package com.example.android.january2022.ui.exercises

import android.util.Log
import android.view.RoundedCorner
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
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

    val indicatorColor =
        animateColorAsState(targetValue = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent)

    Row(
        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp, start = 6.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val indicatorHeight by animateDpAsState(targetValue = if(selected) 32.dp else 0.dp)
        Surface(
            color = indicatorColor.value,
            modifier = Modifier
                .height(indicatorHeight)
                .width(4.dp),
            shape = Shapes.small
        ) {}
        Spacer(Modifier.width(5.dp))
        Surface(
            tonalElevation = if (selected) 1.dp else 0.dp,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (inPicker) {
                        Log.d("EC", "Exercise clicked: ${exercise.id}")
                        onEvent(ExerciseEvent.ExerciseSelected(exercise))
                    }
                },
            shape = Shapes.medium
        ) {
            Row(
                modifier = Modifier
                    .padding(14.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(8f)) {
                    Text(
                        exercise.title.uppercase(),
                        style = MaterialTheme.typography.headlineSmall,
                        overflow = TextOverflow.Ellipsis

                    )
                    Text(
                        exercise.getMuscleGroup() + ", " +
                                exercise.equipment.uppercase(),
                        style = MaterialTheme.typography.bodySmall,
                        color = LocalContentColor.current.copy(alpha = 0.7f)
                    )
                }
                IconButton(
                    onClick = {
                        onEvent(
                            ExerciseEvent.ExerciseInfoClicked(exercise = exercise)
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    Icon(imageVector = Icons.Default.MoreHoriz, contentDescription = "Show more")
                }
            }
        }
    }
}
