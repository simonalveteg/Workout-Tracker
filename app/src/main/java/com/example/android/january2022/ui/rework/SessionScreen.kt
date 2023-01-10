package com.example.android.january2022.ui.rework

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.january2022.utils.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen(
  onNavigate: (UiEvent.Navigate) -> Unit,
) {

  Scaffold(
    bottomBar = {
      BottomAppBar(
        actions = {},
        floatingActionButton = {
          FloatingActionButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Exercise")
          }
        }
      )
    }
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .padding(paddingValues = paddingValues)
        .fillMaxSize()
    ) {
      items(10) {
        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(vertical = 4.dp, horizontal = 8.dp),
          tonalElevation = 1.dp,
          shape = MaterialTheme.shapes.medium
        ) {
          Text(text = "This is an exercise")
        }
      }
    }
  }

}