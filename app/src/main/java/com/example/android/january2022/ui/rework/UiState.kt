package com.example.android.january2022.ui.rework

import androidx.lifecycle.LiveData
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.db.entities.SessionExercise
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

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

data class TimerState(
  val time: MutableStateFlow<Long>,
  val isRunning: MutableStateFlow<Boolean>,
  val maxTime: MutableStateFlow<Long>
)