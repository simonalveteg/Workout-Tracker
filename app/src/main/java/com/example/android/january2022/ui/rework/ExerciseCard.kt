package com.example.android.january2022.ui.rework

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseCard(
  expanded: Boolean = false,
  onClick: () -> Unit
) {
  val height by animateDpAsState(targetValue = if(expanded) 500.dp else 120.dp)

  Surface(
    onClick = {
      onClick()
    },
    modifier = Modifier
      .fillMaxWidth()
      .height(height)
      .padding(vertical = 8.dp, horizontal = 8.dp),
    tonalElevation = 2.dp,
    shape = MaterialTheme.shapes.medium
  ) {
    Column(Modifier.padding(vertical = 12.dp, horizontal = 12.dp)) {
      Text(
        text = "Lever Front Pulldown",
        style = MaterialTheme.typography.titleMedium
      )
      Spacer(Modifier.height(4.dp))
      LazyRow {
        items(3) {
          CompactSetCard(reps = 12, weight = 45f)
        }
      }
    }
  }
}