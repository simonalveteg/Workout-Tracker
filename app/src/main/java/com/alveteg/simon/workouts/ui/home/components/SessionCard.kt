package com.alveteg.simon.workouts.ui.home.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alveteg.simon.workouts.db.MuscleGroup
import com.alveteg.simon.workouts.db.entities.Session
import com.alveteg.simon.workouts.ui.SessionWrapper
import com.alveteg.simon.workouts.ui.theme.ArchivoBlack
import java.time.LocalDate

@Composable
fun SessionCard(
  sessionWrapper: SessionWrapper,
  modifier: Modifier = Modifier,
  dateModifier: Modifier = Modifier,
  titleModifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  val session = sessionWrapper.session
  val muscleGroups = sessionWrapper.muscleGroups
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
  val color by animateColorAsState(targetValue =
    if (session.end == null) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainer
  )

  HomeContainer(
    onClick = { onClick() },
    modifier = modifier,
    color = color
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        SessionDate(session, Modifier
          .padding(start = 20.dp, end = 16.dp)
          .then(dateModifier)
        )
        Column(
          modifier = Modifier.padding(bottom = 2.dp),
          verticalArrangement = Arrangement.Center
        ) {
          Text(
            text = muscleTitle,
            style = MaterialTheme.typography.headlineSmall.copy(fontFamily = ArchivoBlack),
            modifier = titleModifier
          )
          if (muscleSubtitle.isNotEmpty()) {
            Text(
              text = muscleSubtitle,
              style = MaterialTheme.typography.bodySmall,
              color = LocalContentColor.current.copy(alpha = 0.7f),
            )
          }
        }
      }
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        if (session.start.year != LocalDate.now().year) {
          VerticalDivider()
          Text(
            text = session.start.year.toString().toList().joinToString("\n"),
            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 12.sp),
            color = LocalContentColor.current.copy(alpha = 0.7f),
            modifier = Modifier
              .padding(horizontal = 8.dp)
          )
        }
      }
    }
  }
}

@Preview
@Composable
fun SessionCardPreview() {
    SessionCard(
        SessionWrapper(
            session = Session(),
            muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.HIPS, MuscleGroup.CALVES,
                MuscleGroup.CHEST, MuscleGroup.HAMSTRINGS)
        ),
        onClick = {}
    )
}