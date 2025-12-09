package com.alveteg.simon.workouts.ui.exercisepicker.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.alveteg.simon.workouts.db.entities.Exercise
import com.alveteg.simon.workouts.ui.exercisepicker.PickerEvent
import com.alveteg.simon.workouts.ui.session.actions.OpenInNewAction
import com.alveteg.simon.workouts.ui.session.actions.OpenStatsAction
import com.alveteg.simon.workouts.ui.session.components.SmallPill
import com.alveteg.simon.workouts.utils.Event

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExerciseCard(
  exercise: Exercise,
  selected: Boolean,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {

  val targets = exercise.getMuscleGroups()
  val equipment = exercise.equipment
  val color by animateColorAsState(targetValue = if (selected) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.surface)
  val indicatorColor by animateColorAsState(if (selected) MaterialTheme.colorScheme.primary else Color.Transparent)

  val localDensity = LocalDensity.current
  var rowHeightDp by remember { mutableStateOf(0.dp) }

  val indicatorHeight by animateDpAsState(targetValue = if (selected) rowHeightDp else 0.dp)

  Row(
    modifier = modifier
      .fillMaxWidth()
      .onGloballyPositioned { coordinates ->
        // Set column height using the LayoutCoordinates
        rowHeightDp = with(localDensity) {
          coordinates.size.height.minus(95).toDp()
        }
      }, verticalAlignment = Alignment.CenterVertically
  ) {
    Surface(
      color = indicatorColor,
      shape = MaterialTheme.shapes.small,
      modifier = Modifier
        .width(3.dp)
        .height(indicatorHeight)
    ) {}
    Spacer(modifier = Modifier.width(4.dp))
    Surface(
      onClick = onClick,
      modifier = Modifier
        .fillMaxWidth()
        .defaultMinSize(minHeight = 70.dp),
      color = color,
      shape = MaterialTheme.shapes.medium
    ) {
      Column(
        modifier = Modifier.padding(start = 14.dp, top = 12.dp, bottom = 4.dp, end = 4.dp),
        verticalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = exercise.title,
          modifier = Modifier.padding(bottom = 8.dp),
          style = MaterialTheme.typography.titleMediumEmphasized
        )
        Row(
          modifier = Modifier.padding(bottom = 4.dp)
        ) {
          targets.forEach { target ->
            SmallPill(text = target, modifier = Modifier.padding(end = 4.dp))
          }
          equipment.forEach { eq ->
            SmallPill(text = eq)
          }
        }
      }
    }
  }
}