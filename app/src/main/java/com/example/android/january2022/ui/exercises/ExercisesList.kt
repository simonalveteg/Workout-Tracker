package com.example.android.january2022.ui.exercises

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.january2022.db.entities.Exercise


@Composable
fun ExercisesList(
    viewModel: ExerciseViewModel,
    exercises: List<Exercise>,
    selectedExercises: Set<Exercise> = emptySet(),
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item{
            Spacer(modifier = Modifier.height(4.dp))
        }
        items(items = exercises) { exercise ->
            AnimatedVisibility(visible = true) {
                val selected by derivedStateOf{ selectedExercises.contains(exercise)}
                ExerciseCard(
                    exercise = exercise,
                    selected = selected,
                    onEvent = viewModel::onEvent
                )
            }
        }
    }
}