package com.example.android.january2022.ui.exercises.picker

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.ui.exercises.ExerciseEvent
import com.example.android.january2022.ui.exercises.ExerciseViewModel
import com.example.android.january2022.ui.exercises.ExercisesList
import com.example.android.january2022.utils.Event
import com.example.android.january2022.utils.UiEvent
import kotlinx.coroutines.flow.collect


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExercisePickerScreen(
    onPopBackStack: () -> Unit,
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: ExerciseViewModel = hiltViewModel()
) {
    val exercises: List<Exercise> by viewModel.exerciseList.collectAsState(listOf())
    val selectedExercises by viewModel.selectedExercises.collectAsState(initial = emptySet())

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.PopBackStack -> onPopBackStack()
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            // TODO: Animate fab entering and exiting screen when items get selected.
            ExtendedFloatingActionButton(
                onClick = { viewModel.onEvent(ExerciseEvent.AddExercisesToSession) },
                shape = RoundedCornerShape(50),
                backgroundColor = MaterialTheme.colors.primary,
                text = { Text("ADD ${selectedExercises.size}") }
            )
        },
        isFloatingActionButtonDocked = false,
        floatingActionButtonPosition = FabPosition.End
    ) {
        Column(
            Modifier
                .fillMaxSize()
        ) {
            TitleText("CHOOSE EXERCISE", 32)
            ExerciseSearchFilters(viewModel, viewModel::onEvent)
            Box(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)) {
                ExercisesList(viewModel, exercises, selectedExercises, viewModel::onEvent, true)
            }
        }
    }
}

@Composable
fun ExerciseSearchFilters(viewModel: ExerciseViewModel, onEvent: (Event) -> Unit) {
    val selectedMuscleGroups by viewModel.selectedMuscleGroups.collectAsState(emptyList())
    val muscleGroups by remember { mutableStateOf(viewModel.muscleGroups) }.also { list ->
        list.value.filter { !selectedMuscleGroups.contains(it) }
    }

    LazyRow(contentPadding = PaddingValues(4.dp)) {
        items(muscleGroups) { muscleGroup ->
            val isSelected = selectedMuscleGroups.contains(muscleGroup)
            MuscleChip(
                muscleGroup = muscleGroup,
                isSelected = isSelected,
                onEvent = viewModel::onEvent
            )
        }
    }
}

@Composable
fun MuscleChip(
    muscleGroup: String,
    isSelected: Boolean,
    onEvent: (Event) -> Unit
) {
    val chipColor = animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.surface)
    Surface(
        shape = RoundedCornerShape(50),
        color = chipColor.value,
        elevation = 1.dp
    ) {
        Box(modifier = Modifier.toggleable(value = isSelected, onValueChange = {
            onEvent(
                ExerciseEvent.MuscleGroupSelectionChange(muscleGroup)
            )
        })) {
            Text(
                text = muscleGroup,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(8.dp)
            )
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
