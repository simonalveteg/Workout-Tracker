package com.example.android.january2022.ui.exercises

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android.january2022.db.GymRepository
import com.example.android.january2022.db.MuscleGroup
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.db.entities.SessionExercise
import com.example.android.january2022.db.entities.SessionExerciseWithExercise
import com.example.android.january2022.utils.Event
import com.example.android.january2022.utils.Routes
import com.example.android.january2022.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.spec.MGF1ParameterSpec
import javax.inject.Inject
import kotlin.reflect.KType

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val repository: GymRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var currentSession by mutableStateOf<Session?>(null)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var muscleGroups: List<String> = listOf(
        MuscleGroup.BICEPS,
        MuscleGroup.CHEST,
        MuscleGroup.QUADS,
        MuscleGroup.TRAPS,
        MuscleGroup.TRICEPS,
        MuscleGroup.SHOULDERS,
        MuscleGroup.LATS,
        MuscleGroup.HAMSTRINGS,
        MuscleGroup.GLUTES,
        MuscleGroup.FOREARMS,
        MuscleGroup.CALVES,
        MuscleGroup.ABDOMINALS,
        MuscleGroup.LOWER_BACK,
        MuscleGroup.TRAPS_MID_BACK
    ).sorted()

    var selectedMuscleGroups = MutableStateFlow<List<String>>(emptyList())
        private set

    var allExercises: LiveData<List<Exercise>> = repository.getAllExercises()
        private set

    var exerciseList = MutableStateFlow<List<Exercise>>(emptyList())
        private set

    var selectedExercises = MutableStateFlow<Set<Long>>(emptySet())
        private set

    init {
        val sessionId = savedStateHandle.get<Long>("sessionId") ?: -1L
        if (sessionId != -1L) {
            viewModelScope.launch {
                currentSession = withContext(Dispatchers.IO) {
                    repository.getSession(sessionId)
                }
            }
        }
    }


    fun onEvent(event: Event) {
        when (event) {
            is ExerciseEvent.NewExerciseClicked -> {
                viewModelScope.launch {
                    // TODO: Remove or implement functionality. "add exercise to db"
                }
            }
            is ExerciseEvent.ExerciseSelected -> {
                val id = event.exercise.exerciseId
                viewModelScope.launch {
                    if (selectedExercises.value.contains(id)) {
                        selectedExercises.value =
                            selectedExercises.value.filter { it != id }.toSet()
                    } else {
                        selectedExercises.value += id
                    }
                }
            }
            is ExerciseEvent.AddExercisesToSession -> {
                viewModelScope.launch {
                    selectedExercises.collect {
                        it.forEach { exerciseId ->
                            val newSessionExercise = SessionExercise(
                                parentSessionId = currentSession?.sessionId ?: -1L,
                                parentExerciseId = exerciseId
                            )
                            repository.insertSessionExercise(newSessionExercise)
                        }
                    }
                }
                sendUiEvent(UiEvent.PopBackStack)
            }
            is ExerciseEvent.ExerciseInfoClicked -> {
                val exerciseId = event.exercise.exerciseId
                sendUiEvent(UiEvent.Navigate(Routes.EXERCISE_DETAIL_SCREEN + "?exerciseId=${exerciseId}"))
            }
            is ExerciseEvent.FilterExerciseList -> {
                // TODO: remove?
            }
            is ExerciseEvent.MuscleGroupSelectionChange -> {
                val muscleGroup = event.muscleGroup
                viewModelScope.launch {
                    if (selectedMuscleGroups.value.contains(muscleGroup)) {
                        selectedMuscleGroups.value =
                            selectedMuscleGroups.value.filter { it != muscleGroup }.toList()
                    } else {
                        selectedMuscleGroups.value = listOf(muscleGroup)
                    }
                }
                updateExerciseList()
            }
        }
    }

    private fun updateExerciseList() {
        viewModelScope.launch {
            exerciseList.value = emptyList()
            repository.getExercisesByQuery(selectedMuscleGroups.value).collect {
                exerciseList.value += it
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

}