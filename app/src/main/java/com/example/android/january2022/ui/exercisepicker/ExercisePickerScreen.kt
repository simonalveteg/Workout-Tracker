package com.example.android.january2022.ui.exercisepicker

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.android.january2022.ui.MainViewModel
import com.example.android.january2022.ui.exercisepicker.components.EquipmentSheet
import com.example.android.january2022.ui.exercisepicker.components.ExerciseCard
import com.example.android.january2022.ui.exercisepicker.components.MuscleSheet
import com.example.android.january2022.ui.modalbottomsheet.ModalBottomSheetLayout
import com.example.android.january2022.ui.modalbottomsheet.ModalBottomSheetValue
import com.example.android.january2022.ui.modalbottomsheet.rememberModalBottomSheetState
import com.example.android.january2022.utils.UiEvent
import com.example.android.january2022.utils.clearFocusOnKeyboardDismiss
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class,
  ExperimentalComposeUiApi::class
)
@Composable
fun ExercisePickerScreen(
  navController: NavController,
  viewModel: MainViewModel = hiltViewModel()
) {

  val uiState = viewModel.pickerState.collectAsState()
  val exercises by viewModel.getFilteredExercises().collectAsState(initial = emptyList())
  val selectedExercises = uiState.value.selectedExercises
  val muscleFilter = uiState.value.muscleFilter
  val equipmentFilter = uiState.value.equipmentFilter
  val filterSelected = uiState.value.filterSelected
  val filterUsed = uiState.value.filterUsed

  val controller = LocalSoftwareKeyboardController.current
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
  val equipmentBottomsheet = remember { mutableStateOf(false) }
  val filterColors = FilterChipDefaults.filterChipColors(
    selectedContainerColor = MaterialTheme.colorScheme.primary,
    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
    selectedTrailingIconColor = MaterialTheme.colorScheme.onPrimary,
    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
    iconColor = MaterialTheme.colorScheme.onSurfaceVariant
  )

  ModalBottomSheetLayout(
    sheetContent = {
      if (equipmentBottomsheet.value) {
        EquipmentSheet(equipmentFilter, viewModel::onEvent)
      } else {
        MuscleSheet(muscleFilter, viewModel::onEvent)
      }
    },
    sheetState = sheetState,
    sheetShape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp)
  ) {
    Scaffold(
      floatingActionButton = {
        Box(
          modifier = Modifier
            .height(64.dp)
            .width(80.dp)
        ) {
          AnimatedVisibility(
            visible = selectedExercises.isNotEmpty(),
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
          ) {
            FloatingActionButton(
              onClick = {
                viewModel.onEvent(PickerEvent.AddExercises)
                navController.popBackStack()
              },
              containerColor = MaterialTheme.colorScheme.primary,
              modifier = Modifier.align(Alignment.BottomEnd)
            ) {
              Text(
                text = "ADD ${selectedExercises.size}",
                modifier = Modifier
                  .padding(vertical = 4.dp, horizontal = 10.dp)
                  .fillMaxWidth(),
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center
              )
            }
          }
        }
      },
      topBar = {
        Surface(
          shape = CutCornerShape(0.dp),
          tonalElevation = 2.dp
        ) {
          Column {
            Spacer(Modifier.height(40.dp))
            TextField(
              value = uiState.value.searchText,
              label = {
                Text(
                  text = "search for exercise",
                  textAlign = TextAlign.Center,
                  modifier = Modifier.fillMaxWidth()
                )
              },
              onValueChange = { viewModel.onEvent(PickerEvent.SearchChanged(it)) },
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, start = 8.dp, end = 8.dp)
                .clearFocusOnKeyboardDismiss()
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
                },
                colors = filterColors
              )
              Spacer(Modifier.width(8.dp))
              FilterChip(
                selected = filterUsed,
                onClick = { viewModel.onEvent(PickerEvent.FilterUsed) },
                label = {
                  Text(text = "Used")
                },
                colors = filterColors
              )
              Spacer(Modifier.width(8.dp))
              FilterChip(
                selected = muscleFilter.isNotEmpty(),
                onClick = {
                  equipmentBottomsheet.value = false
                  coroutineScope.launch {
                    if (sheetState.isVisible) sheetState.hide() else{
                      controller?.hide()
                      sheetState.expand()
                    }
                  }
                },
                label = {
                  Icon(
                    imageVector = Icons.Default.AccessibilityNew,
                    contentDescription = "Equipment",
                    modifier = Modifier.size(18.dp)
                  )
                },
                trailingIcon = {
                  Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown",
                    modifier = Modifier.size(22.dp)
                  )
                },
                colors = filterColors
              )
              Spacer(Modifier.width(8.dp))
              FilterChip(
                selected = equipmentFilter.isNotEmpty(),
                onClick = {
                  equipmentBottomsheet.value = true
                  coroutineScope.launch {
                    if (sheetState.isVisible) sheetState.hide() else {
                      controller?.hide()
                      sheetState.show()
                    }
                  }
                },
                label = {
                  Icon(
                    imageVector = Icons.Default.FitnessCenter,
                    contentDescription = "Equipment",
                    modifier = Modifier.size(18.dp)
                  )
                },
                trailingIcon = {
                  Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown",
                    modifier = Modifier.size(22.dp)
                  )
                },
                colors = filterColors
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