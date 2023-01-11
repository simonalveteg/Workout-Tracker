package com.example.android.january2022.ui.rework

import androidx.compose.foundation.layout.*
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
          FloatingActionButton(
            onClick = { /*TODO*/ },
            containerColor = MaterialTheme.colorScheme.primary
          ) {
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
      item {
        Box(
          modifier = Modifier.padding(start = 12.dp, top = 120.dp, bottom = 40.dp).fillMaxWidth()
        ) {
          Text(
            text = "Jan 9 2023",
            style = MaterialTheme.typography.headlineLarge
          )
        }
      }
      items(2) {
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