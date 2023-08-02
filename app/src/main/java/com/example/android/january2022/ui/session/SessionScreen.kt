package com.example.android.january2022.ui.session

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.timer.TimerService
import com.example.android.january2022.timer.sendTimerAction
import com.example.android.january2022.ui.SessionWrapper
import com.example.android.january2022.ui.TimerState
import com.example.android.january2022.ui.datetimedialog.MaterialDialog
import com.example.android.january2022.ui.datetimedialog.rememberMaterialDialogState
import com.example.android.january2022.ui.datetimedialog.time.timepicker
import com.example.android.january2022.ui.modalbottomsheet.ModalBottomSheetLayout
import com.example.android.january2022.ui.modalbottomsheet.ModalBottomSheetValue
import com.example.android.january2022.ui.modalbottomsheet.rememberModalBottomSheetState
import com.example.android.january2022.ui.session.components.*
import com.example.android.january2022.ui.theme.onlyTop
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
  viewModel: SessionViewModel = hiltViewModel()
) {
  val uriHandler = LocalUriHandler.current
  val context = LocalContext.current

  LaunchedEffect(true) {
    viewModel.uiEvent.collect { event ->
      Timber.d("UiEvent Received: $event")
      when (event) {
        is UiEvent.OpenWebsite -> {
          uriHandler.openUri(event.url)
        }
        is UiEvent.Navigate -> onNavigate(event)
        is UiEvent.ToggleTimer -> context.sendTimerAction(TimerService.Actions.TOGGLE)
        is UiEvent.ResetTimer -> context.sendTimerAction(TimerService.Actions.RESET)
        is UiEvent.IncrementTimer -> context.sendTimerAction(TimerService.Actions.INCREMENT)
        is UiEvent.DecrementTimer -> context.sendTimerAction(TimerService.Actions.DECREMENT)
        else -> Unit
      }
    }
  }

  val session by viewModel.session.collectAsState(SessionWrapper(Session(), emptyList()))
  val exercises by viewModel.exercises.collectAsState(initial = emptyList())
  val expandedExercise by viewModel.expandedExercise.collectAsState()
  val selectedExercises by viewModel.selectedExercises.collectAsState()
  val muscleGroups by viewModel.muscleGroups.collectAsState(emptyList())

  var timerState by remember { mutableStateOf(TimerState(0L, false, 0L)) }
  val receiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
      intent?.let {
        timerState = TimerState(
          time = it.getLongExtra(TimerService.Intents.Extras.TIME.toString(), 0L),
          running = it.getBooleanExtra(TimerService.Intents.Extras.IS_RUNNING.toString(), false),
          maxTime = it.getLongExtra(TimerService.Intents.Extras.MAX_TIME.toString(), 0L)
        )
      }
    }
  }
  // Register local broadcast manager to update Timer State when Intents are received
  ContextCompat.registerReceiver(
    context,
    receiver,
    IntentFilter(TimerService.Intents.STATUS.toString()),
    ContextCompat.RECEIVER_NOT_EXPORTED
  ).also {
    context.sendTimerAction(TimerService.Actions.QUERY)
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
  val startTimeDialogState = rememberMaterialDialogState()
  MaterialDialog(
    dialogState = startTimeDialogState,
    buttons = {
      positiveButton("Ok")
      negativeButton("Cancel")
    }
  ) {
    timepicker(
      initialTime = session.session.start.toLocalTime(),
      is24HourClock = true,
      waitForPositiveButton = true,
      title = "Select end-time"
    ) { time ->
      viewModel.onEvent(SessionEvent.StartTimeChanged(time))
    }
  }
  val endTimeDialogState = rememberMaterialDialogState()
  MaterialDialog(
    dialogState = endTimeDialogState,
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

  val sessionInfoSheetState =
    rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

  ModalBottomSheetLayout(
    sheetContent = {
      Text("SESSION INFO", modifier = Modifier.height(400.dp))
    },
    sheetState = sessionInfoSheetState,
    sheetShape = MaterialTheme.shapes.large.onlyTop()
  ) {
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
            visible = expandedExercise == null,
            exit = fadeOut(tween(500)),
            enter = fadeIn(tween(500))
          ) {
            SessionAppBar(
              onDeleteSession = { deleteSessionDialog.value = true },
              timerVisible = timerVisible.value,
              onTimerPress = {
                timerVisible.value = !timerVisible.value
              }
            ) {
              viewModel.onEvent(SessionEvent.AddExercise)
            }
          }
          AnimatedVisibility(
            visible = expandedExercise != null,
            exit = fadeOut(tween(500)),
            enter = fadeIn(tween(500))
          ) {
            SessionAppBarExpanded(
              timerVisible = timerVisible.value,
              onTimerPress = {
                timerVisible.value = !timerVisible.value
              },
              onDeleteSession = { deleteSessionDialog.value = true },
              onEvent = viewModel::onEvent
            )
          }
          AnimatedVisibility(
            visible = selectedExercises.isNotEmpty(),
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
              onEvent = viewModel::onEvent
            )
          }
        }
      }
    ) { paddingValues ->
      LazyColumn(
        modifier = Modifier
          .fillMaxSize(),
        state = scrollState
      ) {
        item {
          SessionHeader(
            sessionWrapper = session,
            muscleGroups = muscleGroups,
            scrollState = scrollState,
            height = headerHeight,
            topPadding = paddingValues.calculateTopPadding(),
            onEndTime = { endTimeDialogState.show() },
            onStartTime = { startTimeDialogState.show() }
          )
        }
        itemsIndexed(
          items = exercises,
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
                scrollState.animateScrollToItem(index = (index - 1).coerceAtLeast(0))
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