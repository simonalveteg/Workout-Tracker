package com.example.android.january2022.ui.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.ui.SessionWrapper
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionCard(
  sessionWrapper: Pair<Session, List<String>>,
  onClick: () -> Unit
) {
  val session = sessionWrapper.first
  val muscleGroups = sessionWrapper.second
  val muscleTitle by remember {
    derivedStateOf {
      if (muscleGroups.isNotEmpty()) muscleGroups[0].uppercase() else ""
    }
  }
  val muscleSubtitle by remember {
    derivedStateOf {
      muscleGroups.drop(1).take(3).toString().drop(1).dropLast(1).uppercase()
    }
  }

  Surface(
    onClick = { onClick() },
    modifier = Modifier
      .fillMaxWidth()
      .padding(bottom = 8.dp)
      .requiredHeight(75.dp),
    shape = MaterialTheme.shapes.medium
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      SessionDate(session, Modifier.padding(start = 4.dp, end = 14.dp))
      Column(verticalArrangement = Arrangement.Center) {
        Text(
          text = muscleTitle,
          style = MaterialTheme.typography.headlineSmall
        )
        Row {
          if (muscleSubtitle.isNotEmpty()) {
            Text(
              text = muscleSubtitle,
              style = MaterialTheme.typography.bodySmall,
              color = LocalContentColor.current.copy(alpha = 0.7f)
            )
          }
        }
      }
    }
  }
}