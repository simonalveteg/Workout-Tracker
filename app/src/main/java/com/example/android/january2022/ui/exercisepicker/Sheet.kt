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

@Composable
fun Sheet(
  items: List<String>,
  selectedItems: List<String>,
  onSelect: (String) -> Unit,
  onDeselectAll: () -> Unit
) {
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
      items(items) { item ->
        MuscleButton(
          muscle = item,
          selected = selectedItems.contains(item)
        ) { onSelect(item) }
      }
    }
    TextButton(onClick = { onDeselectAll() }) {
      Text(
        text = "Deselect All".uppercase(),
        style = MaterialTheme.typography.labelLarge
      )
    }
  }
}