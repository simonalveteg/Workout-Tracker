package com.example.android.january2022.ui.exercisepicker

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.ui.rework.MainViewModel
import com.example.android.january2022.utils.UiEvent

@Composable
fun ExercisePickerScreen(
  onNavigate: (UiEvent.Navigate) -> Unit,
  viewModel: MainViewModel = hiltViewModel()
) {

  val uiState = viewModel.pickerState.collectAsState()
  val exercises by uiState.value.exercises.collectAsState(initial = emptyList())

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 8.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(45.dp))
    }
    items(exercises) { exercise ->
      ExerciseCard(exercise = exercise, onEvent = viewModel::onEvent) {
        viewModel.onEvent(PickerEvent.ExerciseSelected(exercise))
      }
    }
  }

}