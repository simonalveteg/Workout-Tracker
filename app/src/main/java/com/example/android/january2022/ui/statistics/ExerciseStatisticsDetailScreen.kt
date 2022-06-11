package com.example.android.january2022.ui.statistics

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.january2022.ui.session.SessionExerciseCard
import com.example.android.january2022.ui.session.SessionExerciseHistoryCard

@Composable
fun ExerciseStatisticsDetailScreen(
    viewModel: ExerciseStatisticsViewModel = hiltViewModel()
) {

    val currentExercise = viewModel.currentExercise
    val sessionExercises by viewModel.getSessionExercisesWithSets().collectAsState(initial = emptyMap())

    Log.d("ESDS",sessionExercises.entries.toString())
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(text = currentExercise.title)
        }
        items(sessionExercises.entries.toList()) { sessionExercise ->
            SessionExerciseHistoryCard(
                sessionExercise = sessionExercise.key,
                sets = sessionExercise.value,
                onEvent = viewModel::onEvent
            )
        }
    }
}