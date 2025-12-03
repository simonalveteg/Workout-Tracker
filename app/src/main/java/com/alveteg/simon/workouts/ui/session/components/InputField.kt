package com.alveteg.simon.workouts.ui.session.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun InputField(
  textFieldState: TextFieldState,
  isValid: Boolean,
  inputTransformation: InputTransformation? = null,
  outputTransformation: OutputTransformation? = null,
  imeAction: ImeAction,
  labelText: String,
  modifier: Modifier = Modifier
) {
  OutlinedTextField(
    state = textFieldState,
    inputTransformation = inputTransformation,
    outputTransformation = outputTransformation,
    keyboardOptions = KeyboardOptions(
      keyboardType = KeyboardType.Number,
      imeAction = imeAction
    ),
    lineLimits = TextFieldLineLimits.SingleLine,
    isError = isValid,
    supportingText = {
      if (isValid) {
        Text(text = "Invalid input")
      }
    },
    label = {
      Text(text = labelText)
    },
    modifier = modifier
  )
}