package com.alveteg.simon.workouts.ui.exercisepicker.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alveteg.simon.workouts.ui.session.components.BottomSheetDetailsContainer
import com.alveteg.simon.workouts.ui.theme.md_theme_dark_onPrimary

@Composable
fun FilterSection(
  modifier: Modifier = Modifier,
  title: String,
  filterOptions: List<String>,
  selectedFilterOptions: List<String>,
  onFilterClicked: (String) -> Unit
) {
  BottomSheetDetailsContainer(
    text = title,
    modifier = modifier
  ) {
    FlowRow(
      modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      filterOptions.forEach {
        FilterChip(
          selected = selectedFilterOptions.contains(it),
          label = {
            Text(
              text = it,
              style = MaterialTheme.typography.labelMedium
            )
          },
          onClick = { onFilterClicked(it) },
        )
      }
    }
  }
}