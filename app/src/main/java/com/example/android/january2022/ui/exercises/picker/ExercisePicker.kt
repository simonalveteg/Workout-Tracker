package com.example.android.january2022.ui.exercises.picker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.january2022.ui.exercises.ExerciseEvent
import com.example.android.january2022.ui.exercises.ExerciseViewModel
import com.example.android.january2022.ui.exercises.ExercisesList
import com.example.android.january2022.utils.UiEvent
import kotlinx.coroutines.flow.collect


@Composable
fun ExercisePickerScreen(
    onPopBackStack: () -> Unit,
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: ExerciseViewModel = hiltViewModel()
) {
    var searchString by remember { mutableStateOf("") }
    val selectedExercises by viewModel.selectedExercises.collectAsState(initial = emptySet())

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.PopBackStack -> onPopBackStack()
                is UiEvent.Navigate -> onNavigate(event)
            }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp, horizontal = 8.dp)
    ) {
        TitleText("CHOOSE EXERCISE",32)
        Box(Modifier.weight(1f)) {
            ExercisesList(viewModel, selectedExercises, viewModel::onEvent, true)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = searchString,
                onValueChange = { newText ->
                    searchString = newText
                    viewModel.onEvent(
                        ExerciseEvent.FilterExerciseList(newText)
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                placeholder = { Text(text = "Filter list") },
                modifier = Modifier
                    .padding(4.dp)
                    .weight(1f)
            )
            AnimatedVisibility(visible = selectedExercises.isNotEmpty()) {
                Button(
                    onClick = { viewModel.onEvent(ExerciseEvent.AddExercisesToSession) },
                    modifier = Modifier.height(56.dp)
                ) {
                    Text("ADD ${selectedExercises.size}")
                }
            }
        }
    }
}

@Composable
fun TitleText(text: String, bottomPadding: Int = 0) {
    Text(
        text = text,
        color = MaterialTheme.colors.primary,
        style = MaterialTheme.typography.h3,
        modifier = Modifier.padding(bottom = bottomPadding.dp)
    )
}

@Composable
fun SubTitleText(text: String, bottomPadding: Int = 0, indent: Int = 0) {
    Text(
        text = text,
        color = MaterialTheme.colors.primaryVariant,
        style = MaterialTheme.typography.h6,
        modifier = Modifier.padding(bottom = bottomPadding.dp, start = indent.dp)
    )
}
