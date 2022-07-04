package com.example.android.january2022.ui.exercises

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.ui.theme.Shapes
import com.example.android.january2022.utils.Event

@Composable
fun ExerciseCard(
    exercise: Exercise,
    selected: Boolean,
    tonalElevation: Dp = if (selected) 1.dp else 0.dp,
    onEvent: (Event) -> Unit
) {

    val indicatorColor =
        animateColorAsState(if (selected) MaterialTheme.colorScheme.primary else Color.Transparent)

    Row(
        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp, start = 6.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val indicatorHeight by animateDpAsState(targetValue = if (selected) 32.dp else 0.dp)
        Surface(
            color = indicatorColor.value,
            modifier = Modifier
                .height(indicatorHeight)
                .width(4.dp),
            shape = Shapes.small,
        ) {}
        Spacer(Modifier.width(5.dp))
        Surface(
            tonalElevation = tonalElevation,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    Log.d("EC", "Exercise clicked: ${exercise.id}")
                    onEvent(ExerciseEvent.ExerciseSelected(exercise))
                },
            shape = Shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .padding(14.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(end = 50.dp),
                        text = exercise.title.uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        overflow = TextOverflow.Ellipsis
                    )
                    IconButton(
                        onClick = {
                            onEvent(
                                ExerciseEvent.ExerciseInfoClicked(exercise = exercise)
                            )
                        },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreHoriz,
                            contentDescription = "Show more"
                        )
                    }
                }
                Row {
                    SmallChip(
                        modifier = Modifier.padding(4.dp),
                        title = exercise.getMuscleGroup(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(20)
                    )
                    SmallChip(
                        modifier = Modifier.padding(4.dp),
                        title = exercise.equipment.uppercase(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(20)
                    )
                }
            }
        }
    }
}
