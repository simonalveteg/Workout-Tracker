package com.alveteg.simon.workouts.ui.exercisepicker.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.alveteg.simon.workouts.db.entities.Exercise

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExerciseCard(
  exercise: Exercise,
  selected: Boolean,
  modifier: Modifier = Modifier,
  onLongClick: () -> Unit = {},
  onClick: () -> Unit
) {
  val color by animateColorAsState(
    targetValue = if (selected) MaterialTheme.colorScheme.surfaceContainerLow else MaterialTheme.colorScheme.surface
  )
  val indicatorWidth by animateDpAsState(targetValue = if (selected) 4.dp else 0.dp)
  val indicatorHeight by animateDpAsState(targetValue = if (selected) 40.dp else 0.dp)

  val primaryMuscles = remember(exercise) {
    exercise.getPrimaryMuscleGroups().joinToString(", ")
  }

  val secondaryMuscles = remember(exercise) {
    val muscles = exercise.getSecondaryMuscleGroups()
    if (muscles.size >= 4) {
      muscles.take(3).joinToString(", ") + ", ..."
    } else {
      muscles.joinToString(", ")
    }  }

  Row(
    modifier = modifier
      .height(60.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Surface(
      modifier = Modifier
        .clip(MaterialTheme.shapes.large)
        .combinedClickable(
          onClick = onClick,
          onLongClick = onLongClick
        )
        .fillMaxHeight()
        .weight(1f),
      color = color,
      shape = MaterialTheme.shapes.large
    ) {
      Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
          .padding(horizontal = 12.dp)
      ) {
        Text(
          text = exercise.title,
          style = MaterialTheme.typography.bodyLargeEmphasized,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier
            .padding(top = 2.dp)
        ) {
          if (primaryMuscles.isNotBlank()) {
            Text(
              text = primaryMuscles,
              style = MaterialTheme.typography.labelMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          if (secondaryMuscles.isNotBlank()) {
            Text(
              text = "($secondaryMuscles)",
              style = MaterialTheme.typography.labelMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          Text(
            text = "5 uses",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary
          )
        }
      }
    }
    Surface(
      modifier = Modifier
        .padding(horizontal = indicatorWidth)
        .height(indicatorHeight)
        .width(indicatorWidth),
      color = MaterialTheme.colorScheme.primary,
      shape = MaterialTheme.shapes.large
      ) { }
  }
}