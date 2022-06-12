package com.example.android.january2022.ui.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.january2022.db.Equipment
import com.example.android.january2022.db.MuscleGroup
import com.example.android.january2022.db.entities.Exercise


@Composable
fun ExercisesScreen(
    viewModel: ExerciseViewModel = hiltViewModel()
) {
    // remember inputValue
    var inputValue by remember { mutableStateOf("") }
    val exercises: List<Exercise> by viewModel.exerciseList.collectAsState(emptyList())

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(Modifier.weight(1f)) {
                ExercisesList(
                    viewModel = viewModel,
                    exercises = exercises,
                    onEvent = viewModel::onEvent,
                    inPicker = false
                )
            }
        }
    }
}


