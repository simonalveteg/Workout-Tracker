package com.example.android.january2022.ui.session

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
  onEvent: (Event) -> Unit,
  onSetCreated: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(top = 8.dp),
    horizontalAlignment = Alignment.CenterHorizontally
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
          .clickable {  }
      ) {
        ColumnHeader(text = "SET${index + 1}")
        Row {
          BasicTextField(
            value = if (repsText != "-1") repsText else " ",
            onValueChange = {
              repsText = it
            },
            textStyle = TextStyle(
              color = MaterialTheme.colorScheme.onSurface,
              fontSize = 21.sp,
              fontWeight = FontWeight.Bold,
              textAlign = TextAlign.End
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
              .defaultMinSize(minWidth = 60.dp)
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
              fontWeight = FontWeight.Bold,
              textAlign = TextAlign.End
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
              .defaultMinSize(minWidth = 60.dp)
              .padding(start = 8.dp)
          )
          SetInputLabel(text = "kg")
        }
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
    }
    IconButton(
      onClick = { onSetCreated() }
    ) {
      Icon(imageVector = Icons.Default.Add, contentDescription = "Add new set")
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
    style = MaterialTheme.typography.labelSmall,
    modifier = Modifier.padding(start = 4.dp)
  )
}

fun setTypeColor(setType: String, colorScheme: ColorScheme): Color {
  return when (setType) {
    SetType.WARMUP -> Color(0xFF7A7272)
    SetType.EASY -> Color(0xFF6A9E44)
    SetType.NORMAL -> colorScheme.primary
    SetType.HARD -> Color(0xFFB84733)
    SetType.DROP -> Color(0xFFAD49A8)
    else -> Color.White
  }
}
