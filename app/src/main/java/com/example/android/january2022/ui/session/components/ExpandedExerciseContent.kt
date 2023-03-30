package com.example.android.january2022.ui.session.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.january2022.db.SetType
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.ui.session.SessionEvent
import com.example.android.january2022.utils.Event

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandedExerciseContent(
  sets: List<GymSet>,
  onEvent: (Event) -> Unit,
  onSetDeleted: (GymSet) -> Unit,
  onSetCreated: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(top = 8.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    sets.forEach { set ->
      val localFocusManager = LocalFocusManager.current
      val reps: Int = set.reps
      val weight: Float = set.weight

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
          .padding(bottom = 4.dp, start = 6.dp, end = 8.dp)
          .fillMaxWidth()
          .clickable { }
      ) {
        IconButton(
          onClick = { onSetDeleted(set) },
          modifier = Modifier.padding(end = 8.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Delete Set",
            tint = LocalContentColor.current.copy(alpha = 0.75f)
          )
        }
        InputField(
          label = "reps",
          initialValue = reps.toString(),
          onValueChange = {
            val tfv = it.text.trim().toIntOrNull()
            if (tfv != null) {
              onEvent(SessionEvent.SetChanged(set.copy(reps = tfv)))
              true
            } else {
              false
            }
          },
          keyboardActions = KeyboardActions(
            onNext = { localFocusManager.moveFocus(FocusDirection.Next) }
          ),
          keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
          )
        )
        InputField(
          label = "kg",
          initialValue = weight.toString(),
          onValueChange = {
            val tfv = it.text.trim().toFloatOrNull()
            if (tfv != null) {
              onEvent(SessionEvent.SetChanged(set.copy(weight = tfv)))
              true
            } else {
              false
            }
          },
          keyboardActions = KeyboardActions(
            onDone = {
              localFocusManager.moveFocus(FocusDirection.Next)
              localFocusManager.clearFocus()
            }
          ),
          keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
          )
        )
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
