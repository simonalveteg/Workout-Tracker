package com.example.android.january2022.ui.rework

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.january2022.utils.UiEvent
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen(
  onNavigate: (UiEvent.Navigate) -> Unit,
) {

  // TODO: Change to be dependent on SessionExerciseId
  val expanded = remember {
    mutableStateOf(true)
  }

  Scaffold(
    bottomBar = {
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
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
    ) {
      item {
        Box(
          modifier = Modifier
            .padding(
              start = paddingValues.calculateTopPadding() + 12.dp,
              top = 120.dp,
              bottom = 40.dp
            )
            .fillMaxWidth()
        ) {
          Text(
            text = "Jan 9 2023",
            style = MaterialTheme.typography.headlineLarge
          )
        }
      }
      items(2) {
        ExerciseCard(
          expanded = expanded.value
        ) {
          expanded.value = !expanded.value
        }
      }
      item {
        Spacer(modifier = Modifier.height(paddingValues.calculateBottomPadding()))
      }
    }
  }
}