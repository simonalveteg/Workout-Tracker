package com.example.android.january2022.ui.exercisepicker

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.ui.rework.SmallPill
import com.example.android.january2022.ui.session.actions.OpenInNewAction
import com.example.android.january2022.ui.session.actions.OpenStatsAction
import com.example.android.january2022.utils.Event
import com.example.android.january2022.utils.turnTargetIntoMuscleGroup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseCard(
  exercise: Exercise,
  onEvent: (Event) -> Unit,
  onClick: () -> Unit
) {

  val targets = exercise.targets.map { turnTargetIntoMuscleGroup(it) }
  val equipment = exercise.equipment

  Surface(
    onClick = onClick,
    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
    shape = MaterialTheme.shapes.medium
  ) {
    Row(
      modifier = Modifier
        .padding(start = 14.dp, top = 4.dp, bottom = 4.dp, end = 4.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(
        modifier = Modifier
          .fillMaxHeight()
          .padding(top = 8.dp),
        verticalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = exercise.title,
          modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(0.65f),
          style = MaterialTheme.typography.titleMedium
        )
        Row(
          modifier = Modifier.padding(bottom = 4.dp)
        ) {
          targets.forEach { target ->
            SmallPill(text = target, modifier = Modifier.padding(end = 4.dp))
          }
          SmallPill(text = equipment)
        }
      }
      Row(
        modifier = Modifier.fillMaxHeight()
      ) {
        OpenStatsAction {}
        OpenInNewAction {}
      }
    }
  }
}