package com.example.android.january2022.ui.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.january2022.ui.exercises.ExerciseCard
import com.example.android.january2022.utils.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val exercisesWithCount by viewModel.getExercisesWithCount().collectAsState(emptyMap())

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }

    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(exercisesWithCount.toList()) { exercise ->
                ExerciseCard(
                    exercise = exercise.first,
                    selected = false,
                    tonalElevation = if(exercise.second > 0) 1.dp else 0.dp,
                    onEvent = viewModel::onEvent
                )
            }
        }
    }
}