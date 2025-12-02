package com.alveteg.simon.workouts.ui.session.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alveteg.simon.workouts.ui.ExerciseWrapper
import com.alveteg.simon.workouts.ui.SessionWrapper
import com.alveteg.simon.workouts.ui.home.components.SessionDate
import java.time.LocalDate

@Composable
fun SetHistoryCard(
  modifier: Modifier = Modifier,
  sessionWrapper: SessionWrapper,
  exerciseWrapper: ExerciseWrapper,
) {
  val sets = exerciseWrapper.sets
  Surface(
    color = MaterialTheme.colorScheme.surfaceContainerHigh,
    modifier = modifier,
    shape = MaterialTheme.shapes.medium,
    border = BorderStroke(
      width = 1.dp,
      color = MaterialTheme.colorScheme.outlineVariant
    )
  ) {
    Row(
      modifier = Modifier
        .padding(horizontal = 8.dp, vertical = 2.dp)
        .height(IntrinsicSize.Min),
      verticalAlignment = Alignment.CenterVertically
    ) {
      SessionDate(sessionWrapper.session)
      sets.forEach { set ->
        SetCard(set = set)
      }
      if (sessionWrapper.session.start.year != LocalDate.now().year) {
        VerticalDivider()
        Text(
          text = sessionWrapper.session.start.year.toString().toList().joinToString("\n"),
          style = MaterialTheme.typography.bodySmall.copy(lineHeight = 12.sp),
          color = LocalContentColor.current.copy(alpha = 0.7f),
          modifier = Modifier
            .padding(start = 8.dp)
        )
      }
    }
  }
}