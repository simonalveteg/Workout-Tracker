package com.alveteg.simon.workouts.ui.session.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BottomSheetDetailsContainer(
  text: String = "",
  modifier: Modifier = Modifier,
  titleInset: Dp = 0.dp,
  content: @Composable () -> Unit
) {
  Column(
    modifier = modifier,
  ) {
    if (text.isNotEmpty()) {
      Text(
        text = text,
        style = MaterialTheme.typography.titleMediumEmphasized,
        modifier = Modifier.padding(horizontal = titleInset)
      )
    }
    content()
  }
}