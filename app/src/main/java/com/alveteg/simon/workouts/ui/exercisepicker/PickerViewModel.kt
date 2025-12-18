package com.alveteg.simon.workouts.ui.exercisepicker

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alveteg.simon.workouts.db.GymRepository
import com.alveteg.simon.workouts.db.entities.Exercise
import com.alveteg.simon.workouts.db.entities.ExerciseWithSessionCount
import com.alveteg.simon.workouts.db.entities.SessionExercise
import com.alveteg.simon.workouts.utils.Event
import com.alveteg.simon.workouts.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PickerViewModel @Inject constructor(
  private val repo: GymRepository,
  private val savedStateHandle: SavedStateHandle
) : ViewModel() {

  private val _selectedExercises = MutableStateFlow<List<Exercise>>(emptyList())
  val selectedExercises = _selectedExercises.asStateFlow()

  private val _equipmentFilter = MutableStateFlow<List<String>>(emptyList())
  val equipmentFilter = _equipmentFilter.asStateFlow()

  private val _muscleFilter = MutableStateFlow<List<String>>(emptyList())
  val muscleFilter = _muscleFilter.asStateFlow()

  private val _filterSelected = MutableStateFlow(false)
  val filterSelected = _filterSelected.asStateFlow()


  private val _searchText = MutableStateFlow("")
  val searchText = _searchText.asStateFlow()

  val filteredExercises: Flow<List<ExerciseWithSessionCount>> = combine(
    repo.getAllExercisesWithSessionCount(),
    selectedExercises,
    equipmentFilter,
    muscleFilter,
    filterSelected,
    searchText
  ) { exercises, selectedExercises, equipmentFilter, muscleFilter, selected, searchText ->
    exercises.filter { exerciseWithSessionCount ->
      val exercise = exerciseWithSessionCount.exercise
      val muscleCondition =
        (muscleFilter.isEmpty() || exercise.getPrimaryMuscleGroups()
          .any { muscleFilter.contains(it) })
      val equipmentCondition =
        (equipmentFilter.isEmpty() || exercise.equipment.any { equipmentFilter.contains(it) })

      if (selected) selectedExercises.contains(exercise)
      else muscleCondition && equipmentCondition && exercise.getStringMatch(searchText)

    }.sortedWith(compareByDescending<ExerciseWithSessionCount> { it.sessionCount }
      .thenBy { exerciseWithSessionCount ->
        val exercise = exerciseWithSessionCount.exercise
        if (searchText.isNotBlank()) {
          exercise.title.length
        } else {
          exercise.title
        }
      }
    )
  }

  suspend fun getHistoryForExercise(exercise: Exercise) = repo.getHistoryForExercise(exercise)

  fun onEvent(event: Event) {
    when (event) {
      is PickerEvent.OpenGuide -> openGuide(event.exercise)
      is PickerEvent.ToggleSelectExercise -> {
        val currentList = _selectedExercises.value
        val exercise = event.exercise
        _selectedExercises.value = if (currentList.contains(exercise)) {
          currentList - exercise
        } else {
          currentList + exercise
        }
        if (_selectedExercises.value.isEmpty()) {
          _filterSelected.value = false
        }
      }

      is PickerEvent.FilterSelected -> {
        _filterSelected.value = !_filterSelected.value
      }

      is PickerEvent.ToggleSelectMuscle -> {
        _muscleFilter.value = if (_muscleFilter.value.contains(event.muscle)) {
          _muscleFilter.value.minus(event.muscle)
        } else {
          _muscleFilter.value.plus(event.muscle)
        }
      }

      is PickerEvent.DeselectFilters -> {
        _muscleFilter.value = emptyList()
        _equipmentFilter.value = emptyList()
      }

      is PickerEvent.ToggleSelectEquipment -> {
        _equipmentFilter.value = if (_equipmentFilter.value.contains(event.equipment)) {
          _equipmentFilter.value.minus(event.equipment)
        } else {
          _equipmentFilter.value.plus(event.equipment)
        }
      }

      is PickerEvent.AddExercises -> {
        viewModelScope.launch {
          _selectedExercises.value.forEach { exercise ->
            savedStateHandle.get<Long>("session_id")?.let { sessionId ->
              withContext(Dispatchers .IO) {
                repo.insertSessionExercise(
                  SessionExercise(
                    parentSessionId = sessionId,
                    parentExerciseId = exercise.id
                  )
                )
              }
            }
          }
        }
      }

      is PickerEvent.UpdateSearchText -> {
        _searchText.value = event.text
      }
    }
  }

  private val _uiEvent = Channel<UiEvent>()
  val uiEvent = _uiEvent.receiveAsFlow()

  private fun openGuide(exercise: Exercise) {
    sendUiEvent(UiEvent.OpenWebsite(url = "https://duckduckgo.com/?q=! exrx ${exercise.title}"))
  }

  private fun sendUiEvent(event: UiEvent) {
    viewModelScope.launch {
      _uiEvent.send(event)
    }
  }
}

inline fun <T1, T2, T3, T4, T5, T6, R> combine(
  flow: Flow<T1>,
  flow2: Flow<T2>,
  flow3: Flow<T3>,
  flow4: Flow<T4>,
  flow5: Flow<T5>,
  flow6: Flow<T6>,
  crossinline transform: suspend (T1, T2, T3, T4, T5, T6) -> R
): Flow<R> {
  return combine(
    flow,
    flow2,
    flow3,
    flow4,
    flow5,
    flow6,
  ) { args: Array<*> ->
    @Suppress("UNCHECKED_CAST")
    transform(
      args[0] as T1,
      args[1] as T2,
      args[2] as T3,
      args[3] as T4,
      args[4] as T5,
      args[5] as T6,
    )
  }
}