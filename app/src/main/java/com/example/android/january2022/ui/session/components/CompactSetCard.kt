package com.example.android.january2022.ui.session.components

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
import com.example.android.january2022.db.entities.GymSet


@Composable
fun CompactSetCard(
  set: GymSet
) {
  val reps = set.reps
  val weight = set.weight
  val repsText by remember { derivedStateOf { if (reps > -1) reps.toString() else "0" } }
  val weightText by remember { derivedStateOf { if (weight > -1) weight.toString() else "0" } }

  Row(
    Modifier
      .padding(horizontal = 8.dp, vertical = 4.dp)
      .requiredHeight(48.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center
  ) {
    Surface(
      modifier = Modifier
        .fillMaxHeight(0.7f)
        .padding(top = 1.dp)
        .width(2.dp),
      color = setTypeColor(set.setType, MaterialTheme.colorScheme)
    ) {}
    Column(Modifier.padding(start = 4.dp)) {
      Row {
        Text(text = repsText, fontWeight = FontWeight.Bold)
        Text(
          text = "reps",
          fontSize = 10.sp,
          color = LocalContentColor.current.copy(alpha = 0.85f)
        )
      }
      Row {
        Text(text = weightText, fontWeight = FontWeight.Bold)
        Text(
          text = "kg", fontSize = 10.sp, color = LocalContentColor.current.copy(alpha = 0.85f)
        )
      }
    }
  }
}