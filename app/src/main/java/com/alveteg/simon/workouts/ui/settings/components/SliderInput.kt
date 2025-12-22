package com.alveteg.simon.workouts.ui.settings.components

import androidx.annotation.IntRange
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun SliderInput(
  label: String,
  description: String,
  onValueChange: (Float) -> Unit,
  value: Float,
  roundToInt: Boolean = false,
  valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
  @IntRange(from = 0) steps: Int = 0,
  modifier: Modifier = Modifier
) {
  val numberText = if (roundToInt) value.roundToInt().toString() else "%.1f".format(value)

  GenericInput(
    label = label,
    description = description,
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
      modifier = modifier.fillMaxWidth()
    ) {
      Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = valueRange,
        steps = steps,
        modifier = Modifier
          .padding(vertical = 16.dp)
          .weight(1f)
      )
      Text(
        text = numberText,
        modifier = Modifier.requiredWidth(36.dp),
        textAlign = TextAlign.Center
      )
    }
  }
}