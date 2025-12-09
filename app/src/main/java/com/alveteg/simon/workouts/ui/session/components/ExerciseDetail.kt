package com.alveteg.simon.workouts.ui.session.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExerciseDetail(
  modifier: Modifier = Modifier,
  text: String,
  icon: ImageVector
) {
  Row(
    modifier = modifier
      .padding(vertical = 12.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(6.dp)
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null
    )
    Text(
      text = text,
      style = MaterialTheme.typography.titleMediumEmphasized,
    )
  }

}