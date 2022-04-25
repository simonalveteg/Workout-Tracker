package com.example.android.january2022.ui.exercises.picker

import android.text.Layout
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.january2022.ui.exercises.ExerciseEvent
import com.example.android.january2022.ui.exercises.ExerciseViewModel
import com.example.android.january2022.ui.theme.Shapes
import com.example.android.january2022.utils.UiEvent

@Composable
fun MusclePickerScreen(
    onPopBackStack: () -> Unit,
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: ExerciseViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.PopBackStack -> onPopBackStack()
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }
    val muscleGroups = viewModel.muscleGroups
    Column(
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
    ) {
        muscleGroups.forEach { muscleGroup ->
            Surface(
                shape = Shapes.medium,
                tonalElevation = 1.dp,
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 4.dp)
            ) {
                Text(
                    text = muscleGroup,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .align(CenterHorizontally)
                        .clickable {
                            viewModel.onEvent(ExerciseEvent.OnMuscleGroupSelected(muscleGroup))
                        }
                )
            }
        }
    }
}