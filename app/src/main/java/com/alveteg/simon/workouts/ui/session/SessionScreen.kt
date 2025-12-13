package com.alveteg.simon.workouts.ui.session

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.alveteg.simon.workouts.db.entities.Session
import com.alveteg.simon.workouts.timer.TimerService
import com.alveteg.simon.workouts.timer.sendTimerAction
import com.alveteg.simon.workouts.ui.ExerciseWrapper
import com.alveteg.simon.workouts.ui.SessionWrapper
import com.alveteg.simon.workouts.ui.SetWrapper
import com.alveteg.simon.workouts.ui.TimerState
import com.alveteg.simon.workouts.ui.datetimedialog.MaterialDialog
import com.alveteg.simon.workouts.ui.datetimedialog.rememberMaterialDialogState
import com.alveteg.simon.workouts.ui.datetimedialog.time.timepicker
import com.alveteg.simon.workouts.ui.session.components.DeletionAlertDialog
import com.alveteg.simon.workouts.ui.session.components.ExerciseBottomSheet
import com.alveteg.simon.workouts.ui.session.components.ExerciseCard
import com.alveteg.simon.workouts.ui.session.components.ExerciseDetail
import com.alveteg.simon.workouts.ui.session.components.MuscleList
import com.alveteg.simon.workouts.ui.session.components.SessionBottomSheet
import com.alveteg.simon.workouts.ui.session.components.SessionHeader
import com.alveteg.simon.workouts.ui.session.components.SetBottomSheet
import com.alveteg.simon.workouts.ui.session.components.SmallPill
import com.alveteg.simon.workouts.ui.session.components.TimerBar
import com.alveteg.simon.workouts.utils.ScaleVisibility
import com.alveteg.simon.workouts.utils.UiEvent
import kotlinx.coroutines.channels.Channel
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import timber.log.Timber
import java.time.LocalTime

