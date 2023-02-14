package com.example.android.january2022.ui.exercisepicker

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
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

  LazyColumn(
    modifier = Modifier.fillMaxSize().padding(vertical = 80.dp, horizontal = 12.dp)
  ) {
    item {
      ExerciseCard(exercise = Exercise(), onEvent = viewModel::onEvent) {

      }
    }
  }

}