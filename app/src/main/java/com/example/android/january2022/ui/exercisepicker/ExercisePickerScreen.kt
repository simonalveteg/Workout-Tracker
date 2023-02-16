package com.example.android.january2022.ui.exercisepicker

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.january2022.ui.modalbottomsheet.ModalBottomSheetLayout
import com.example.android.january2022.ui.modalbottomsheet.ModalBottomSheetValue
import com.example.android.january2022.ui.modalbottomsheet.rememberModalBottomSheetState
import com.example.android.january2022.ui.rework.MainViewModel
import com.example.android.january2022.utils.UiEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ExercisePickerScreen(
  onNavigate: (UiEvent.Navigate) -> Unit,
  viewModel: MainViewModel = hiltViewModel()
) {

  val uiState = viewModel.pickerState.collectAsState()
  val exercises by uiState.value.exercises.collectAsState(initial = emptyList())
  val selectedExercises = uiState.value.selectedExercises
  val muscleFilter = uiState.value.muscleFilter
  val equipmentFilter = uiState.value.equipmentFilter
  val filterSelected = uiState.value.filterSelected
  val filterUsed = uiState.value.filterUsed

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

  val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
  val coroutineScope = rememberCoroutineScope()

  ModalBottomSheetLayout(
    sheetContent = {
      Text(
        "HEJHEJ", modifier = Modifier
          .height(400.dp)
          .fillMaxWidth()
          .padding(50.dp), textAlign = TextAlign.Center
      )
    },
    sheetState = sheetState,
    sheetShape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp)
  ) {
    Scaffold(
      topBar = {
        Surface(
          shape = CutCornerShape(0.dp),
          tonalElevation = 2.dp
        ) {
          Column(

          ) {
            Spacer(Modifier.height(40.dp))
            TextField(
              value = "",
              label = {
                Text(
                  text = "search for exercise",
                  textAlign = TextAlign.Center,
                  modifier = Modifier.fillMaxWidth()
                )
              },
              onValueChange = {},
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, start = 8.dp, end = 8.dp)
                .align(Alignment.CenterHorizontally),
              colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
              ),
              shape = RoundedCornerShape(8.dp),
              textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
            )
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp, bottom = 0.dp),
              horizontalArrangement = Arrangement.End
            ) {
              FilterChip(
                selected = filterSelected,
                onClick = { viewModel.onEvent(PickerEvent.FilterSelected) },
                label = {
                  Text(text = "Selected")
                }
              )
              Spacer(Modifier.width(8.dp))
              FilterChip(
                selected = filterUsed,
                onClick = { viewModel.onEvent(PickerEvent.FilterUsed) },
                label = {
                  Text(text = "Used")
                }
              )
              Spacer(Modifier.width(8.dp))
              FilterChip(
                selected = muscleFilter.isNotEmpty(),
                onClick = {
                  coroutineScope.launch {
                    if (sheetState.isVisible) sheetState.hide() else sheetState.expand()
                  }
                },
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
                selected = equipmentFilter.isNotEmpty(),
                onClick = {
                  coroutineScope.launch {
                    if (sheetState.isVisible) sheetState.hide() else sheetState.show()
                  }
                },
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
        }
      },
    ) { paddingValues ->
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 8.dp)
      ) {
        item {
          Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding() + 8.dp))
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
  }
}