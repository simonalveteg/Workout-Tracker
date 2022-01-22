package com.example.android.january2022.ui.exercises

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.utils.Event


@Composable
fun ExercisesList(
    viewModel: ExerciseViewModel,
    selectedExercises: Set<Long> = emptySet(),
    onEvent: (Event) -> Unit,
    inPicker: Boolean
) {
    val exercises: List<Exercise> by viewModel.exerciseList.observeAsState(listOf())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(items = exercises) { exercise ->
            Box(
                Modifier
                    .padding(vertical = 2.dp)
                    .clickable {
                        if (inPicker) {
                            Log.d("EL", "Exercise clicked: ${exercise.exerciseId}")
                            onEvent(ExerciseEvent.ExerciseSelected(exercise))
                        }
                    }) {
                val selected = selectedExercises.contains(exercise.exerciseId)
                ExerciseCard(exercise, selected, viewModel::onEvent)
            }
        }
    }
}