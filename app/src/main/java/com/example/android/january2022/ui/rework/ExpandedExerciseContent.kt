package com.example.android.january2022.ui.rework

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.example.android.january2022.utils.Event

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandedExerciseContent(
  sets: List<GymSet>,
  onEvent: (Event) -> Unit
) {
  Column(
    modifier = Modifier.fillMaxWidth(),
  ) {
    sets.forEachIndexed { index, set ->
      val localFocusManager = LocalFocusManager.current
      val reps: Int = set.reps
      val weight: Float = set.weight
      val requester = FocusRequester()
      var repsText by remember { mutableStateOf(reps.toString()) }
      var weightText by remember { mutableStateOf(weight.toString()) }

      LaunchedEffect(weightText) {
        try {
          val newWeight = weightText.trim().toFloat()
          onEvent(SessionEvent.SetChanged(set.copy(weight = newWeight)))
        } catch (_: Exception) {
        }
      }
      LaunchedEffect(repsText) {
        try {
          val newReps = repsText.trim().toInt()
          onEvent(SessionEvent.SetChanged(set.copy(reps = newReps)))
        } catch (_: Exception) {
        }
      }

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 4.dp, start = 4.dp, end = 8.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          ColumnHeader(text = "SET${index + 1}")
          Surface(
            onClick = {
              onEvent(SessionEvent.SetChanged(set.copy(setType = SetType.next(set.setType))))
            },
            color = setTypeColor(set.setType, MaterialTheme.colorScheme),
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
              .defaultMinSize(minWidth = 100.dp)
              .padding(start = 12.dp)
          ) {
            Text(
              text = set.setType,
              modifier = Modifier.padding(6.dp),
              textAlign = TextAlign.Center
            )
          }
        }
        Row {
          Row {
            BasicTextField(
              value = if (repsText != "-1") repsText else " ",
              onValueChange = {
                repsText = it
              },
              textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 21.sp,
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
                .focusRequester(requester)
            )
            SetInputLabel(text = "reps")
          }
          Row {
            BasicTextField(
              value = if (weightText != "-1.0") weightText else " ",
              onValueChange = {
                weightText = it
              },
              textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 21.sp,
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
                .padding(start = 8.dp)
            )
            SetInputLabel(text = "kg")
          }
        }
      }
    }
  }
}

@Composable
fun ColumnHeader(text: String) {
  Box {
    Text(text = text, style = MaterialTheme.typography.labelMedium)
    Text(
      text = "SET000",
      style = MaterialTheme.typography.labelMedium,
      modifier = Modifier.alpha(0f)
    )
  }
}

@Composable
fun SetInputLabel(text: String) {
  Text(
    text = text,
    color = LocalContentColor.current.copy(alpha = 0.8f),
    style = MaterialTheme.typography.labelSmall
  )
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
