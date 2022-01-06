package com.example.android.january2022.ui.theme.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.android.january2022.HomeViewModel
import com.example.android.january2022.db.entities.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun SessionScreen(homeViewModel: HomeViewModel) {
    SessionContent(homeViewModel)
}


@ExperimentalFoundationApi
@Composable
fun SessionContent(homeViewModel: HomeViewModel) {
    val sessionExercises: List<SessionExerciseWithExercise> by homeViewModel.currentSessionExerciseList.observeAsState(
        listOf()
    )
    var selectedSessionExercise by remember { mutableStateOf(-1L) }
    val session by homeViewModel.currentSession.observeAsState(Session())
    Scaffold(
        floatingActionButton = { GymFAB(homeViewModel::onNavigateToExercisePicker) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {
            Box(
                Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                SessionInfoThings(session)
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(3f)
            ) {
                items(items = sessionExercises) { sessionExercise ->
                    SessionExerciseCard(
                        sessionExercise,
                        homeViewModel,
                        selectedSessionExercise
                    ) {
                        selectedSessionExercise = it
                    }
                }
                item { Spacer(Modifier.height(100.dp)) }
            }
        }
    }
}

@Composable
fun SessionInfoThings(session: Session) {
    val startDate = SimpleDateFormat(
        "MMM d yyyy",
        Locale.ENGLISH
    ).format(session.startTimeMilli)
    val startTime = SimpleDateFormat(
        "HH:mm",
        Locale.ENGLISH
    ).format(session.startTimeMilli)
    val endTime = SimpleDateFormat(
        "HH:mm",
        Locale.ENGLISH
    ).format(session.endTimeMilli)

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TitleText(text = startDate.lowercase(), bottomPadding = 2)
            Spacer(Modifier.weight(1f))
            //Text(session.sessionId.toString())
        }
        SubTitleText(text = "$startTime - $endTime", indent = 16)
        SubTitleText(text = session.trainingType)
    }
}

@ExperimentalFoundationApi
@Composable
fun SessionExerciseCard(
    sessionExercise: SessionExerciseWithExercise,
    viewModel: HomeViewModel,
    selected: Long,
    setSelectedSessionExercise: (Long) -> Unit
) {

    val sets: List<GymSet> by viewModel.getSetsForSessionExercise(
        sessionExercise.sessionExercise.sessionExerciseId).observeAsState(listOf())

    val selected = sessionExercise.sessionExercise.sessionExerciseId == selected

    Card(
        Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 4.dp, start = 8.dp, end = 8.dp)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    delayMillis = 50,
                    easing = LinearOutSlowInEasing
                )
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        setSelectedSessionExercise(sessionExercise.sessionExercise.sessionExerciseId)
                    }
                )
            }
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 8.dp, top = 8.dp, end = 2.dp)
            ) {
                Text(
                    text = sessionExercise.exercise.exerciseTitle,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                )
                IconButton(
                    onClick = { viewModel.onAddSet(sessionExercise.sessionExercise.sessionExerciseId) },

                    ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Exercise"
                    )
                }
            }
            sets.forEach { set ->
                SetCard(
                    set,
                    viewModel::onMoodClicked,
                    viewModel::onRepsUpdated,
                    viewModel::onWeightUpdated,
                    viewModel::removeSelectedSet
                )
            }

        }
    }

}

