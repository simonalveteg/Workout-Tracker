package com.example.android.january2022.ui

import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.db.entities.SessionExercise

data class SessionWrapper(
  val session: Session,
  val muscleGroups: List<String>
)

data class ExerciseWrapper(
  val sessionExercise: SessionExercise,
  val exercise: Exercise,
  val sets: List<GymSet>
)

data class TimerState(
  val time: Long,
  val running: Boolean,
  val maxTime: Long
)

data class DatabaseModel(
  val sessions: List<Session>,
  val exercises: List<Exercise>,
  val sessionExercises: List<SessionExercise>,
  val sets: List<GymSet>
)