package com.example.android.january2022.ui.exercisepicker

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuscleButton(
  muscle: String,
  selected: Boolean,
  onClick: () -> Unit
) {
  val containerColor by animateColorAsState(
    targetValue = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.23f)
  )

  Surface(
    onClick = onClick,
    color = containerColor,
    shape = MaterialTheme.shapes.medium,
    modifier = Modifier.padding(vertical = 2.dp, horizontal = 12.dp)
  ) {
    Text(
      text = muscle.uppercase(),
      modifier = Modifier.padding(8.dp).fillMaxWidth(),
      textAlign = TextAlign.Center
    )
  }
}