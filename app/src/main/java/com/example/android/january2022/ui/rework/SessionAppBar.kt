package com.example.android.january2022.ui.rework

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SessionAppBar() {
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
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = { /*TODO*/ },
        containerColor = MaterialTheme.colorScheme.primary
      ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Exercise")
      }
    }
  )
}