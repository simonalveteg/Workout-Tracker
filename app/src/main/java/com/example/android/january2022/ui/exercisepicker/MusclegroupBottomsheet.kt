package com.example.android.january2022.ui.exercisepicker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(top = 16.dp, bottom = 16.dp)
      .padding(horizontal = 16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = "Filter by Body-parts",
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.headlineMedium,
      modifier = Modifier.fillMaxWidth()
    )
    for (muscleGroup in MuscleGroup.getAllMuscleGroups()) {
      Text(muscleGroup)
    }
  }
}