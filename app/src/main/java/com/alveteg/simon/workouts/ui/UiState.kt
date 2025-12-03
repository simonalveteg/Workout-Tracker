package com.alveteg.simon.workouts.ui

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.alveteg.simon.workouts.db.entities.Exercise
import com.alveteg.simon.workouts.db.entities.GymSet
import com.alveteg.simon.workouts.db.entities.Rpe
import com.alveteg.simon.workouts.db.entities.Session
import com.alveteg.simon.workouts.db.entities.SessionExercise

data class SessionWrapper(
  val session: Session,
  val muscleGroups: List<String>
)

data class SetWrapper(
  val set: GymSet,
  val exerciseWrapper: ExerciseWrapper
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

data class OldDatabaseModel(
  val sessions: List<Session>,
  val exercises: List<Exercise>,
  val sessionExercises: List<SessionExercise>,
  val sets: List<OldGymSet>
)

data class OldGymSet(
  val setId: Long = 0L,
  val parentSessionExerciseId: Long,
  val reps: Int? = null,
  val weight: Float? = null,
  val time: Long? = null,
  val distance: Float? = null,
  val setType: String? = "Normal"
)