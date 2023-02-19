package com.example.android.january2022.ui.session

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
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

  val uiState = viewModel.sessionState.collectAsState()
  val session = uiState.value.currentSession
  val exercises = session.exercises.collectAsState(initial = emptyList())
  val selectedExercise = uiState.value.selectedExercise
  val muscleGroups by uiState.value.currentSession.muscleGroups.collectAsState(initial = emptyList())
  val timerState by viewModel.timerState.collectAsState()

  val uriHandler = LocalUriHandler.current

  LaunchedEffect(true) {
    Timber.d(session.toString())
    viewModel.uiEvent.collect { event ->
      when (event) {
        is UiEvent.OpenWebsite -> {
          uriHandler.openUri(event.url)
        }
        is UiEvent.Navigate -> onNavigate(event)
        else -> Unit
      }
    }
  }

  val scrollState = rememberLazyListState()
  val headerHeight = 240.dp
  val coroutineScope = rememberCoroutineScope()
  val timerVisible = remember { mutableStateOf(false) }

  Scaffold(
    bottomBar = {
      Box(
        contentAlignment = Alignment.BottomCenter
      ) {
        BottomAppBar {}
        AnimatedVisibility(
          visible = timerVisible.value,
          exit = slideOutVertically(
            animationSpec = tween(250),
            targetOffsetY = { height ->
              height / 2
            }
          ),
          enter = slideInVertically(
            animationSpec = tween(250),
            initialOffsetY = { height ->
              height / 2
            }
          )
        ) {
          Column {
            TimerBar(timerState, viewModel::onEvent)
            BottomAppBar {}
          }
        }
        AnimatedVisibility(
          visible = uiState.value.selectedExercise == null,
          exit = fadeOut(tween(900)),
          enter = fadeIn(tween(900))
        ) {
          SessionAppBar(
            onEvent = viewModel::onEvent,
            timerVisible = timerVisible.value,
            onTimerPress = {
              timerVisible.value = !timerVisible.value
            }
          ) {
            viewModel.onEvent(SessionEvent.AddExercise)
          }
        }
        AnimatedVisibility(
          visible = uiState.value.selectedExercise != null,
          exit = fadeOut(tween(900)),
          enter = fadeIn(tween(900))
        ) {
          SessionAppBarSelected(
            timerVisible = timerVisible.value,
            onTimerPress = {
              timerVisible.value = !timerVisible.value
            },
            onEvent = viewModel::onEvent
          )
        }
      }
    }
  ) { paddingValues ->
    Box {
      SessionHeader(
        sessionWrapper = session,
        muscleGroups = muscleGroups,
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
            exercise.sessionExercise.sessionExerciseId == selectedExercise?.sessionExercise?.sessionExerciseId
          ExerciseCard(
            exerciseWrapper = exercise,
            expanded = expanded,
            onEvent = viewModel::onEvent
          ) {
            viewModel.onEvent(SessionEvent.ExerciseSelection(exercise))
            if (!expanded) {
              coroutineScope.launch {
                scrollState.animateScrollToItem(index = (index - 2).coerceAtLeast(0))
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