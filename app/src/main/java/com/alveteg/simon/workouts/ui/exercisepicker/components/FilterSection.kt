package com.alveteg.simon.workouts.ui.exercisepicker.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alveteg.simon.workouts.ui.session.components.BottomSheetDetailsContainer
import com.alveteg.simon.workouts.ui.session.components.OutlinedSmallPill
import com.alveteg.simon.workouts.ui.session.components.SmallPill
import com.alveteg.simon.workouts.ui.theme.md_theme_dark_onPrimary

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FilterSection(
  modifier: Modifier = Modifier,
  title: String,
  filterOptions: List<String>,
  selectedFilterOptions: List<String>,
  onFilterClicked: (String) -> Unit
) {
  Column() {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(4.dp),
      modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
    ) {
      HorizontalDivider(modifier = Modifier.weight(1f))
      Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.labelLargeEmphasized
      )
      HorizontalDivider(modifier = Modifier.weight(1f))
    }
    FlowRow(
      modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
      horizontalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.CenterHorizontally)
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