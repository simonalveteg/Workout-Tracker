package com.example.android.january2022.ui.exercises.picker

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.january2022.ui.session.MuscleItem
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.ui.exercises.ExerciseEvent
import com.example.android.january2022.ui.exercises.ExerciseViewModel
import com.example.android.january2022.utils.BackPressHandler
import com.example.android.january2022.utils.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusclePickerScreen(
    onPopBackStack: () -> Unit,
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: ExerciseViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.PopBackStack -> onPopBackStack()
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }
    val muscleGroups = viewModel.muscleGroups
    val exercises: List<Exercise> by viewModel.exerciseList.collectAsState(listOf())
    val selectedExercises by viewModel.selectedExercises.collectAsState(initial = emptySet())
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
    }
    val searchOpen = rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(searchOpen.value) {
        viewModel.onEvent(ExerciseEvent.ToggleSearch)
        Log.d("MP","search toggled")
    }

    val inputValue = rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    // used by searchfield to request focus
    val requester = FocusRequester()

    BackPressHandler(onBackPressed = {
        if(searchOpen.value) searchOpen.value = false else {
            onPopBackStack()
        }
    })
    Scaffold(
        topBar = {
            val topBarBackgroundColor = Color.Transparent
            if (searchOpen.value) {
                LaunchedEffect(Unit) {
                    requester.requestFocus()
                }
                SmallTopAppBar(
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = topBarBackgroundColor
                    ),
                    title = {
                        TextField(
                            value = inputValue.value,
                            onValueChange = {
                                viewModel.onEvent(ExerciseEvent.FilterExerciseList(it))
                                inputValue.value = it
                            },
                            modifier = Modifier.focusRequester(requester),
                            textStyle = MaterialTheme.typography.bodyLarge,
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = topBarBackgroundColor,
                                focusedIndicatorColor = topBarBackgroundColor,
                                unfocusedIndicatorColor = topBarBackgroundColor
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    focusManager.clearFocus()
                                }
                            ),
                            placeholder = {
                                Text(
                                    text = "Search for an exercise",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Light,
                                    textAlign = TextAlign.Center
                                )
                            }
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                searchOpen.value = false
                                focusManager.clearFocus()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { inputValue.value = "" }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            } else {
                SmallTopAppBar(

                    title = { Text(text = "Exercises") },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(),
                    actions = {
                        IconButton(onClick = {
                            searchOpen.value = !searchOpen.value
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        },
        floatingActionButton = {
            val offset = if (selectedExercises.isEmpty()) 100.dp else 0.dp
            ExtendedFloatingActionButton(
                onClick = { viewModel.onEvent(ExerciseEvent.AddExercisesToSession) },
                shape = RoundedCornerShape(35),
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.offset(offset)
            ) { Text("ADD ${selectedExercises.size}") }
        },
        floatingActionButtonPosition = FabPosition.End,
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(bottom = 60.dp)
    ) { innerPadding ->

        AnimatedVisibility(
            visible = searchOpen.value,
            enter = fadeIn() + slideInVertically { it },
            exit = fadeOut() + slideOutVertically { it / 2 }
        ) {
            ExerciseListWithFilter(
                viewModel = viewModel,
                innerPadding = innerPadding,
                exercises = exercises,
                selectedExercises = selectedExercises
            )
        }
        AnimatedVisibility(
            visible = !searchOpen.value,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(150.dp),
                userScrollEnabled = true,
                verticalArrangement = Arrangement.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(
                        top = innerPadding.calculateTopPadding()
                    )
            ) {
                items(
                    count = muscleGroups.size,
                ) { index ->
                    val muscleGroup = muscleGroups[index]
                    MuscleItem(
                        muscleGroup = muscleGroup,
                        selectedExercises = selectedExercises
                    ) { viewModel.onEvent(ExerciseEvent.OnMuscleGroupSelected(muscleGroup)) }
                }
            }
        }
    }
}