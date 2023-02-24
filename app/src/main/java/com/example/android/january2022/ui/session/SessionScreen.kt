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
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.ui.datetimedialog.MaterialDialog
import com.example.android.january2022.ui.datetimedialog.rememberMaterialDialogState
import com.example.android.january2022.ui.datetimedialog.time.TimePickerDefaults
import com.example.android.january2022.ui.datetimedialog.time.timepicker
import com.example.android.january2022.ui.rework.MainViewModel
import com.example.android.january2022.utils.UiEvent
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalTime
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
  val expandedExercise = uiState.value.expandedExercise
  val selectedExercises = uiState.value.selectedExercises
  val muscleGroups by uiState.value.currentSession.muscleGroups.collectAsState(initial = emptyList())
  val timerState by viewModel.timerState.collectAsState()

  val uriHandler = LocalUriHandler.current

  LaunchedEffect(true) {
    Timber.d(session.toString())
    viewModel.uiEvent.collect { event ->
      Timber.d("UiEvent Received: $event")
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

  val deleteExerciseDialog = remember { mutableStateOf(false) }
  val deleteSessionDialog = remember { mutableStateOf(false) }
  val deleteSetDialog = remember { mutableStateOf<GymSet?>(null) }

  if (deleteExerciseDialog.value) {
    DeletionAlertDialog(
      onDismiss = { deleteExerciseDialog.value = false },
      onDelete = {
        viewModel.onEvent(SessionEvent.RemoveSelectedExercises)
        deleteExerciseDialog.value = false
      },
      title = {
        Text(text = "Remove ${selectedExercises.size} Exercise${if (selectedExercises.size > 1) "s" else ""}?")
      },
      text = {
        Text(text = "Are you sure you want to remove the selected exercises from this session? This action can not be undone.")
      }
    )
  }
  if (deleteSessionDialog.value) {
    DeletionAlertDialog(
      onDismiss = { deleteSessionDialog.value = false },
      onDelete = {
        viewModel.onEvent(SessionEvent.RemoveSession)
        deleteSessionDialog.value = false
      },
      title = {
        Text(text = "Delete Session?")
      },
      text = {
        Text(text = "Are you sure you want to delete this session and all of its contents? This action can not be undone.")
      }
    )
  }
  if (deleteSetDialog.value != null) {
    DeletionAlertDialog(
      onDismiss = { deleteSetDialog.value = null },
      onDelete = {
        deleteSetDialog.value?.let { viewModel.onEvent(SessionEvent.SetDeleted(it)) }
        deleteSetDialog.value = null
      },
      title = {
        Text(text = "Delete Set?")
      },
      text = {
        Text(text = "Are you sure you want to delete this set? This action can not be undone.")
      }
    )
  }
  val dialogState = rememberMaterialDialogState()
  MaterialDialog(
    dialogState = dialogState,
    buttons = {
      positiveButton("Ok")
      negativeButton("Cancel")
    }
  ) {
    timepicker(
      initialTime = LocalTime.now(),
      is24HourClock = true,
      waitForPositiveButton = true,
      title = "Select end-time"
    ) { time ->
      viewModel.onEvent(SessionEvent.EndTimeChanged(time))
    }
  }

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
          visible = uiState.value.expandedExercise == null,
          exit = fadeOut(tween(500)),
          enter = fadeIn(tween(500))
        ) {
          SessionAppBar(
            onEvent = viewModel::onEvent,
            onDeleteSession = { deleteSessionDialog.value = true },
            timerVisible = timerVisible.value,
            onTimerPress = {
              timerVisible.value = !timerVisible.value
            },
            onTime = { dialogState.show() }
          ) {
            viewModel.onEvent(SessionEvent.AddExercise)
          }
        }
        AnimatedVisibility(
          visible = uiState.value.expandedExercise != null,
          exit = fadeOut(tween(500)),
          enter = fadeIn(tween(500))
        ) {
          SessionAppBarExpanded(
            timerVisible = timerVisible.value,
            onTimerPress = {
              timerVisible.value = !timerVisible.value
            },
            onDeleteSession = { deleteSessionDialog.value = true },
            onEvent = viewModel::onEvent,
            onTime = { dialogState.show() }
          )
        }
        AnimatedVisibility(
          visible = uiState.value.selectedExercises.isNotEmpty(),
          exit = fadeOut(tween(500)),
          enter = fadeIn(tween(500))
        ) {
          SessionAppBarSelected(
            timerVisible = timerVisible.value,
            onTimerPress = {
              timerVisible.value = !timerVisible.value
            },
            onDeleteExercise = { deleteExerciseDialog.value = true },
            onDeleteSession = { deleteSessionDialog.value = true },
            onEvent = viewModel::onEvent,
            onTime = { dialogState.show() }
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
            exercise.sessionExercise.sessionExerciseId == expandedExercise?.sessionExercise?.sessionExerciseId
          val selected = selectedExercises.contains(exercise)
          ExerciseCard(
            exerciseWrapper = exercise,
            expanded = expanded,
            selected = selected,
            onEvent = viewModel::onEvent,
            onLongClick = { viewModel.onEvent(SessionEvent.ExerciseSelected(exercise)) },
            onSetDeleted = { deleteSetDialog.value = it }
          ) {
            viewModel.onEvent(SessionEvent.ExerciseExpanded(exercise))
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