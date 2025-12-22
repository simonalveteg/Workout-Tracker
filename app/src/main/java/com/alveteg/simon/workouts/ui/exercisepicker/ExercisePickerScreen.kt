package com.alveteg.simon.workouts.ui.exercisepicker

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalFloatingToolbar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.alveteg.simon.workouts.db.Equipment
import com.alveteg.simon.workouts.db.MuscleGroup
import com.alveteg.simon.workouts.db.entities.Exercise
import com.alveteg.simon.workouts.ui.exercisepicker.components.ExerciseCard
import com.alveteg.simon.workouts.ui.exercisepicker.components.FilterSection
import com.alveteg.simon.workouts.ui.session.components.ExerciseBottomSheet
import com.alveteg.simon.workouts.utils.ScaleVisibility
import com.alveteg.simon.workouts.utils.UiEvent

@OptIn(
  ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
  ExperimentalMaterial3ExpressiveApi::class, ExperimentalSharedTransitionApi::class
)
@Composable
fun ExercisePickerScreen(
  navController: NavController,
  sharedTransitionScope: SharedTransitionScope,
  animatedVisibilityScope: AnimatedVisibilityScope,
  viewModel: PickerViewModel = hiltViewModel()
) {
  val exercises by viewModel.filteredExercises.collectAsState()
  val selectedExercises by viewModel.selectedExercises.collectAsState()
  val muscleFilter by viewModel.muscleFilter.collectAsState()
  val equipmentFilter by viewModel.equipmentFilter.collectAsState()
  val filterSelected by viewModel.filterSelected.collectAsState()
  val searchText by viewModel.searchText.collectAsState()

  val controller = LocalSoftwareKeyboardController.current
  val uriHandler = LocalUriHandler.current
  val filterActive = equipmentFilter.isNotEmpty() || muscleFilter.isNotEmpty()

  LaunchedEffect(Unit) {
    viewModel.uiEvent.collect { event ->
      when (event) {
        is UiEvent.OpenWebsite -> {
          uriHandler.openUri(event.url)
        }

        else -> Unit
      }
    }
  }

  var openFilterSheet by rememberSaveable { mutableStateOf(false) }
  var openExerciseBottomSheet by rememberSaveable { mutableStateOf<Exercise?>(null) }
  var skipPartiallyExpanded by rememberSaveable { mutableStateOf(false) }
  val filterSheetState =
    rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)
  val exerciseBottomSheetState =
    rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)

  if (openFilterSheet) {
    ModalBottomSheet(
      sheetState = filterSheetState,
      onDismissRequest = { openFilterSheet = false },
      dragHandle = {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .height(50.dp)
        ) {
          Text(
            text = "Filter Exercises",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
              .align(Alignment.Center)
          )
          IconButton(
            enabled = filterActive,
            onClick = {
              viewModel.onEvent(PickerEvent.DeselectFilters)
            },
            modifier = Modifier
              .align(Alignment.CenterEnd)
              .padding(end = 8.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Refresh,
              contentDescription = "Clear filter selection"
            )
          }
        }
      }
    ) {
      Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(top = 8.dp)
      ) {
        FilterSection(
          title = "Muscle Group",
          filterOptions = MuscleGroup.getAllMuscleGroups().sorted(),
          onFilterClicked = { viewModel.onEvent(PickerEvent.ToggleSelectMuscle(it)) },
          selectedFilterOptions = muscleFilter
        )
        FilterSection(
          title = "Equipment",
          filterOptions = Equipment.getAllEquipment().sorted(),
          onFilterClicked = { viewModel.onEvent(PickerEvent.ToggleSelectEquipment(it)) },
          selectedFilterOptions = equipmentFilter
        )
      }
    }
  }

  if (openExerciseBottomSheet != null) {
    val exercise = remember(exercises, openExerciseBottomSheet) {
      exercises.find { it.exercise.id == openExerciseBottomSheet?.id }!!
    }
    ExerciseBottomSheet(
      exercise = exercise.exercise,
      onDismissRequest = { openExerciseBottomSheet = null },
      getSetHistory = viewModel::getHistoryForExercise,
      onEvent = viewModel::onEvent,
      sheetState = exerciseBottomSheetState,
    )
  }

  val lazyListState = rememberLazyListState()
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  val searchBarFocusRequester = remember { FocusRequester() }


  LaunchedEffect(muscleFilter, equipmentFilter, searchText, filterSelected) {
    lazyListState.scrollToItem(0)
  }

  with(sharedTransitionScope) {
    Scaffold(
      modifier = Modifier
        .sharedBounds(
          sharedContentState = sharedTransitionScope.rememberSharedContentState(key = "exercisePicker"),
          animatedVisibilityScope = animatedVisibilityScope,
          clipInOverlayDuringTransition = OverlayClip(
            clipShape = MaterialTheme.shapes.large
          )
        )
        .nestedScroll(scrollBehavior.nestedScrollConnection),
      topBar = {
        TopAppBar(
          title = {
            SearchBar(
              inputField = {
                SearchBarDefaults.InputField(
                  query = searchText,
                  onQueryChange = {
                    viewModel.onEvent(PickerEvent.UpdateSearchText(it))
                  },
                  onSearch = {
                    controller?.hide()
                  },
                  expanded = true,
                  onExpandedChange = { },
                  placeholder = { Text(text = "Search exercises") },
                  leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                  trailingIcon = {
                    ScaleVisibility(visible = searchText.isNotEmpty()) {
                      IconButton(
                        onClick = {
                          viewModel.onEvent(PickerEvent.UpdateSearchText(""))
                        }) {
                        Icon(
                          imageVector = Icons.Default.Clear,
                          contentDescription = "Clear search bar."
                        )
                      }
                    }
                  },
                  modifier = Modifier.focusRequester(searchBarFocusRequester)
                )
              },
              expanded = false,
              onExpandedChange = { },
              modifier = Modifier.padding(bottom = 8.dp),
            ) { }
          },
          contentPadding = PaddingValues(horizontal = 8.dp),
          scrollBehavior = scrollBehavior
        )
      },
      floatingActionButton = {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          VerticalFloatingToolbar(
            modifier = Modifier
              .clip(FloatingToolbarDefaults.ContainerShape)
              .animateContentSize(
                alignment = Alignment.BottomCenter
              )
              .zIndex(1f),
            expanded = scrollBehavior.state.collapsedFraction < 1f,
            leadingContent = {
              ScaleVisibility(visible = selectedExercises.isNotEmpty()) {
                val containerColor by animateColorAsState(
                  targetValue =
                    if (filterSelected) MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.surfaceContainer
                )
                val contentColor by animateColorAsState(
                  targetValue =
                    if (filterSelected) MaterialTheme.colorScheme.onSecondaryContainer
                    else MaterialTheme.colorScheme.onSurface
                )
                FilledTonalIconButton(
                  onClick = {
                    viewModel.onEvent(PickerEvent.FilterSelected)
                  },
                  colors = IconButtonDefaults.filledTonalIconButtonColors(
                    contentColor = contentColor,
                    containerColor = containerColor
                  )
                ) {
                  Text(
                    text = selectedExercises.size.toString()
                  )
                }
              }
              val showHighlight = filterActive && !filterSelected
              IconButton(
                colors = IconButtonDefaults.iconButtonColors(
                  containerColor = if (showHighlight) MaterialTheme.colorScheme.secondaryContainer else {
                    Color.Transparent
                  },
                  contentColor = if (showHighlight) MaterialTheme.colorScheme.onSecondaryContainer else {
                    MaterialTheme.colorScheme.onSurface
                  }
                ),
                onClick = { openFilterSheet = true },
              ) {
                Icon(Icons.Filled.FilterList, contentDescription = "Filter results.")
              }
              IconButton(
                onClick = {
                  searchBarFocusRequester.requestFocus()
                  controller?.show()
                },
              ) {
                Icon(Icons.Filled.Search, contentDescription = "Focus search bar.")
              }
            }
          ) {
            FilledIconButton(
              onClick = {
                viewModel.onEvent(PickerEvent.AddExercises)
                navController.popBackStack()
              },
            ) {
              Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Add selected exercises to session."
              )
            }
          }
        }
      },
    ) { innerPadding ->
      LazyColumn(
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 12.dp),
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
      ) {
        items(exercises, key = { it.exercise.id }) {
          val selected = selectedExercises.contains(it.exercise)
          ExerciseCard(
            exerciseWithSessionCount = it,
            selected = selected,
            modifier = Modifier
              .animateItem(),
            onLongClick = { openExerciseBottomSheet = it.exercise }
          ) {
            viewModel.onEvent(PickerEvent.ToggleSelectExercise(it.exercise))
          }
        }
      }
    }
  }
}