@ExperimentalFoundationApi
@Composable
fun SetCard(
    set: GymSet,
    onMoodClicked: (GymSet, Int) -> Unit,
    onRepsUpdated: (GymSet, Int) -> Unit,
    onWeightUpdated: (GymSet, Float) -> Unit,
    removeSelectedSet: (GymSet) -> Unit
) {
    val reps: Int = set.reps
    val weight: Float = set.weight
    val mood: Int = set.mood
    var dismissed by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    val surfaceColor: Color by animateColorAsState(
        targetValue =
        if (offsetX.value > 100f && offsetX.value < 1100f) {
            MaterialTheme.colors.error
        } else Color.Transparent,
        animationSpec = tween(
            durationMillis = if (offsetX.value > 1000f || offsetX.value < 100f) 700 else 200,
            delayMillis = 25,
            easing = LinearOutSlowInEasing
        )
    )


    if(dismissed) {
        removeSelectedSet(set)
        dismissed = false
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .draggable(
                state = rememberDraggableState { delta ->
                    coroutineScope.launch {
                        offsetX.snapTo(offsetX.value + delta)
                    }
                },
                orientation = Orientation.Horizontal,
                onDragStopped = {
                    var toValue = 0f
                    if (offsetX.value > 500f){
                        toValue = 1500f
                        dismissed = true
                    }
                    coroutineScope.launch {
                        offsetX.animateTo(
                            targetValue = toValue,
                            animationSpec = tween(
                                durationMillis = 300,
                                delayMillis = 25,
                                easing = LinearOutSlowInEasing
                            )
                        )
                        //offsetX.snapTo(0f)
                    }
                }
            )
            .offset { IntOffset(offsetX.value.roundToInt(), 0) }
            .background(color = surfaceColor)
    ) {

        MoodIcons(set, mood, onMoodClicked)
        Spacer(modifier = Modifier.weight(1f))
        SetWeightRepsInputFields(
            set = set,
            reps = reps,
            weight = weight,
            onRepsUpdated = onRepsUpdated,
            onWeightUpdated = onWeightUpdated
        )

    }
}

@Composable
fun SetWeightRepsInputFields(
    set: GymSet,
    reps: Int = -1,
    weight: Float = -1F,
    onRepsUpdated: (GymSet, Int) -> Unit,
    onWeightUpdated: (GymSet, Float) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Number,
) {
    Row {
        TextField(
            value = if (reps > -1) reps.toString() else "",
            onValueChange = {
                try {
                    val newValue = it.trim().toInt()
                    onRepsUpdated(set, newValue)
                } catch (e: Exception) {
                    onRepsUpdated(set, -1)
                }
            },
            placeholder = { Text("reps") },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier.width(100.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            trailingIcon = { Icon(Icons.Default.UnfoldMore, "Number of reps") },
            maxLines = 1

        )
        TextField(
            value = if (weight > -1) weight.toString() else "",
            onValueChange = {
                try {
                    val newValue = it.trim().toFloat()
                    onWeightUpdated(set, newValue)
                } catch (e: Exception) {
                }
            },
            placeholder = { Text("weight") },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier.width(120.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            trailingIcon = { Icon(Icons.Filled.FitnessCenter, "weight") },
        )
    }
}

@Composable
fun MoodIcons(
    set: GymSet,
    mood: Int,
    onMoodClicked: (GymSet, Int) -> Unit,
) {
    Row {
        if (mood == -1 || mood == 1) {
            IconToggleButton(
                checked = mood == 1, onCheckedChange = { onMoodClicked(set, 1) }
            ) {
                Icon(
                    imageVector = Icons.Default.SentimentVeryDissatisfied,
                    contentDescription = "Bad",
                    tint = if (mood == 1) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
                )
            }
        } else {
            Spacer(Modifier.width(0.dp))
        }
        if (mood == -1 || mood == 2) IconToggleButton(
            checked = mood == 2, onCheckedChange = { onMoodClicked(set, 2) }
        ) {
            Icon(
                imageVector = Icons.Default.SentimentNeutral,
                contentDescription = "Neutral",
                tint = if (mood == 2) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
            )
        }
        if (mood == -1 || mood == 3) IconToggleButton(
            checked = mood == 3, onCheckedChange = { onMoodClicked(set, 3) }
        ) {
            Icon(
                imageVector = Icons.Default.SentimentVerySatisfied,
                contentDescription = "Good",
                tint = if (mood == 3) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
            )
        }
    }
}