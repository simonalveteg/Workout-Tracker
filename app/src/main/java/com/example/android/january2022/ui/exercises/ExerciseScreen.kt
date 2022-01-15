package com.example.android.january2022.ui.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.android.january2022.ui.home.HomeViewModel
import com.example.android.january2022.db.entities.Exercise


@Composable
fun ExercisesScreen(
    viewModel: ExerciseViewModel = hiltViewModel()
) {
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
                ExercisesList(viewModel, viewModel::onEvent, false)
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
                        viewModel.onEvent(ExerciseEvent.NewExerciseClicked(inputValue))
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


