package com.alveteg.simon.workouts.ui

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.alveteg.simon.workouts.db.entities.Exercise
import com.alveteg.simon.workouts.db.entities.GymSet
import com.alveteg.simon.workouts.db.entities.Rpe
import com.alveteg.simon.workouts.db.entities.Session
import com.alveteg.simon.workouts.db.entities.SessionExercise
import kotlinx.parcelize.Parcelize

data class SessionWrapper(
  val session: Session,
  val muscleGroups: List<String>
)

@Parcelize
data class SetWrapper(
  val set: GymSet,
  val exerciseWrapper: ExerciseWrapper
) : Parcelable

@Parcelize
data class ExerciseWrapper(
  val sessionExercise: SessionExercise,
  val exercise: Exercise,
  val sets: List<GymSet>
) : Parcelable

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
