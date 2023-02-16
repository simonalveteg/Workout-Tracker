package com.example.android.january2022.ui.exercisepicker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.android.january2022.db.MuscleGroup
import com.example.android.january2022.utils.Event

@Composable
fun MusclegroupBottomsheet(
  selectedMusclegroups: List<String>,
  onEvent: (Event) -> Unit
) {
  val muscles = MuscleGroup.getAllMuscleGroups().sorted()

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(top = 16.dp, bottom = 20.dp)
      .padding(horizontal = 16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      text = "Filter by Body-parts",
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.titleLarge,
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 12.dp)
    )
    LazyVerticalGrid(
      columns = GridCells.Adaptive(120.dp),
      horizontalArrangement = Arrangement.Center
    ) {
      items(muscles) { muscle ->
        MuscleButton(
          muscle = muscle,
          selected = selectedMusclegroups.contains(muscle)
        ) {
          onEvent(PickerEvent.SelectMuscle(muscle))
        }
      }
    }
    TextButton(onClick = { onEvent(PickerEvent.DeselectMuscles) }) {
      Text(
        text = "Deselect All".uppercase(),
        style = MaterialTheme.typography.labelLarge
      )
    }
  }
}