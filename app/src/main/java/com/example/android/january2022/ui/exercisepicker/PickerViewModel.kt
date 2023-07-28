package com.example.android.january2022.ui.exercisepicker

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.january2022.db.GymRepository
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.SessionExercise
import com.example.android.january2022.utils.Event
import com.example.android.january2022.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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

  private val _filterUsed = MutableStateFlow(false)
  val filterUsed = _filterUsed.asStateFlow()

  private val _searchText = MutableStateFlow("")
  val searchText = _searchText.asStateFlow()

  val filteredExercises: Flow<List<Exercise>> = combine(
    repo.getAllExercises(),
    selectedExercises,
    equipmentFilter,
    muscleFilter,
    filterSelected,
    filterUsed,
    searchText
  ) { exercises, selectedExercises, equipmentFilter, muscleFilter, selected, used, text ->
    exercises.filter { exercise ->
      val muscleCondition =
        (muscleFilter.isEmpty() || exercise.getMuscleGroups().any { muscleFilter.contains(it) })
      val equipmentCondition =
        (equipmentFilter.isEmpty() || exercise.equipment.any { equipmentFilter.contains(it) })
      val selectedCondition = (!selected || selectedExercises.contains(exercise))

      muscleCondition && equipmentCondition && selectedCondition && exercise.getStringMatch(text)
    }.sortedBy { exercise ->
      if (text.isNotBlank()) {
        exercise.title.length
      } else {
        exercise.title.first().code
      }
    }
  }

  fun onEvent(event: Event) {
    when (event) {
      is PickerEvent.OpenGuide -> openGuide(event.exercise)
      is PickerEvent.ExerciseSelected -> {
        _selectedExercises.value = buildList {
          if (_selectedExercises.value.contains(event.exercise)) {
            addAll(_selectedExercises.value.minusElement(event.exercise))
          } else {
            addAll(_selectedExercises.value)
            add(event.exercise)
          }
        }
      }
      is PickerEvent.FilterSelected -> {
        _filterSelected.value = !_filterSelected.value
      }
      is PickerEvent.FilterUsed -> {
        _filterUsed.value = !_filterUsed.value
      }
      is PickerEvent.SelectMuscle -> {
        _muscleFilter.value = if (_muscleFilter.value.contains(event.muscle)) {
          _muscleFilter.value.minus(event.muscle)
        } else {
          _muscleFilter.value.plus(event.muscle)
        }
      }
      is PickerEvent.DeselectMuscles -> {
        _muscleFilter.value = emptyList()
      }
      is PickerEvent.SelectEquipment -> {
        _equipmentFilter.value = if (_equipmentFilter.value.contains(event.equipment)) {
          _equipmentFilter.value.minus(event.equipment)
        } else {
          _equipmentFilter.value.plus(event.equipment)
        }
      }
      is PickerEvent.DeselectEquipment -> {
        _equipmentFilter.value = emptyList()
      }
      is PickerEvent.AddExercises -> {
        viewModelScope.launch {
          _selectedExercises.value.forEach { exercise ->
            savedStateHandle.get<Long>("session_id")?.let { sessionId ->
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
      is PickerEvent.SearchChanged -> {
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

inline fun <T1, T2, T3, T4, T5, T6, T7, R> combine(
  flow: Flow<T1>,
  flow2: Flow<T2>,
  flow3: Flow<T3>,
  flow4: Flow<T4>,
  flow5: Flow<T5>,
  flow6: Flow<T6>,
  flow7: Flow<T7>,
  crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7) -> R
): Flow<R> {
  return combine(
    flow,
    flow2,
    flow3,
    flow4,
    flow5,
    flow6,
    flow7
  ) { args: Array<*> ->
    @Suppress("UNCHECKED_CAST")
    transform(
      args[0] as T1,
      args[1] as T2,
      args[2] as T3,
      args[3] as T4,
      args[4] as T5,
      args[5] as T6,
      args[6] as T7,
    )
  }
}