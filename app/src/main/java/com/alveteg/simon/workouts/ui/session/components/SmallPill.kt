package com.alveteg.simon.workouts.ui.session.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alveteg.simon.workouts.ui.theme.Shapes

@Composable
fun SmallPill(text: String, modifier: Modifier = Modifier) {
  Surface(
    shape = MaterialTheme.shapes.small,
    color = MaterialTheme.colorScheme.secondaryContainer,
    modifier = modifier
  ) {
    Text(
      text = text.uppercase(),
      style = MaterialTheme.typography.labelSmall,
      modifier = Modifier.padding(4.dp)
    )
  }
}