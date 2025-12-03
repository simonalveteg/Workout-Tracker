package com.alveteg.simon.workouts.ui.session.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alveteg.simon.workouts.db.entities.GymSet
import com.alveteg.simon.workouts.db.entities.Rpe
import com.alveteg.simon.workouts.ui.theme.ArchivoBlack


@Composable
fun SetCard(
  set: GymSet,
  modifier: Modifier = Modifier,
  isClickable: Boolean = true,
  onClick: (GymSet) -> Unit = {}
) {
  val reps = set.reps
  val weight = set.weight
  val repsText = remember(set.reps) { reps?.toString() ?: "0" }
  val weightText = remember(set.weight) {
    weight?.let {
      if (it % 1 == 0f) {
        it.toInt().toString()
      } else {
        it.toString()
      }
    } ?: "0"
  }

  val shape = MaterialTheme.shapes.medium

  Surface(
    modifier = modifier
      .clip(shape)
      .then(if (isClickable) Modifier.clickable { onClick(set) } else Modifier),
    color = Color.Transparent,
  ) {
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
}

@Composable
fun SetIndicator(set: GymSet, modifier: Modifier = Modifier) {
  val color = getSetColorFromRPE(set.rpe, MaterialTheme.colorScheme)
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = set.rpe?.value?.toString() ?: "",
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
  Row(modifier = modifier) {
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

fun getSetColorFromRPE(setRpe: Rpe?, colorScheme: ColorScheme): Color {
  val veryLowExhaustion = Color(0xFF7A7272)
  val lowExhaustion = Color(0xFF6A9E44)
  val mediumExhaustion = colorScheme.primary
  val highExhaustion = Color(0xFFB84733)

  return when (setRpe) {
    Rpe.Level1 -> veryLowExhaustion
    Rpe.Level2 -> veryLowExhaustion
    Rpe.Level3 -> veryLowExhaustion
    Rpe.Level4 -> veryLowExhaustion
    Rpe.Level5 -> lowExhaustion
    Rpe.Level6 -> lowExhaustion
    Rpe.Level7 -> mediumExhaustion
    Rpe.Level8 -> mediumExhaustion
    Rpe.Level9 -> highExhaustion
    Rpe.Level10 -> highExhaustion
    else -> colorScheme.primary
  }
}