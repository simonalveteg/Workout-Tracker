package com.example.android.january2022.ui.session

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.animation.AnimatedVisibility
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
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
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
            val timerVisible = rememberSaveable { mutableStateOf(false) }
            Column {
                LargeTopAppBar(
                    title = { Text(text = sessionTitle(session)) },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(),
                    actions = {
                        val dropdownExpanded = remember { mutableStateOf(false) }
                        IconButton(onClick = { timerVisible.value = !timerVisible.value }) {
                            Icon(
                                imageVector = Icons.Outlined.Timer,
                                contentDescription = "Timer"
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
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                    ) {
                        val currentWidth = LocalConfiguration.current.screenWidthDp
                        val timerTime = viewModel.timerTime.observeAsState()
                        val timerMaxTime = viewModel.timerMaxTime
                        Log.d("SS","currentWidth: $currentWidth")
                        val width = timerTime.value?.toFloat()?.div(timerMaxTime)
                            ?.let { currentWidth.times(it) }?.toInt()?.dp ?: 40.dp
                        Log.d("SS","width: $width")
                        val animatedWidth by animateDpAsState(
                            targetValue = width,
                            animationSpec = tween(1000,0, LinearEasing)
                        )


                        Box(Modifier.fillMaxSize()) {
                            Surface(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(animatedWidth),
                                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.5f)
                            ) {

                            }
                            Text(
                                text = viewModel.timerTime.observeAsState().value.toString(),
                                modifier = Modifier.align(Alignment.Center)
                            )
                            IconButton(
                                onClick = { viewModel.onEvent(SessionEvent.TimerToggled) },
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
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