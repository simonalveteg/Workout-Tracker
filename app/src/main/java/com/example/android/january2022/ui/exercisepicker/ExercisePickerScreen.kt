package com.example.android.january2022.ui.exercisepicker

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.android.january2022.ui.exercisepicker.components.EquipmentSheet
import com.example.android.january2022.ui.exercisepicker.components.ExerciseCard
import com.example.android.january2022.ui.exercisepicker.components.MuscleSheet
import com.example.android.january2022.ui.modalbottomsheet.ModalBottomSheetLayout
import com.example.android.january2022.ui.modalbottomsheet.ModalBottomSheetValue
import com.example.android.january2022.ui.modalbottomsheet.rememberModalBottomSheetState
import com.example.android.january2022.ui.theme.onlyTop
import com.example.android.january2022.utils.UiEvent
import kotlinx.coroutines.launch

@OptIn(
  ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
  ExperimentalFoundationApi::class
)
@Composable
fun ExercisePickerScreen(
  navController: NavController,
  viewModel: PickerViewModel = hiltViewModel()
) {
  val exercises by viewModel.filteredExercises.collectAsState(initial = emptyList())
  val selectedExercises by viewModel.selectedExercises.collectAsState()
  val muscleFilter by viewModel.muscleFilter.collectAsState()
  val equipmentFilter by viewModel.equipmentFilter.collectAsState()
  val filterSelected by viewModel.filterSelected.collectAsState()
  val filterUsed by viewModel.filterUsed.collectAsState()
  val searchText by viewModel.searchText.collectAsState()

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
    sheetShape = MaterialTheme.shapes.large.onlyTop()
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
    ) { paddingValues ->
      var active by rememberSaveable { mutableStateOf(false) }
      SearchBar(
        modifier = Modifier
          .fillMaxWidth(),
        query = searchText,
        onQueryChange = { viewModel.onEvent(PickerEvent.SearchChanged(it)) },
        onSearch = { active = false },
        active = active,
        onActiveChange = { active = it },
        placeholder = { Text("Search exercises") },
        leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
        trailingIcon = { Icon(Icons.Rounded.MoreVert, contentDescription = null) },
        tonalElevation = 0.dp
      ) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(MaterialTheme.colorScheme.background),
        ) {
          LazyColumn(
            modifier = Modifier
              .fillMaxSize()
              .padding(horizontal = 8.dp)
          ) {
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
          Surface(
            modifier = Modifier
              .align(Alignment.BottomCenter),
            tonalElevation = 2.dp
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp, bottom = 0.dp)
                .navigationBarsPadding(),
              horizontalArrangement = Arrangement.SpaceEvenly
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
                    if (sheetState.isVisible) sheetState.hide() else {
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
      }
      LazyRow(
        modifier = Modifier.padding(top = 110.dp)
      ) {
        items(5) {
          Surface(
            onClick = { /*TODO*/ }, modifier = Modifier
              .size(200.dp)
              .padding(8.dp)
          ) {

          }
        }
      }
    }
  }
}