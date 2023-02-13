package com.example.android.january2022.ui.rework

import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.db.entities.SessionExercise
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

data class HomeState(
  val sessions: Flow<List<Session>>
)

data class SessionState(
  val currentSession: SessionWrapper,
  val selectedExercise: ExerciseWrapper?
)

data class SessionWrapper(
  val session: Session,
  val exercises: Flow<List<ExerciseWrapper>>,
  val muscleGroups: Flow<List<String>>
)

data class ExerciseWrapper(
  val sessionExercise: SessionExercise,
  val exercise: Exercise,
  val sets: Flow<List<GymSet>>
)

data class TimerState(
  val time: MutableStateFlow<Long>,
  val isRunning: MutableStateFlow<Boolean>,
  val maxTime: MutableStateFlow<Long>,
  val finishedEvent: Channel<Boolean>
)