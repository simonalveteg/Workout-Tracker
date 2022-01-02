package com.example.android.january2022.ui.theme.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.android.january2022.HomeViewModel
import com.example.android.january2022.db.entities.Exercise


@Composable
fun ExercisePickerScreen(viewModel: HomeViewModel, navController: NavController) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "CHOOSE EXERCISE",
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.h3,
            modifier =
            Modifier.padding(bottom = 32.dp)
        )
        Box(Modifier.weight(1f)) {
            ExercisesList(viewModel, navController, true)
        }
    }
}

@Composable
fun ExercisesList(viewModel: HomeViewModel, navController: NavController, inPicker: Boolean) {
    val exercises: List<Exercise> by viewModel.exerciseList.observeAsState(listOf())
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(items = exercises) { exercise ->
            Box(Modifier.clickable {
                if (inPicker) {
                    viewModel.onExerciseClicked(exercise)
                    navController.navigate("session") {
                        popUpTo("session") { inclusive = true }
                    }
                }
            }) {
                ExerciseCard(exercise)
            }
        }
    }
}

@Composable
fun ExercisesScreen(viewModel: HomeViewModel, navController: NavController) {
    // remember inputValue
    var inputValue by remember { mutableStateOf("") }

    Surface(
        Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(Modifier.weight(1f)) {
                ExercisesList(viewModel, navController, false)
            }
            Row(
                Modifier
                    .padding(vertical = 16.dp)
                    .height(TextFieldDefaults.MinHeight)
            ) {
                TextField(
                    value = inputValue,
                    onValueChange = { newText ->
                        inputValue = newText
                    },
                    placeholder = { Text(text = "Enter exercise-name") }
                )
                Button(
                    onClick = {
                        viewModel.onNewExercise(inputValue)
                        inputValue = ""
                    },
                    Modifier
                        .padding(start = 8.dp)
                        .fillMaxHeight()
                ) {
                    Text(text = "Submit")
                }
            }
        }
    }
}

@Composable
fun ExerciseCard(exercise: Exercise) {
    Card(Modifier.padding(vertical = 2.dp, horizontal = 16.dp)) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            Text(text = "${exercise.exerciseId} ${exercise.exerciseTitle}")
        }
    }
}
