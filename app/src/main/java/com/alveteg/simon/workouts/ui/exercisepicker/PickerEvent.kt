package com.alveteg.simon.workouts.ui.exercisepicker

import com.alveteg.simon.workouts.db.entities.Exercise
import com.alveteg.simon.workouts.utils.Event

sealed class PickerEvent : Event {
  data class ExerciseSelected(val exercise: Exercise) : PickerEvent()
  data class OpenGuide(val exercise: Exercise) : PickerEvent()
  object FilterSelected : PickerEvent()
  object FilterUsed : PickerEvent()
  data class SelectMuscle(val muscle: String) : PickerEvent()
  object DeselectMuscles : PickerEvent()
  data class SelectEquipment(val equipment: String) : PickerEvent()
  object DeselectEquipment : PickerEvent()
  object AddExercises : PickerEvent()
  data class SearchChanged(val text: String) : PickerEvent()
}
