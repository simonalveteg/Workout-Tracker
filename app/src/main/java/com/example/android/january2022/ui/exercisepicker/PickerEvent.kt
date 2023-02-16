package com.example.android.january2022.ui.exercisepicker

import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.utils.Event

sealed class PickerEvent : Event {
  data class ExerciseSelected(val exercise: Exercise) : PickerEvent()
  data class OpenGuide(val exercise: Exercise) : PickerEvent()
  object FilterSelected : PickerEvent()
  object FilterUsed : PickerEvent()
}
