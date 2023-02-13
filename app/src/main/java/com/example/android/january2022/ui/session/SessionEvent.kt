package com.example.android.january2022.ui.session

import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.SessionExercise
import com.example.android.january2022.ui.rework.ExerciseWrapper
import com.example.android.january2022.utils.Event

sealed class SessionEvent : Event {
  data class ExerciseSelection(val exercise: ExerciseWrapper) : SessionEvent()
  data class SetChanged(val updatedSet: GymSet) : SessionEvent()
  data class SetCreated(val sessionExercise: ExerciseWrapper) : SessionEvent()

  object TimerToggled : SessionEvent()
  object TimerReset : SessionEvent()
  object TimerIncreased : SessionEvent()
  object TimerDecreased : SessionEvent()

  object OpenGuide : SessionEvent()
}