package com.alveteg.simon.workouts.ui.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HomeContainer(
  modifier: Modifier = Modifier,
  color: Color = MaterialTheme.colorScheme.surfaceContainer,
  onClick: () -> Unit,
  content: @Composable () -> Unit
) {
  Surface(
    onClick = { onClick() },
    color = color,
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp)
      .height(74.dp),
    shape = MaterialTheme.shapes.medium
  ) {
    content()
  }
}