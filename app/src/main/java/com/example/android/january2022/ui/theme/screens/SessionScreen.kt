package com.example.android.january2022.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                    SessionExerciseCard(sessionExercise, homeViewModel)
                }
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
fun SessionExerciseCard(sessionExercise: SessionExerciseWithExercise, viewModel: HomeViewModel) {

    val sets: List<GymSet> by viewModel.setsList.observeAsState(listOf())

    Column {
        Card(
            Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp, start = 8.dp, end = 8.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 8.dp, top = 8.dp)

                ) {
                    Text(
                        text = sessionExercise.exercise.exerciseTitle,
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
                        SetCard(set, viewModel::onMoodClicked)
                    }
                }
            }
        }
    }
}

@Composable
fun SetCard(set: GymSet, onMoodClicked: (set: GymSet, a: Int) -> Unit) {
    val reps: Int = set.reps
    val mood: Int = set.mood


    Row {
        IconToggleButton(checked = mood == 1, onCheckedChange = { onMoodClicked(set, 1) }) {
            Icon(
                imageVector = Icons.Default.SentimentVeryDissatisfied,
                contentDescription = "Bad",
                tint = if (mood == 1) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
            )
        }
        IconToggleButton(checked = mood == 2, onCheckedChange = { onMoodClicked(set, 2) }) {
            Icon(
                imageVector = Icons.Default.SentimentNeutral,
                contentDescription = "Neutral",
                tint = if (mood == 2) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
            )
        }
        IconToggleButton(checked = mood == 3, onCheckedChange = { onMoodClicked(set, 3) }) {
            Icon(
                imageVector = Icons.Default.SentimentVerySatisfied,
                contentDescription = "Good",
                tint = if (mood == 3) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        TextField(value = reps.toString(), onValueChange = {})
    }
}

