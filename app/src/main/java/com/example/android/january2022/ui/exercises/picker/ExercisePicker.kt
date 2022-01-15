package com.example.android.january2022.ui.exercises.picker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.android.january2022.ui.exercises.ExerciseViewModel
import com.example.android.january2022.ui.exercises.ExercisesList
import com.example.android.january2022.ui.home.HomeViewModel
import com.example.android.january2022.utils.UiEvent
import kotlinx.coroutines.flow.collect


@Composable
fun ExercisePickerScreen(
    onPopBackStack: () -> Unit,
    viewModel: ExerciseViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.PopBackStack -> onPopBackStack()
            }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TitleText("CHOOSE EXERCISE",32)
        Box(Modifier.weight(1f)) {
            ExercisesList(viewModel, viewModel::onEvent, true)
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
