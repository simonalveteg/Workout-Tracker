package com.example.android.january2022.ui.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.january2022.utils.Event

@Composable
fun HomeAppBar(
  onEvent: (Event) -> Unit
) {
  BottomAppBar(
    actions = {
      Spacer(modifier = Modifier.width(4.dp))
      IconButton(onClick = { /*TODO*/ }) {
        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Options")
      }
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = { onEvent(HomeEvent.NewSession) },
        containerColor = MaterialTheme.colorScheme.primary
      ) {
        Icon(Icons.Default.Add, "Add Session")
      }
    }
  )
}