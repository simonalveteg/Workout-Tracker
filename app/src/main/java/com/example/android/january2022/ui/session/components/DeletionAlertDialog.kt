package com.example.android.january2022.ui.session.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DeletionAlertDialog(
  onDismiss: () -> Unit,
  onDelete: () -> Unit,
  title: @Composable () -> Unit,
  text: @Composable () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    confirmButton = {
      Button(onClick = onDelete) {
        Text(text = "Delete")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text(text = "Cancel")
      }
    },
    title = title,
    text = text
  )
}