package com.example.android.january2022.ui.theme.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.android.january2022.HomeViewModel
import com.example.android.january2022.db.entities.*
import java.text.SimpleDateFormat
import java.util.*


@ExperimentalMaterialApi
@Composable
fun SessionScreen(homeViewModel: HomeViewModel, navController: NavController) {
    SessionContent(homeViewModel, navController)
}


@Composable
fun SessionContent(homeViewModel: HomeViewModel, navController: NavController) {
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

@Composable
fun SessionExerciseCard(
    sessionExercise: SessionExerciseWithExercise,
    viewModel: HomeViewModel,
    selected: Long,
    setSelectedSessionExercise: (Long) -> Unit
) {

    val sets: List<GymSet> by viewModel.setsList.observeAsState(listOf())
    var selectedSet by remember { mutableStateOf(-1L) }

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
            .selectable(
                selected = selected == sessionExercise.sessionExercise.sessionExerciseId,
                onClick = {
                    setSelectedSessionExercise(sessionExercise.sessionExercise.sessionExerciseId)
                }
            )
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
                if (set.parentSessionExerciseId == sessionExercise.sessionExercise.sessionExerciseId) {
                    SetCard(
                        set,
                        viewModel::onMoodClicked,
                        viewModel::onRepsUpdated,
                        viewModel::onWeightUpdated,
                        selectedSet
                    ) {
                        selectedSet = it
                    }
                }
            }
        }
    }

}

@Composable
fun SetCard(
    set: GymSet,
    onMoodClicked: (GymSet, Int) -> Unit,
    onRepsUpdated: (GymSet, Int) -> Unit,
    onWeightUpdated: (GymSet, Float) -> Unit,
    selectedSet: Long,
    setSelectedSet: (Long) -> Unit
) {
    val reps: Int = set.reps
    val weight: Float = set.weight
    val mood: Int = set.mood


    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .selectable(
                selected = selectedSet == set.setId,
                onClick = { setSelectedSet(set.setId) }
            )
    ) {

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

        Spacer(modifier = Modifier.weight(1f))
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.width(100.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            trailingIcon = { Icon(Icons.Default.UnfoldMore, "weight") },
            maxLines = 1

        )
        TextField(
            value = if (weight > -1) weight.toString() else "",
            onValueChange = {
                try {
                    val newValue = it.trim().toFloat()
                    onWeightUpdated(set, newValue)
                } catch (e: Exception) {
                    //onWeightUpdated(set, -1F)
                }
            },
            placeholder = { Text("weight") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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

