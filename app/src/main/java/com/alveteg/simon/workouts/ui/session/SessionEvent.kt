package com.alveteg.simon.workouts.ui.session

import com.alveteg.simon.workouts.db.entities.GymSet
import com.alveteg.simon.workouts.ui.ExerciseWrapper
import com.alveteg.simon.workouts.utils.Event
import java.time.LocalTime

sealed class SessionEvent : Event {
  data class ChangeSet(val updatedSet: GymSet) : SessionEvent()
  data class CreateSet(val sessionExercise: ExerciseWrapper) : SessionEvent()
  data class DeleteSet(val set: GymSet) : SessionEvent()
  data class OpenGuide(val exercise: ExerciseWrapper) : SessionEvent()
  data class ReorderExercises(val from: Int, val to: Int) : SessionEvent()
  data class RemoveExercise(val exercise: ExerciseWrapper) : SessionEvent()
  object RemoveSession : SessionEvent()
  object TimerToggled : SessionEvent()
  object TimerReset : SessionEvent()
  object TimerIncreased : SessionEvent()
  object TimerDecreased : SessionEvent()

  object AddExercise : SessionEvent()

  data class SetStartTime(val newTime: LocalTime) : SessionEvent()
  data class SetEndTime(val newTime: LocalTime) : SessionEvent()
}