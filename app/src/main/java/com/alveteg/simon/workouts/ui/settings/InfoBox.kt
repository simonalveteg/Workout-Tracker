package com.alveteg.simon.workouts.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InfoBox(text: String, modifier: Modifier = Modifier) {
  Column(
    modifier = modifier.padding(top = 16.dp, bottom = 8.dp, end = 32.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
  Icon(
    imageVector = Icons.Outlined.Info,
    contentDescription = null,
    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
  )
  Text(
    text = text,
    style = MaterialTheme.typography.bodySmall,
    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
  )
  }
}