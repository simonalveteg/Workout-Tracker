package com.alveteg.simon.workouts.ui.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsSection(
  title: String,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(bottom = 16.dp),
  ) {
    Text(
      text = title,
      modifier = Modifier.padding(bottom = 12.dp),
      style = MaterialTheme.typography.labelLarge,
      color = MaterialTheme.colorScheme.secondary
    )
    content()
  }
}
