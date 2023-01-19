package com.example.android.january2022.ui.rework

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.android.january2022.db.entities.GymSet

@Composable
fun ExpandedExerciseContent(
  sets: List<GymSet>
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      ColumnHeader(text = "difficulty")
      sets.forEach { set ->
        Row {
          for(i in 0..5) {
            val selected = i == set.mood
            val width = if (selected) 50.dp else 25.dp
            Box(
              modifier = Modifier
                .width(width)
                .height(25.dp)
                .moodColor(i+1)
            ) {
              Text(text = if (selected) "this" else set.mood.toString())
            }
          }
        }
      }
    }
    Column() {
      ColumnHeader(text = "reps")
    }
    Column() {
      ColumnHeader(text = "weight")
    }
  }
}

@Composable
fun ColumnHeader(text: String) {
  Text(
    text = text,
    style = MaterialTheme.typography.labelMedium,
    textAlign = TextAlign.Center,
  )
}

fun Modifier.moodColor(mood: Int): Modifier {
  val color = when (mood) {
    1 -> Color.Gray
    2 -> Color.Green
    3 -> Color.Yellow
    4 -> Color.Red
    5 -> Color.Magenta
    else -> Color.White
  }
  return background(color)
}