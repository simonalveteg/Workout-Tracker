package com.example.android.january2022.ui.statistics

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.january2022.ui.session.SessionExerciseCard
import com.example.android.january2022.ui.session.SessionExerciseHistoryCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseStatisticsDetailScreen(
    viewModel: ExerciseStatisticsViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val currentExercise = viewModel.currentExercise
    val sessionExercises by viewModel.getSessionExercisesWithSets()
        .collectAsState(initial = emptyMap())

    Log.d("ESDS", sessionExercises.entries.toString())
    Scaffold(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.statusBars),
        topBar = {
            SmallTopAppBar(
                title = { Text(currentExercise.title) },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (sessionExercises.values.isEmpty()) {
                Text("no data")
            } else {
                ExerciseChart(
                    data = sessionExercises, modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .weight(3f)
                )
            }
            LazyColumn(
                modifier = Modifier.weight(5f)
            ) {
                items(
                    sessionExercises.entries.toList()
                        .sortedByDescending { it.key.session.start }) { sessionExercise ->
                    SessionExerciseHistoryCard(
                        sessionExercise = sessionExercise.key,
                        sets = sessionExercise.value,
                        onEvent = viewModel::onEvent
                    )
                }
                item {
                    Spacer(Modifier.height(100.dp))
                }
            }
        }

    }
}
