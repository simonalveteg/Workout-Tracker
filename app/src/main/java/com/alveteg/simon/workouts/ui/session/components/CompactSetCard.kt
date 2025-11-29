package com.alveteg.simon.workouts.ui.session.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alveteg.simon.workouts.db.entities.GymSet
import com.alveteg.simon.workouts.ui.theme.ArchivoBlack


@Composable
fun CompactSetCard(
  set: GymSet
) {
  val reps = set.reps
  val weight = set.weight
  val repsText by remember { derivedStateOf { reps?.toString() ?: "0" } }
  val weightText by remember {
    derivedStateOf {
      weight?.let {
        if (it % 1 == 0f) {
          it.toInt().toString()
        } else {
          it.toString()
        }
      } ?: "0"
    }
  }

  Row(
    Modifier
      .padding(horizontal = 8.dp, vertical = 4.dp)
      .requiredHeight(48.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center
  ) {
    SetIndicator(set)
    Column(Modifier.padding(start = 4.dp)) {
      SetText(repsText, "reps")
      Spacer(modifier = Modifier.height(4.dp))
      SetText(weightText, "kg")
    }
  }
}

@Composable
fun SetIndicator(set: GymSet, modifier: Modifier = Modifier) {
  val color = setTypeColor(set.setType, MaterialTheme.colorScheme)
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = "4",
      style = MaterialTheme.typography.labelSmall.copy(fontFamily = ArchivoBlack),
      color = color,
      modifier = Modifier.padding(end = 2.dp)
    )
    Surface(
      modifier = Modifier
        .fillMaxHeight(0.7f)
        .padding(top = 1.dp)
        .width(2.dp),
      color = color
    ) {}
  }
}

@Composable
fun SetText(
  text: String,
  textType: String,
  modifier: Modifier = Modifier
) {
  Row {
    Text(
      text = text,
      style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp),
    )
    Text(
      text = textType,
      style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
      color = LocalContentColor.current.copy(alpha = 0.85f),
      modifier = Modifier.padding(start = 2.dp)
    )
  }
}