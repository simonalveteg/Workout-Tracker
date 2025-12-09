package com.alveteg.simon.workouts.ui.session.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MuscleList(
  modifier: Modifier = Modifier,
  label: String,
  items: List<String>
) {
  BottomSheetDetailsContainer(
    text = label,
    modifier = modifier,
    titleInset = 16.dp
  ) {
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(4.dp),
      contentPadding = PaddingValues(horizontal = 8.dp),
      modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
    ) {
      items(items) {
        SmallPill(it)
      }
    }
  }
}