package com.alveteg.simon.workouts.ui.session.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ElevatedAssistChip(
  onClick: () -> Unit,
  label: @Composable () -> Unit,
  enabled: Boolean = true,
  leadingIcon: ImageVector,
  leadingIconDescription: String,
  modifier: Modifier = Modifier
) {
  ElevatedAssistChip(
    modifier = modifier,
    onClick = onClick,
    enabled = enabled,
    leadingIcon = {
      Icon(
        imageVector = leadingIcon,
        contentDescription = leadingIconDescription,
        Modifier.size(AssistChipDefaults.IconSize)
      )
    },
    label = label,
    colors = AssistChipDefaults.elevatedAssistChipColors(
      containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
      labelColor = MaterialTheme.colorScheme.onSurface,
      leadingIconContentColor = MaterialTheme.colorScheme.onSurface,
      trailingIconContentColor = MaterialTheme.colorScheme.onSurface,
    )
  )
}