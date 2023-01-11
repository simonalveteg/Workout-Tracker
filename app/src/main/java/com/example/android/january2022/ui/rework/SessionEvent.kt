package com.example.android.january2022.ui.rework

import com.example.android.january2022.utils.Event

sealed class SessionEvent : Event {
  data class ExerciseSelection(val exercise: ExerciseWrapper) : SessionEvent()
}