package com.example.android.january2022.ui.rework

import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.db.entities.SessionExercise
import kotlinx.coroutines.flow.Flow

data class HomeState(
  val sessions: Flow<List<Session>>
)

data class SessionState(
  val sessions: Flow<List<Session>>,
  val currentSession: SessionWrapper,
  val selectedExercise: SessionExercise?
)

data class SessionWrapper(
  val session: Session,
  val exercises: Flow<List<ExerciseWrapper>>
)

data class ExerciseWrapper(
  val sessionExercise: SessionExercise,
  val exercise: Exercise,
  val sets: Flow<List<GymSet>>
)