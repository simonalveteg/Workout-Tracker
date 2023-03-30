package com.example.android.january2022.ui.session.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InputField(
  label: String,
  initialValue: String,
  onValueChange: (TextFieldValue) -> Boolean,
  keyboardActions: KeyboardActions,
  keyboardOptions: KeyboardOptions
) {
  val initialText = initialValue.toFloatOrNull().let {
    if (it == null || it < 0) "" else initialValue
  }
  val requester = remember { FocusRequester() }
  var textValidation by remember { mutableStateOf(true) }
  val selection = remember { mutableStateOf(TextRange(100)) }
  var textFieldValue by remember {
    val tfv = TextFieldValue(text = initialText, selection = selection.value)
    mutableStateOf(tfv)
  }
  val textColor by animateColorAsState(
    targetValue = if (textValidation) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.error
  )

  Row(
    modifier = Modifier
      .clickable {
        requester.requestFocus()
      }
      .height(40.dp)
      .defaultMinSize(minWidth = 60.dp)
      .padding(start = 10.dp, end = 20.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center
  ) {
    BasicTextField(
      value = textFieldValue,
      onValueChange = {
        textFieldValue = it
        textValidation = onValueChange(textFieldValue)
      },
      textStyle = TextStyle(
        color = textColor,
        fontSize = 21.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.End
      ),
      keyboardOptions = keyboardOptions,
      keyboardActions = keyboardActions,
      cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
      modifier = Modifier
        .width(48.dp)
        .focusRequester(requester)
        .onFocusChanged {
          // reset cursor position when receiving focus
          if (it.hasFocus || it.isFocused) {
            textFieldValue = TextFieldValue(text = textFieldValue.text, selection = selection.value)
          }
        }
    )
    InputLabel(text = label)
  }
}