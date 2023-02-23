package com.example.android.january2022.ui.session

import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.ui.rework.ExerciseWrapper
import com.example.android.january2022.utils.Event
import java.time.LocalDateTime
import java.time.LocalTime

sealed class SessionEvent : Event {
  data class ExerciseExpanded(val exercise: ExerciseWrapper) : SessionEvent()
  data class ExerciseSelected(val exercise: ExerciseWrapper) : SessionEvent()
  data class SetChanged(val updatedSet: GymSet) : SessionEvent()
  data class SetCreated(val sessionExercise: ExerciseWrapper) : SessionEvent()

  object RemoveSelectedExercises : SessionEvent()
  object RemoveSession : SessionEvent()
  object DeselectExercises : SessionEvent()

  object TimerToggled : SessionEvent()
  object TimerReset : SessionEvent()
  object TimerIncreased : SessionEvent()
  object TimerDecreased : SessionEvent()

  object OpenGuide : SessionEvent()
  object AddExercise : SessionEvent()

  data class StartTimeChanged(val newTime: LocalTime) : SessionEvent()
  data class EndTimeChanged(val newTime: LocalTime) : SessionEvent()
}