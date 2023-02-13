package com.example.android.january2022.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.january2022.ui.rework.SessionWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionCard(
  sessionWrapper: SessionWrapper,
  onClick: () -> Unit
) {

  val session = sessionWrapper.session
  val muscleGroups by sessionWrapper.muscleGroups.collectAsState(initial = emptyList())

  Surface(
    onClick = { onClick() },
    modifier = Modifier
      .fillMaxWidth().padding(bottom = 8.dp).requiredHeight(75.dp),
    shape = MaterialTheme.shapes.medium
  ) {
    Row(
      modifier = Modifier.fillMaxWidth().padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      SessionDate(session, Modifier.padding(end = 16.dp))
      Column(verticalArrangement = Arrangement.Center) {
        Text(
          text = if (muscleGroups.isNotEmpty()) muscleGroups[0].uppercase() else "",
          style = MaterialTheme.typography.headlineSmall
        )
        Row {
          muscleGroups.forEachIndexed { index, string ->
            if (index in 1..3) {
              var newString = string
              if (index in 1..2 && muscleGroups.size >= index + 2) {
                newString = "$string, "
              }
              Text(
                text = newString.uppercase(),
                style = MaterialTheme.typography.bodySmall,
                color = LocalContentColor.current.copy(alpha = 0.7f)
              )
            }
          }
        }
      }
    }
  }
}