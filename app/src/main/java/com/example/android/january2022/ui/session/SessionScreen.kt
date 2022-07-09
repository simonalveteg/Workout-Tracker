package com.example.android.january2022.ui.session

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.january2022.db.entities.*
import com.example.android.january2022.ui.session.components.SessionAppBar
import com.example.android.january2022.ui.session.components.SessionExerciseCard
import com.example.android.january2022.ui.session.components.SessionInfo
import com.example.android.january2022.utils.Event
import com.example.android.january2022.utils.UiEvent
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: SessionViewModel = hiltViewModel()
) {
    val selectedSessionExercise by derivedStateOf { viewModel.selectedSessionExercise }

    val session by derivedStateOf { viewModel.currentSession }
    val allSets by viewModel.setsList.observeAsState(listOf())
    val setsMap by derivedStateOf { allSets.groupBy { it.parentSessionExerciseId } }

    val sessionExercises: List<SessionExerciseWithExercise> by viewModel.getSessionExercisesForSession()
        .observeAsState(listOf())
    val muscleGroups by session.let {
        viewModel.getMuscleGroupsForSession(it.sessionId).collectAsState(emptyList())
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val topAppBarScrollState = rememberTopAppBarScrollState()
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec, topAppBarScrollState)
    }
    val mContext = LocalContext.current
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, year: Int, month: Int, day: Int ->
            val newDateTime = session.end.withYear(year).withMonth(month + 1).withDayOfMonth(day)
            viewModel.onEvent(SessionEvent.DateChanged(newDateTime))
        }, session.start.year, session.start.monthValue - 1, session.start.dayOfMonth
    )

    val timerRunning by viewModel.timerIsRunning.observeAsState(false)
    val timerVisible = rememberSaveable { mutableStateOf(false) }
    val timerColor by animateColorAsState(targetValue = if (timerRunning && !timerVisible.value)
        MaterialTheme.colorScheme.primary else LocalContentColor.current)
    val timerBackground by animateColorAsState(
        if (timerRunning) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceVariant
    )
    val currentTimerWidth = LocalConfiguration.current.screenWidthDp
    val timerTime = viewModel.timerTime.observeAsState(0)
    val timerMaxTime = viewModel.timerMaxTime.observeAsState(60000L)
    val timerText = if (timerRunning) {
        viewModel.timerTime.observeAsState(0L).value.toTimerString()
    } else timerMaxTime.value.toTimerString()
    val timerWidth by derivedStateOf {
        currentTimerWidth.times(timerTime.value.toFloat().div(timerMaxTime.value))
            .toInt().dp
    }
    val animatedTimerWidth by animateDpAsState(
        targetValue = timerWidth,
        animationSpec = tween(50, 0, LinearEasing)
    )

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event)
                is UiEvent.ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.actionLabel,
                        withDismissAction = true
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
            SessionAppBar(
                session = session,
                timerVisible = timerVisible,
                timerColor = timerColor,
                mDatePickerDialog = mDatePickerDialog,
                scrollBehavior = scrollBehavior,
                timerBackground = timerBackground,
                animatedWidth = animatedTimerWidth,
                onEvent = viewModel::onEvent,
                timerText = timerText
            )
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
        ) {
            item {
                SessionInfo(
                    startTime = session.start,
                    endTime = session.end,
                    muscleGroups = muscleGroups,
                    onEvent = viewModel::onEvent
                )
            }

            items(
                items = sessionExercises,
                key = { it.sessionExercise.sessionExerciseId }) { sessionExercise ->
                val sets = setsMap[sessionExercise.sessionExercise.sessionExerciseId].orEmpty()
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
    val displayedSeconds = if (seconds < 10) "0$seconds" else seconds
    return "$minutes:$displayedSeconds"
}