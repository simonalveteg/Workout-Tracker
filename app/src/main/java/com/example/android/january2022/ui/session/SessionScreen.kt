package com.example.android.january2022.ui.session

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.SnackbarResult.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.january2022.db.entities.*
import com.example.android.january2022.utils.UiEvent
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: SessionViewModel = hiltViewModel()
) {
    val selectedSessionExercise = viewModel.selectedSessionExercise
    val session = viewModel.currentSession
    val allSets by viewModel.setsList.observeAsState(listOf())
    val sessionExercises: List<SessionExerciseWithExercise> by viewModel.getSessionExercisesForSession()
        .observeAsState(
            listOf()
        )
    val muscleGroups = session.let {
        viewModel.getMuscleGroupsForSession(it.sessionId).collectAsState(initial = emptyList())
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val topAppBarScrollState = rememberTopAppBarScrollState()
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec,topAppBarScrollState)
    }
    val mContext = LocalContext.current
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, year: Int, month: Int, day: Int ->
            val newDateTime = session.end.withYear(year).withMonth(month + 1).withDayOfMonth(day)
            viewModel.onEvent(SessionEvent.DateChanged(newDateTime))
        }, session.start.year, session.start.monthValue - 1, session.start.dayOfMonth
    )

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event)
                is UiEvent.ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.actionLabel
                    )
                    if (result == ActionPerformed) {
                        if (event.action != null) viewModel.onEvent(event.action)
                    }
                }
                else -> Unit
            }
        }
    }
    Scaffold(
        modifier = Modifier
            .padding(bottom = 60.dp)
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .windowInsetsPadding(WindowInsets.statusBars),
        topBar = {
            val timerRunning by viewModel.timerIsRunning.observeAsState(false)
            val timerVisible = rememberSaveable { mutableStateOf(false) }
            val timerColor by animateColorAsState(targetValue =
                if(timerRunning && !timerVisible.value) MaterialTheme.colorScheme.primary else LocalContentColor.current)
            Column {
                LargeTopAppBar(
                    title = { Text(text = sessionTitle(session)) },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(),
                    actions = {
                        val dropdownExpanded = remember { mutableStateOf(false) }
                        IconButton(onClick = { timerVisible.value = !timerVisible.value }) {
                            Icon(
                                imageVector = Icons.Outlined.Timer,
                                contentDescription = "Timer",
                                tint = timerColor
                            )
                        }
                        DropdownMenu(
                            expanded = dropdownExpanded.value,
                            onDismissRequest = { dropdownExpanded.value = false }) {
                            DropdownMenuItem(
                                text = { Text("Change Date") },
                                onClick = {
                                    mDatePickerDialog.show()
                                    dropdownExpanded.value = false
                                }
                            )

                        }
                        IconButton(onClick = { dropdownExpanded.value = true }) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
                AnimatedVisibility(visible = timerVisible.value) {
                    val timerBackground by animateColorAsState(
                        targetValue =
                        if (timerRunning) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        color = timerBackground,
                    ) {
                        val currentWidth = LocalConfiguration.current.screenWidthDp
                        val timerTime = viewModel.timerTime.observeAsState(0)
                        val timerMaxTime = viewModel.timerMaxTime.observeAsState(60000L)
                        val timerText = if(timerRunning) {
                            viewModel.timerTime.observeAsState(0L).value.toTimerString()
                        } else timerMaxTime.value.toTimerString()
                        val width = currentWidth.times(timerTime.value.toFloat().div(timerMaxTime.value))
                            .toInt().dp
                        val animatedWidth by animateDpAsState(
                            targetValue = width,
                            animationSpec = tween(50, 0, LinearEasing)
                        )

                        Box(Modifier.fillMaxSize()) {
                            Surface(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(animatedWidth),
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
                            ) {}
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(onClick = { viewModel.onEvent(SessionEvent.TimerChanged(false)) }) {
                                        Icon(
                                            imageVector = Icons.Filled.Remove,
                                            contentDescription = "Decrease length of timer"
                                        )
                                    }
                                    Text(
                                        text = timerText,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                    IconButton(onClick = { viewModel.onEvent(SessionEvent.TimerChanged(true)) }) {
                                        Icon(
                                            imageVector = Icons.Filled.Add,
                                            contentDescription = "Increase length of timer"
                                        )
                                    }
                                }
                                Row {
                                    IconButton(
                                        onClick = { viewModel.onEvent(SessionEvent.TimerReset) },
                                        modifier = Modifier
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Replay,
                                            contentDescription = "Reset timer"
                                        )
                                    }
                                    IconButton(
                                        onClick = { viewModel.onEvent(SessionEvent.TimerToggled) },
                                        modifier = Modifier
                                            .padding(end = 12.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Pause,
                                            contentDescription = "Start or stop timer"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(SessionEvent.OnAddSessionExerciseClicked) },
                shape = RoundedCornerShape(35),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, "")
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
            //.statusBarsPadding()
        ) {
            item {
                Surface(
                    Modifier
                        .padding(16.dp)
                ) {
                    SessionInfo(session, muscleGroups, viewModel::onEvent)
                }
            }
            items(
                items = sessionExercises,
                key = { it.sessionExercise.sessionExerciseId }) { sessionExercise ->

                val sets = mutableListOf<GymSet>()
                allSets.forEach { set ->
                    if (set.parentSessionExerciseId == sessionExercise.sessionExercise.sessionExerciseId) {
                        sets.add(set)
                    }
                }
                SessionExerciseCard(
                    sessionExercise = sessionExercise,
                    selected = selectedSessionExercise,
                    sets = sets,
                    onEvent = viewModel::onEvent
                )
            }
            item { Spacer(Modifier.height(100.dp)) }
        }
    }

}

fun sessionTitle(session: Session): String {
    val date = session.start
    return DateTimeFormatter.ofPattern("MMM d yyyy").format(date)
}

fun Long.toTimerString(): String {
    val totalSeconds = this.toFloat().div(1000).roundToInt()
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    val displayedSeconds = if(seconds < 10) "0$seconds" else seconds
    return "$minutes:$displayedSeconds"
}