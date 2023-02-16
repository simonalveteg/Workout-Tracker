package com.example.android.january2022.ui.exercisepicker

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.ui.rework.MainViewModel
import com.example.android.january2022.utils.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercisePickerScreen(
  onNavigate: (UiEvent.Navigate) -> Unit,
  viewModel: MainViewModel = hiltViewModel()
) {

  val uiState = viewModel.pickerState.collectAsState()
  val exercises by uiState.value.exercises.collectAsState(initial = emptyList())
  val selectedExercises = uiState.value.selectedExercises
  val uriHandler = LocalUriHandler.current

  LaunchedEffect(true) {
    viewModel.uiEvent.collect { event ->
      when (event) {
        is UiEvent.OpenWebsite -> {
          uriHandler.openUri(event.url)
        }
        else -> Unit
      }
    }
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 8.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(45.dp))
    }
    item {
      Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.End
      ) {
        FilterChip(
          selected = false,
          onClick = { /*TODO*/ },
          label = {
            Icon(
              imageVector = Icons.Default.AccessibilityNew,
              contentDescription = "Equipment",
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(18.dp)
            )
                  },
          trailingIcon = {
            Icon(
              imageVector = Icons.Default.ArrowDropDown,
              contentDescription = "Dropdown",
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(22.dp)
            )
          }
        )
        Spacer(Modifier.width(8.dp))
        FilterChip(
          selected = false,
          onClick = { /*TODO*/ },
          label = {
            Icon(
              imageVector = Icons.Default.FitnessCenter,
              contentDescription = "Equipment",
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(18.dp)
            )
          },
          trailingIcon = {
            Icon(
              imageVector = Icons.Default.ArrowDropDown,
              contentDescription = "Dropdown",
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(22.dp)
            )
          }
        )
      }
    }
    items(exercises) { exercise ->
      ExerciseCard(
        exercise = exercise,
        selected = selectedExercises.contains(exercise),
        onEvent = viewModel::onEvent
      ) {
        viewModel.onEvent(PickerEvent.ExerciseSelected(exercise))
      }
    }
  }

}