package com.alveteg.simon.workouts.ui

import com.alveteg.simon.workouts.db.entities.Exercise
import com.alveteg.simon.workouts.db.entities.GymSet
import com.alveteg.simon.workouts.db.entities.Session
import com.alveteg.simon.workouts.db.entities.SessionExercise

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