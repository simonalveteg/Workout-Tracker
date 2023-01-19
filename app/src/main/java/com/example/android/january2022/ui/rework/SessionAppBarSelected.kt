package com.example.android.january2022.ui.rework

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.FileCopy
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SessionAppBarSelected() {
  BottomAppBar(
    actions = {
      Spacer(modifier = Modifier.width(4.dp))
      IconButton(onClick = { /*TODO*/ }) {
        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Options")
      }
      Spacer(modifier = Modifier.width(8.dp))
      IconButton(onClick = { /*TODO*/ }) {
        Icon(imageVector = Icons.Outlined.Timer, contentDescription = "Options")
      }
      Spacer(modifier = Modifier.width(8.dp))
      IconButton(onClick = { /*TODO*/ }) {
        Icon(imageVector = Icons.Outlined.ContentCopy, contentDescription = "Copy Exercise")
      }
    }
  )
}