package com.alveteg.simon.workouts.ui.settings.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> SegmentedInput(
  label: String,
  description: String,
  options: List<T>,
  selectedOption: T,
  onOptionSelect: (T) -> Unit,
  labelProvider: (T) -> String,
  modifier: Modifier = Modifier,
) {
  GenericInput(
    label = label,
    description = description,
    modifier = modifier
  ) {
    SingleChoiceSegmentedButtonRow(
      modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 16.dp)
    ) {
      options.forEachIndexed { index, option ->
        SegmentedButton(
          shape = SegmentedButtonDefaults.itemShape(
            index = index,
            count = options.size
          ),
          onClick = { onOptionSelect(option) },
          selected = option == selectedOption,
          label = { Text(labelProvider(option)) }
        )
      }
    }
  }
}