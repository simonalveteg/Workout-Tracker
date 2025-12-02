package com.alveteg.simon.workouts.utils

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.core.text.isDigitsOnly

class IntegerInputTransformation : InputTransformation {
  override fun TextFieldBuffer.transformInput() {
    if (!asCharSequence().isDigitsOnly()) {
      revertAllChanges()
    }
  }
}

class FloatInputTransformation : InputTransformation {
  override fun TextFieldBuffer.transformInput() {
    val newText = asCharSequence().toString()
    // Allow empty string, otherwise check if it can be a valid float.
    // A valid float consists of digits and at most one decimal point.
    val isFloat = newText.count { it == '.' } <= 1 && newText.all { it.isDigit() || it == '.' }

    if (!isFloat && newText.isNotEmpty()) {
      revertAllChanges()
    }
  }
}
