package com.example.android.january2022.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.android.january2022.db.entities.Session
import java.time.format.TextStyle
import java.util.*

@Composable
fun SessionDate(
  session: Session,
  modifier: Modifier = Modifier
) {
  val startMonth by remember {
    derivedStateOf {
      session.start.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
    }
  }
  val startDay by remember { derivedStateOf { session.start.dayOfMonth.toString() } }

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier
  ) {
    Text(text = startMonth, style = MaterialTheme.typography.bodyMedium)
    Text(text = startDay, style = MaterialTheme.typography.bodyMedium)
  }
}