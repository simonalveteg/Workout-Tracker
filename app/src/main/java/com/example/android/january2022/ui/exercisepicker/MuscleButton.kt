package com.example.android.january2022.ui.exercisepicker

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MuscleButton(
  muscle: String,
  selected: Boolean,
  onClick: () -> Unit
) {
  
  val contentColor by animateColorAsState(
    targetValue = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
  )

  val containerColor by animateColorAsState(
    targetValue = if (selected) MaterialTheme.colorScheme.onSurfaceVariant else Color.Transparent
  )
  
  OutlinedButton(
    onClick = onClick,
    colors = ButtonDefaults.buttonColors(
      contentColor = contentColor,
      containerColor = containerColor
    ),
    contentPadding = PaddingValues(0.dp),
    modifier = Modifier.width(150.dp),
    shape = CutCornerShape(0.dp)
  ) {
    Text(text = muscle.uppercase())
  }
}