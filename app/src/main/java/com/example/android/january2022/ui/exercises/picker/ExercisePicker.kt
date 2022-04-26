package com.example.android.january2022.ui.exercises.picker

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.ui.exercises.ExerciseEvent
import com.example.android.january2022.ui.exercises.ExerciseViewModel
import com.example.android.january2022.ui.exercises.ExercisesList
import com.example.android.january2022.ui.session.SessionEvent
import com.example.android.january2022.utils.Event
import com.example.android.january2022.utils.UiEvent
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ExercisePickerScreen(
    onPopBackStack: () -> Unit,
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: ExerciseViewModel
) {
    val exercises: List<Exercise> by viewModel.exerciseList.collectAsState(listOf())
    val selectedExercises by viewModel.selectedExercises.collectAsState(initial = emptySet())
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
    }
    val currentMuscleGroup by viewModel.selectedMuscleGroup.collectAsState()
    val createExerciseDialogOpen = rememberSaveable { mutableStateOf(false) }


    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.PopBackStack -> onPopBackStack()
                is UiEvent.Navigate -> onNavigate(event)
                is UiEvent.OpenDialog -> createExerciseDialogOpen.value = true
                else -> Unit
            }
        }
    }
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(text = currentMuscleGroup) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(),
                actions = {
                    val dropdownExpanded = remember { mutableStateOf(false) }
                    DropdownMenu(
                        expanded = dropdownExpanded.value,
                        onDismissRequest = { dropdownExpanded.value = false }) {
                        DropdownMenuItem(
                            text = { Text("Create New Exercise") },
                            onClick = {
                                createExerciseDialogOpen.value = true
                                dropdownExpanded.value = false
                            }
                        )

                    }
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Localized description"
                        )
                    }
                    IconButton(onClick = { dropdownExpanded.value = true }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            val alpha by animateFloatAsState(targetValue = if(selectedExercises.isEmpty()) 0f else 1f)
            ExtendedFloatingActionButton(
                onClick = { viewModel.onEvent(ExerciseEvent.AddExercisesToSession) },
                shape = RoundedCornerShape(35),
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.scale(alpha)
            ) { Text("ADD ${selectedExercises.size}") }

        },
        floatingActionButtonPosition = FabPosition.End,
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(bottom = 60.dp)
    ) { innerPadding ->
        AnimatedVisibility(visible = createExerciseDialogOpen.value) {
            Dialog(onDismissRequest = { createExerciseDialogOpen.value = false }) {
                val muscleGroups = viewModel.muscleGroups
                val equipment = viewModel.equipment
                val selectedEquipment = remember { mutableStateOf("") }
                val selectedMuscleGroups = remember { mutableStateListOf("") }
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 48.dp)
                            .padding(8.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        var inputValue by remember { mutableStateOf("") }
                        Text(
                            "Create new exercise",
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        TextField(
                            value = inputValue,
                            onValueChange = { newText ->
                                inputValue = newText
                            },
                            placeholder = { Text(text = "Enter exercise-name") }
                        )
                        FlowRow(
                            mainAxisAlignment = FlowMainAxisAlignment.SpaceAround
                        ) {
                            muscleGroups.forEach { muscleGroup ->
                                val selected = selectedMuscleGroups.contains(muscleGroup)
                                MuscleChip(title = muscleGroup, isSelected = selected) {
                                    if (selected) {
                                        selectedMuscleGroups.remove(it)
                                    } else selectedMuscleGroups.add(it)
                                    Log.d(
                                        "EP",
                                        "Pressed $it, selected: ${selectedMuscleGroups.filter{it.isNotEmpty()}.joinToString( ", ")}"
                                    )
                                }
                            }
                        }
                        Divider()
                        FlowRow(
                            mainAxisAlignment = FlowMainAxisAlignment.SpaceAround
                        ) {
                            equipment.forEach { equipment ->
                                val selected = selectedEquipment.value == equipment
                                MuscleChip(title = equipment, isSelected = selected) {
                                    if (selected) {
                                        selectedEquipment.value = ""
                                    } else selectedEquipment.value = it
                                    Log.d("EP", "Pressed $it, selected: $selectedEquipment")
                                }
                            }
                        }
                        Row {
                            Button(
                                onClick = {
                                    viewModel.onEvent(
                                        ExerciseEvent.OnCreateExercise(
                                            Exercise(
                                                exerciseTitle = inputValue,
                                                muscleGroups = selectedMuscleGroups.filter {
                                                    it.isNotEmpty()
                                                }.joinToString(separator = ", "),
                                                equipment = selectedEquipment.value
                                            )
                                        )
                                    )
                                    createExerciseDialogOpen.value = false
                                }
                            ) {
                                Text("Create")
                            }
                        }
                    }
                }
            }
        }
        ExerciseListWithFilter(
            viewModel = viewModel,
            innerPadding = innerPadding,
            exercises = exercises,
            selectedExercises = selectedExercises
        )
    }
}

@Composable
fun ExerciseListWithFilter(
    viewModel: ExerciseViewModel,
    innerPadding: PaddingValues,
    exercises: List<Exercise>,
    selectedExercises: Set<Exercise> = emptySet()
) {
    Column {
        ExerciseEquipmentFilter(viewModel, viewModel::onEvent, innerPadding)
        ExercisesList(
            viewModel,
            exercises,
            selectedExercises,
            viewModel::onEvent,
            true
        )
    }
}

@Composable
fun ExerciseEquipmentFilter(
    viewModel: ExerciseViewModel,
    onEvent: (Event) -> Unit,
    innerPadding: PaddingValues
) {
    val selectedEquipment by viewModel.selectedEquipment.collectAsState("")
    val equipment by remember { mutableStateOf(viewModel.equipment) }
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
        ) {
            items(equipment) { equipment ->
                val isSelected = selectedEquipment.contains(equipment)
                MuscleChip(
                    title = equipment,
                    isSelected = isSelected,
                    onToggle = {
                        onEvent(ExerciseEvent.EquipmentSelectionChange(it))
                    }
                )
            }
        }
    }
}

@Composable
fun ExerciseMuscleGroupFilters(viewModel: ExerciseViewModel, onEvent: (Event) -> Unit) {
    val selectedMuscleGroup by viewModel.selectedMuscleGroup.collectAsState()
    val muscleGroups by remember { mutableStateOf(viewModel.muscleGroups) }
    LazyRow(contentPadding = PaddingValues(4.dp)) {
        items(muscleGroups) { muscleGroup ->
            val isSelected = selectedMuscleGroup.contains(muscleGroup)
            MuscleChip(
                title = muscleGroup,
                isSelected = isSelected,
                onToggle = {
                    onEvent(ExerciseEvent.MuscleGroupSelectionChange(it))
                }
            )
        }
    }
}

@Composable
fun MuscleChip(
    title: String,
    isSelected: Boolean,
    onToggle: (String) -> Unit
) {
    val chipColor = animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
    )
    Surface(
        shape = RoundedCornerShape(0),
        color = chipColor.value,
    ) {
        Box(modifier = Modifier.toggleable(value = isSelected, onValueChange = {
            onToggle(title)
        })) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun TitleText(text: String, bottomPadding: Int = 0, startPadding: Int = 0) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.displayMedium,
        modifier = Modifier.padding(bottom = bottomPadding.dp, start = startPadding.dp)
    )
}

@Composable
fun SubTitleText(text: String, bottomPadding: Int = 0, indent: Int = 0) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(bottom = bottomPadding.dp, start = indent.dp)
    )
}
