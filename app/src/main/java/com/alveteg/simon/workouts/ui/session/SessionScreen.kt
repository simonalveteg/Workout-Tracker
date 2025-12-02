package com.alveteg.simon.workouts.ui.session

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.alveteg.simon.workouts.db.entities.Session
import com.alveteg.simon.workouts.db.entities.SessionExercise
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
import com.alveteg.simon.workouts.ui.session.components.ExerciseCard
import com.alveteg.simon.workouts.ui.session.components.InputField
import com.alveteg.simon.workouts.ui.session.components.SessionHeader
import com.alveteg.simon.workouts.ui.session.components.SetHistoryCard
import com.alveteg.simon.workouts.ui.session.components.TimerBar
import com.alveteg.simon.workouts.utils.FloatInputTransformation
import com.alveteg.simon.workouts.utils.IntegerInputTransformation
import com.alveteg.simon.workouts.utils.UiEvent
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SessionScreen(
  onNavigate: (UiEvent.Navigate) -> Unit, viewModel: SessionViewModel = hiltViewModel()
) {
  val uriHandler = LocalUriHandler.current
  val context = LocalContext.current

  var openSetBottomSheet by rememberSaveable { mutableStateOf<SetWrapper?>(null) }
  var openExerciseBottomSheet by rememberSaveable { mutableStateOf<SessionExercise?>(null) }

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

  var screenUnlocked by remember(session) { mutableStateOf(session.session.end == null) }
  var timerState by remember { mutableStateOf(TimerState(0L, false, 0L)) }
  var timerVisible by remember { mutableStateOf(false) }

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

  val deleteSessionDialog = remember { mutableStateOf(false) }
  if (deleteSessionDialog.value) {
    DeletionAlertDialog(onDismiss = { deleteSessionDialog.value = false }, onDelete = {
      viewModel.onEvent(SessionEvent.RemoveSession)
      deleteSessionDialog.value = false
    }, title = {
      Text(text = "Delete Session?")
    }, text = {
      Text(text = "Are you sure you want to delete this session and all of its contents? This action can not be undone.")
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
    val setWrapper = openSetBottomSheet!!
    val exerciseName = setWrapper.exerciseWrapper.exercise.title
    val setNumber = setWrapper.exerciseWrapper.sets.indexOf(setWrapper.set).let { index ->
      if (index == -1) {
        setWrapper.exerciseWrapper.sets.size
      } else {
        index
      }
    } + 1
    var setHistory by remember {
      mutableStateOf<List<Pair<SessionWrapper, ExerciseWrapper>>>(emptyList())
    }


    val repsTextFieldState =
      rememberTextFieldState(initialText = setWrapper.set.reps?.toString() ?: "")
    val weightTextFieldState =
      rememberTextFieldState(initialText = setWrapper.set.weight?.toString() ?: "")

    LaunchedEffect(setWrapper) {
      setHistory = viewModel.getHistoryForExercise(setWrapper.exerciseWrapper.exercise)
        .filter { it.first.session.sessionId != session.session.sessionId }
    }

    LaunchedEffect(repsTextFieldState, weightTextFieldState) {
      snapshotFlow { repsTextFieldState.text.toString() to weightTextFieldState.text.toString() }
        .collectLatest { (repsText, weightText) ->
          val reps = repsText.toIntOrNull()
          val weight = weightText.toFloatOrNull()
          Timber.d("Reps: $reps, Weight: $weight")

          var updatedSet = setWrapper.set

          reps?.let { updatedSet = updatedSet.copy(reps = it) }
          weight?.let { updatedSet = updatedSet.copy(weight = it) }

          if (updatedSet != setWrapper.set) {
            viewModel.onEvent(SessionEvent.ChangeSet(updatedSet))
          }
        }
    }

    ModalBottomSheet(
      onDismissRequest = { openSetBottomSheet = null },
      sheetState = setBottomSheetState,
      dragHandle = {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)
            ) {
              Text(
                text = "SET $setNumber", style = MaterialTheme.typography.titleLarge
              )
              Text(
                text = exerciseName, style = MaterialTheme.typography.titleSmall
              )
            }
          }
          HorizontalDivider()
        }
      }) {
      Column() {
        LazyRow(
          modifier = Modifier
            .padding(vertical = 8.dp)
            .height(60.dp)
        ) {
          items(setHistory) { pair ->
            val (sessionWrapper, exerciseWrapper) = pair
            SetHistoryCard(
              modifier = Modifier.padding(horizontal = 4.dp),
              sessionWrapper = sessionWrapper,
              exerciseWrapper = exerciseWrapper
            )
          }
        }
        Spacer(modifier = Modifier.height(80.dp))
        HorizontalDivider()
        Row(
          horizontalArrangement = Arrangement.SpaceAround,
          modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
        ) {
          InputField(
            textFieldState = repsTextFieldState,
            inputTransformation = IntegerInputTransformation(),
            isValid = repsTextFieldState.text.toString()
              .let { it.isNotEmpty() && it.toIntOrNull() == null },
            imeAction = ImeAction.Next,
            labelText = "Reps",
            modifier = Modifier
              .weight(1f)
              .padding(horizontal = 4.dp)
          )
          InputField(
            textFieldState = weightTextFieldState,
            inputTransformation = FloatInputTransformation(),
            isValid = weightTextFieldState.text.toString()
              .let { it.isNotEmpty() && it.toFloatOrNull() == null },
            imeAction = ImeAction.Done,
            labelText = "Weight",
            modifier = Modifier
              .weight(1f)
              .padding(horizontal = 4.dp)
          )
        }
        Spacer(modifier = Modifier.height(120.dp))
      }
    }
  }
  if (openExerciseBottomSheet != null) {
    ModalBottomSheet(
      onDismissRequest = { openExerciseBottomSheet = null },
      sheetState = exerciseBottomSheetState,
    ) {
      (1..4).forEach {
        Text(
          text = "Hello $it"
        )
      }
    }
  }

  Scaffold(
    floatingActionButton = {
      AnimatedVisibility(
        visible = screenUnlocked,
        enter = scaleIn(animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()),
        exit = scaleOut(animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec())
      ) {
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
      modifier = Modifier
        .fillMaxSize()
        .padding(top = innerPadding.calculateTopPadding()),
    ) {
      val verticalSpacing = 6.dp
      val horizontalPadding = 8.dp

      item {
        SessionHeader(
          modifier = Modifier.padding(horizontal = horizontalPadding),
          sessionWrapper = session,
          screenUnlocked = screenUnlocked,
          muscleGroups = muscleGroups,
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
              .animateItem(), timerState = timerState, onEvent = viewModel::onEvent
          )
        }
      }
      itemsIndexed(
        items = exercises, key = { _, exercise ->
          exercise.sessionExercise.sessionExerciseId
        }) { index, exercise ->
        ExerciseCard(
          modifier = Modifier
            .padding(horizontal = horizontalPadding, vertical = verticalSpacing)
            .animateItem(),
          exerciseWrapper = exercise,
          editable = screenUnlocked,
          onEvent = viewModel::onEvent,
          onClick = { openExerciseBottomSheet = it.sessionExercise },
          onSetClicked = { openSetBottomSheet = it })
      }
      item {
        Spacer(modifier = Modifier.height(innerPadding.calculateBottomPadding()))
      }
    }
  }
}