@OptIn(
  ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class,
  ExperimentalSharedTransitionApi::class
)
@Composable
fun SessionScreen(
  sharedTransitionScope: SharedTransitionScope,
  animatedVisibilityScope: AnimatedVisibilityScope,
  onNavigate: (UiEvent.Navigate) -> Unit, viewModel: SessionViewModel = hiltViewModel()
) {
  val uriHandler = LocalUriHandler.current
  val context = LocalContext.current

  var openSetBottomSheet by rememberSaveable { mutableStateOf<SetWrapper?>(null) }
  var openExerciseBottomSheet by rememberSaveable { mutableStateOf<ExerciseWrapper?>(null) }

  LaunchedEffect(true) {
    viewModel.uiEvent.collect { event ->
      Timber.d("UiEvent Received: $event")
      when (event) {
        is UiEvent.OpenWebsite -> {
          uriHandler.openUri(event.url)
        }

        is UiEvent.SetCreated -> {
          openSetBottomSheet = event.set
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
  val muscleGroups by viewModel.muscleGroups.collectAsState(emptyList())

  var screenUnlocked by remember(session) { mutableStateOf(false) }
  LaunchedEffect(session) {
    screenUnlocked = session.session.end == null
  }

  var timerState by remember { mutableStateOf(TimerState(0L, false, 0L)) }
  var timerVisible by remember { mutableStateOf(false) }
  LaunchedEffect(timerState.running) {
    if (timerState.running) {
      timerVisible = true
    }
  }

  DisposableEffect(context) {
    val receiver = object : BroadcastReceiver() {
      override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
          val newTime = it.getLongExtra(TimerService.Intents.Extras.TIME.toString(), 0L)
          val newRunning =
            it.getBooleanExtra(TimerService.Intents.Extras.IS_RUNNING.toString(), false)
          val newMaxTime = it.getLongExtra(TimerService.Intents.Extras.MAX_TIME.toString(), 0L)

          timerState = TimerState(
            time = newTime, running = newRunning, maxTime = newMaxTime
          )
        }
      }
    }
    ContextCompat.registerReceiver(
      context,
      receiver,
      IntentFilter(TimerService.Intents.STATUS.toString()),
      ContextCompat.RECEIVER_NOT_EXPORTED
    )
    context.sendTimerAction(TimerService.Actions.QUERY)

    onDispose {
      context.unregisterReceiver(receiver)
    }
  }

  val listUpdatedChannel = remember { Channel<Unit>() }
  val hapticFeedback = LocalHapticFeedback.current
  val lazyListState = rememberLazyListState()
  val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
    listUpdatedChannel.tryReceive()
    Timber.d("Reorder exercise ${from.key} to ${to.key}")
    val numberOfItemsAbove = 2
    val fromIndex = from.index - numberOfItemsAbove
    val toIndex = to.index - numberOfItemsAbove
    viewModel.onEvent(SessionEvent.ReorderExercises(fromIndex, toIndex))
    listUpdatedChannel.receive()
    hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
  }

  LaunchedEffect(exercises) {
    Timber.d("Exercise list: ${exercises.map { it.sessionExercise.sessionExerciseId }}")
    listUpdatedChannel.trySend(Unit)
  }


  var deleteSessionDialog by remember { mutableStateOf(false) }
  if (deleteSessionDialog) {
    DeletionAlertDialog(onDismiss = { deleteSessionDialog = false }, onDelete = {
      viewModel.onEvent(SessionEvent.RemoveSession)
      deleteSessionDialog = false
    }, title = {
      Text(text = "Delete Session?")
    }, text = {
      Text(text = "Are you sure you want to delete this session and all of its contents? This action can not be undone.")
    })
  }

  var deleteExerciseDialog by remember { mutableStateOf(false) }
  if (deleteExerciseDialog) {
    DeletionAlertDialog(onDismiss = { deleteExerciseDialog = false }, onDelete = {
      openExerciseBottomSheet?.let { viewModel.onEvent(SessionEvent.RemoveExercise(it)) }
      deleteExerciseDialog = false
      openExerciseBottomSheet = null
    }, title = {
      Text(text = "Delete Exercise?")
    }, text = {
      Text(text = "Are you sure you want to delete this exercise and all its sets from the session? This action can not be undone.")
    })
  }

  var deleteSetDialog by remember { mutableStateOf(false) }
  if (deleteSetDialog) {
    DeletionAlertDialog(onDismiss = { deleteSetDialog = false }, onDelete = {
      openSetBottomSheet?.set?.let { viewModel.onEvent(SessionEvent.DeleteSet(it)) }
      deleteSetDialog = false
      openSetBottomSheet = null
    }, title = {
      Text(text = "Delete Set?")
    }, text = {
      Text(text = "Are you sure you want to delete this set? This action can not be undone.")
    })
  }

  val startTimeDialogState = rememberMaterialDialogState()
  val endTimeDialogState = rememberMaterialDialogState()
  MaterialDialog(
    dialogState = startTimeDialogState, buttons = {
      positiveButton("Ok")
      negativeButton("Cancel")
    }) {
    timepicker(
      initialTime = session.session.start.toLocalTime(),
      is24HourClock = true,
      waitForPositiveButton = true,
      title = "Set start time"
    ) { time ->
      viewModel.onEvent(SessionEvent.SetStartTime(time))
    }
  }
  MaterialDialog(
    dialogState = endTimeDialogState, buttons = {
      positiveButton("Ok")
      negativeButton("Cancel")
    }) {
    timepicker(
      initialTime = LocalTime.now(),
      is24HourClock = true,
      waitForPositiveButton = true,
      title = "Set end time"
    ) { time ->
      viewModel.onEvent(SessionEvent.SetEndTime(time))
      screenUnlocked = false
    }
  }

  var skipPartiallyExpanded by rememberSaveable { mutableStateOf(false) }
  val setBottomSheetState =
    rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)
  val exerciseBottomSheetState =
    rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)

  if (openSetBottomSheet != null) {
    val setWrapper = remember(exercises, openSetBottomSheet) {
      val updatedExerciseWrapper = exercises.find {
        it.exercise.id == openSetBottomSheet?.exerciseWrapper?.exercise?.id
      }
      val updatedSet = updatedExerciseWrapper?.sets?.find {
        it.setId == openSetBottomSheet?.set?.setId
      }
      if (updatedExerciseWrapper != null && updatedSet != null) {
        SetWrapper(set = updatedSet, exerciseWrapper = updatedExerciseWrapper)
      } else {
        openSetBottomSheet!!
      }
    }

    SetBottomSheet(
      setWrapper = setWrapper,
      sessionWrapper = session,
      exerciseWrapper = setWrapper.exerciseWrapper,
      sheetState = setBottomSheetState,
      getSetHistory = viewModel::getHistoryForExercise,
      onDeleteSet = { deleteSetDialog = true },
      onEvent = viewModel::onEvent,
    ) { openSetBottomSheet = null }
  }

  if (openExerciseBottomSheet != null) {
    val exerciseWrapper = remember(exercises, openExerciseBottomSheet) {
      exercises.find {
        it.sessionExercise.sessionExerciseId == openExerciseBottomSheet?.sessionExercise?.sessionExerciseId
      }!!
    }
    ExerciseBottomSheet(
      exercise = exerciseWrapper.exercise,
      onDismissRequest = { openExerciseBottomSheet = null },
      getSetHistory = viewModel::getHistoryForExercise,
      sheetState = exerciseBottomSheetState,
    )
  }

  with(sharedTransitionScope) {
    Scaffold(
      modifier = Modifier.sharedBounds(
        sharedContentState = sharedTransitionScope.rememberSharedContentState(key = "session-${session.session.sessionId}"),
        animatedVisibilityScope = animatedVisibilityScope,
        clipInOverlayDuringTransition = OverlayClip(
          clipShape = MaterialTheme.shapes.large
        )
      ),
      floatingActionButton = {
        ScaleVisibility(visible = screenUnlocked) {
          FloatingActionButton(
            onClick = {
              viewModel.onEvent(SessionEvent.AddExercise)
            }) {
            Icon(
              Icons.Default.Add,
              contentDescription = "Add Exercise to Session.",
            )
          }
        }
      },
    ) { innerPadding ->
      LazyColumn(
        state = lazyListState,
        modifier = Modifier
          .fillMaxSize()
          .padding(top = innerPadding.calculateTopPadding()),
      ) {
        val verticalSpacing = 6.dp
        val horizontalPadding = 8.dp

        item {
          SessionHeader(
            modifier = Modifier
              .padding(horizontal = horizontalPadding),
            dateModifier = Modifier
              .sharedElement(
                sharedContentState = sharedTransitionScope.rememberSharedContentState(key = "session-date-${session.session.sessionId}"),
                animatedVisibilityScope = animatedVisibilityScope
              ),
            titleModifier = Modifier
              .sharedBounds(
                sharedContentState = sharedTransitionScope.rememberSharedContentState(key = "session-title-${session.session.sessionId}"),
                animatedVisibilityScope = animatedVisibilityScope,
              ),
            sessionWrapper = session,
            screenUnlocked = screenUnlocked,
            muscleGroups = muscleGroups,
            onDeleteSession = { deleteSessionDialog = true },
            onEndTime = { endTimeDialogState.show() },
            onStartTime = { startTimeDialogState.show() },
            timerState = timerState,
            timerVisible = timerVisible,
            onTimerButtonClick = { timerVisible = !timerVisible },
            onToggleEdit = {
              if (session.session.end == null) {
                endTimeDialogState.show()
              } else {
                screenUnlocked = !screenUnlocked
              }
            })
        }
        stickyHeader {
          if (screenUnlocked && timerVisible) {
            TimerBar(
              modifier = Modifier
                .padding(horizontal = horizontalPadding, vertical = verticalSpacing)
                .animateItem(),
              timerState = timerState,
              onEvent = viewModel::onEvent
            )
          }
        }
        items(
          items = exercises, key = { it.sessionExercise.sessionExerciseId }
        ) { exercise ->
          ReorderableItem(
            state = reorderableLazyListState,
            key = exercise.sessionExercise.sessionExerciseId
          ) {
            ExerciseCard(
              modifier = Modifier
                .padding(horizontal = horizontalPadding, vertical = verticalSpacing)
                .longPressDraggableHandle(
                  enabled = screenUnlocked,
                  onDragStarted = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate)
                  },
                  onDragStopped = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.GestureEnd)
                  },
                ),
              exerciseWrapper = exercise,
              editable = screenUnlocked,
              onEvent = viewModel::onEvent,
              onClick = { openExerciseBottomSheet = it },
              onSetClicked = { openSetBottomSheet = it })
          }
        }
        item {
          Spacer(modifier = Modifier.height(innerPadding.calculateBottomPadding()))
        }
      }
    }
  }
}
