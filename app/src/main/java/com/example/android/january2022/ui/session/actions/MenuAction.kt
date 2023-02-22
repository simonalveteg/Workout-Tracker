package com.example.android.january2022.ui.session.actions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun MenuAction(
  onClick: () -> Unit
) {
  IconButton(onClick = onClick) {
    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Options")
  }
}