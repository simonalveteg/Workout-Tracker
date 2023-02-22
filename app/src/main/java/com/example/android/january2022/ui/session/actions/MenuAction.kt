package com.example.android.january2022.ui.session.actions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun MenuAction(
  onDelete: () -> Unit
) {
  val expanded = remember { mutableStateOf(false) }

  Column {
    Box {
      DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
        DropdownMenuItem(text = { Text(text = "Delete Session") }, onClick = onDelete )
      }
    }
    IconButton(onClick = { expanded.value = true }) {
      Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Options")
    }
  }
}