package com.example.android.january2022.ui.rework

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.january2022.db.SetType
import com.example.android.january2022.db.entities.GymSet

@Composable
fun ExpandedExerciseContent(
  sets: List<GymSet>
) {
  Column(
    modifier = Modifier.fillMaxWidth(),
  ) {
    sets.forEach { set ->
      val localFocusManager = LocalFocusManager.current
      val reps: Int = set.reps
      val weight: Float = set.weight
      val requester = FocusRequester()
      var repsText by remember { mutableStateOf(reps.toString()) }
      var weightText by remember { mutableStateOf(weight.toString()) }

      LaunchedEffect(weightText) {
        try {
          val newWeight = weightText.trim().toFloat()
          //onEvent(SessionEvent.WeightChanged(set, newWeight))
        } catch (_: Exception) {
        }
      }
      LaunchedEffect(repsText) {
        try {
          val newReps = repsText.trim().toInt()
          //onEvent(SessionEvent.RepsChanged(set, newReps))
        } catch (_: Exception) {
        }
      }

      Row {
        Box(
          modifier = Modifier
            .width(50.dp)
            .height(25.dp)
            .background(setTypeColor(set.setType, MaterialTheme.colorScheme))
        ) {
          Text(text = set.setType)
        }
        BasicTextField(
          value = if (repsText != "-1") repsText else " ",
          onValueChange = {
            repsText = it
          },
          textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
          ),
          keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
          ),
          keyboardActions = KeyboardActions(
            onNext = { localFocusManager.moveFocus(FocusDirection.Right) }
          ),
          cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
          modifier = Modifier
            .width(IntrinsicSize.Min)
            .padding(start = 6.dp)
            .padding(horizontal = 8.dp)
            .focusRequester(requester)
        )
        Text(
          text = "reps",
          fontSize = 14.sp,
          color = LocalContentColor.current.copy(alpha = 0.9f)
        )
        BasicTextField(
          value = if (weightText != "-1.0") weightText else " ",
          onValueChange = {
            weightText = it
          },
          textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
          ),
          keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
          ),
          keyboardActions = KeyboardActions(
            onDone = {
              localFocusManager.moveFocus(FocusDirection.Next)
              localFocusManager.clearFocus()
              //onDone()
            }
          ),
          cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
          modifier = Modifier
            .width(IntrinsicSize.Min)
            .padding(start = 6.dp)
            .padding(horizontal = 8.dp)
        )
        Text(
          text = "kg", fontSize = 14.sp, color = LocalContentColor.current.copy(alpha = 0.9f)
        )
      }
    }
  }
}

fun setTypeColor(setType: String, colorScheme: ColorScheme): Color {
  return when (setType) {
    SetType.WARMUP -> Color.Gray
    SetType.EASY -> Color.Green
    SetType.NORMAL -> colorScheme.primary
    SetType.HARD -> Color.Red
    SetType.DROP -> Color.Magenta
    else -> Color.White
  }
}