package com.example.android.january2022.ui.rework

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.utils.UiEvent
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen(
  onNavigate: (UiEvent.Navigate) -> Unit,
  viewModel: MainViewModel = hiltViewModel()
) {

  val uiState = viewModel.uiState.collectAsState()
  val session = uiState.value.currentSession
  val exercises = session.exercises.collectAsState(initial = emptyList())
  val selectedExercise = uiState.value.selectedExercise

  LaunchedEffect(true) {
    Log.d("SessionScreen", session.toString())
  }

  val scrollState = rememberLazyListState()
  val headerHeight = 240.dp

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
    Box {
      SessionHeader(
        sessionWrapper = session,
        muscleGroups = listOf("One", "Two", "Three"),
        scrollState = scrollState,
        height = headerHeight,
        topPadding = paddingValues.calculateTopPadding()
      )
      LazyColumn(
        modifier = Modifier
          .fillMaxSize(),
        state = scrollState
      ) {
        item {
          Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding() + headerHeight))
        }
        items(exercises.value) { exercise ->
          ExerciseCard(
            exerciseWrapper = exercise,
            expanded = exercise.sessionExercise.sessionExerciseId == selectedExercise.sessionExerciseId
          ) {
            viewModel.onEvent(SessionEvent.ExerciseSelection(exercise))
          }
        }
        item {
          Spacer(modifier = Modifier.height(paddingValues.calculateBottomPadding()))
        }
      }
    }
  }
}

fun Session.toSessionTitle(): String {
  return try {
    DateTimeFormatter.ofPattern("MMM d yyyy").format(this.start)
  } catch (e: Exception) {
    "no date"
  }
}