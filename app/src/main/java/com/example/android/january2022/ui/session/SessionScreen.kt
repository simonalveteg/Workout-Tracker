package com.example.android.january2022.ui.session

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.ui.rework.MainViewModel
import com.example.android.january2022.utils.UiEvent
import kotlinx.coroutines.launch
import timber.log.Timber
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
    Timber.d(session.toString())
  }

  val scrollState = rememberLazyListState()
  val headerHeight = 240.dp
  val coroutineScope = rememberCoroutineScope()

  Scaffold(
    bottomBar = {
      Box {
        BottomAppBar {}
        AnimatedVisibility(
          visible = uiState.value.selectedExercise == null,
          exit = fadeOut(tween(900)),
          enter = fadeIn(tween(900))
        ) {
          SessionAppBar()
        }
        AnimatedVisibility(
          visible = uiState.value.selectedExercise != null,
          exit = fadeOut(tween(900)),
          enter = fadeIn(tween(900))
        ) {
          SessionAppBarSelected()
        }
      }
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
        itemsIndexed(
          items = exercises.value,
          key = { _, exercise ->
            exercise.sessionExercise.sessionExerciseId
          }
        ) { index, exercise ->
          val expanded =
            exercise.sessionExercise.sessionExerciseId == selectedExercise?.sessionExerciseId
          ExerciseCard(
            exerciseWrapper = exercise,
            expanded = expanded,
            onEvent = viewModel::onEvent
          ) {
            viewModel.onEvent(SessionEvent.ExerciseSelection(exercise))
            if (!expanded) {
              coroutineScope.launch {
                scrollState.animateScrollToItem(index = (index-2).coerceAtLeast(0))
              }
            }
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