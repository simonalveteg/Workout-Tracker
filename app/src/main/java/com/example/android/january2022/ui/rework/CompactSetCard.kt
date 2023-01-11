package com.example.android.january2022.ui.rework

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
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


@Composable
fun CompactSetCard(
  reps: Int,
  weight: Float
) {
  val repsText by remember { derivedStateOf { if (reps > -1) reps.toString() else "0" } }
  val weightText by remember { derivedStateOf { if (weight > -1) weight.toString() else "0" } }
  Row(
    Modifier
      .padding(horizontal = 8.dp, vertical = 4.dp)
      .requiredHeight(48.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center
  ) {
    Surface(color = MaterialTheme.colorScheme.primary) {
      Box(
        Modifier
          .height(34.dp)
          .width(2.dp)
      )
    }
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