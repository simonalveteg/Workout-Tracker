package com.alveteg.simon.workouts.ui.exercisepicker

import com.alveteg.simon.workouts.db.entities.Exercise
import com.alveteg.simon.workouts.utils.Event

sealed class PickerEvent : Event {
  data class OpenGuide(val exercise: Exercise) : PickerEvent()
  data class ToggleSelectExercise(val exercise: Exercise) : PickerEvent()
  data class ToggleSelectMuscle(val muscle: String) : PickerEvent()
  data class ToggleSelectEquipment(val equipment: String) : PickerEvent()
  data class UpdateSearchText(val text: String) : PickerEvent()
  object FilterSelected : PickerEvent()
  object DeselectFilters : PickerEvent()
  object AddExercises : PickerEvent()
}
