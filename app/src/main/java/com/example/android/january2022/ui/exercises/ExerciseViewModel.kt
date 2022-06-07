package com.example.android.january2022.ui.exercises

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android.january2022.db.Equipment
import com.example.android.january2022.db.GymRepository
import com.example.android.january2022.db.MuscleGroup
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.db.entities.SessionExercise
import com.example.android.january2022.ui.session.SessionEvent
import com.example.android.january2022.utils.Event
import com.example.android.january2022.utils.Routes
import com.example.android.january2022.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

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
    var equipment: List<String> = listOf(
        Equipment.BARBELL,
        Equipment.MACHINE,
        Equipment.DUMBBELLS,
        Equipment.KETTLEBELLS,
        Equipment.STRETCHES,
        Equipment.BODYWEIGHT
    ).sorted()

    var selectedMuscleGroup = MutableStateFlow<String>("")
        private set

    var selectedEquipment = MutableStateFlow("")
        private set

    var currentQuery = MutableStateFlow("")
        private set

    var allExercises: LiveData<List<Exercise>> = repository.getAllExercises()
        private set

    var exerciseList = MutableStateFlow<List<Exercise>>(emptyList())
        private set

    var selectedExercises = MutableStateFlow<Set<Exercise>>(emptySet())
        private set

    init {
        Log.d("EVM","initialisation")
        val sessionId = savedStateHandle.get<Long>("sessionId") ?: -1L
        Log.d("EVM","sessionId: $sessionId")
        if (sessionId != -1L) {
            viewModelScope.launch {
                currentSession = withContext(Dispatchers.IO) {
                    repository.getSession(sessionId)
                }
            }
        }
        updateExerciseList()
    }

    fun onEvent(event: Event) {
        when (event) {
            is ExerciseEvent.NewExerciseClicked -> {
                viewModelScope.launch {
                    // TODO: Remove or implement functionality. "add exercise to db"
                }
            }
            is ExerciseEvent.ExerciseSelected -> {
                val selectedExercise = event.exercise
                viewModelScope.launch {
                    if (selectedExercises.value.contains(selectedExercise)) {
                        selectedExercises.value =
                            selectedExercises.value.filter { it != selectedExercise }.toSet()
                    } else {
                        selectedExercises.value += selectedExercise
                    }
                }
            }
            is ExerciseEvent.AddExercisesToSession -> {
                viewModelScope.launch {
                    selectedExercises.collect {
                        it.forEach { exercise ->
                            val newSessionExercise = SessionExercise(
                                parentSessionId = currentSession?.sessionId ?: -1L,
                                parentExerciseId = exercise.id
                            )
                            repository.insertSessionExercise(newSessionExercise)
                        }
                    }
                }
                sendUiEvent(UiEvent.PopBackStack)
            }
            is ExerciseEvent.ExerciseInfoClicked -> {
                val exerciseId = event.exercise.id
                sendUiEvent(UiEvent.Navigate(Routes.EXERCISE_DETAIL_SCREEN + "?exerciseId=${exerciseId}"))
            }
            is ExerciseEvent.ToggleSearch -> {
                currentQuery.value = ""
                selectedEquipment.value = ""
                selectedMuscleGroup.value = ""
            }
            is ExerciseEvent.FilterExerciseList -> {
                currentQuery.value = event.searchString
                updateExerciseList()
            }
            is ExerciseEvent.MuscleGroupSelectionChange -> {
                val muscleGroup = event.muscleGroup
                viewModelScope.launch {
                    // update selected muscle groups
                    selectedMuscleGroup.value = muscleGroup
                }
                updateExerciseList()
            }
            is ExerciseEvent.EquipmentSelectionChange -> {
                val equipment = event.equipment
                viewModelScope.launch {
                    // update selected equipment
                    selectedEquipment.value = if(selectedEquipment.value == equipment) "" else equipment
                }
                updateExerciseList()
            }
            is ExerciseEvent.OnMuscleGroupSelected -> {
                // update exercise list
                onEvent(ExerciseEvent.MuscleGroupSelectionChange(event.muscleGroup))
                // navigate to exercise picker
                sendUiEvent(UiEvent.Navigate(Routes.EXERCISE_PICKER_SCREEN))
            }
            is ExerciseEvent.OnCreateExercise -> {
                Log.d("SVM","Create Exercise: ${event.exercise}")
                viewModelScope.launch {
                    repository.insertExercise(event.exercise)
                }
            }
        }
    }

    private fun updateExerciseList() {
        viewModelScope.launch {
            exerciseList.value = emptyList()
            repository.getExercisesByQuery(
                muscleGroup = selectedMuscleGroup.value,
                equipment = selectedEquipment.value,
                query = currentQuery.value
            ).collect {
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