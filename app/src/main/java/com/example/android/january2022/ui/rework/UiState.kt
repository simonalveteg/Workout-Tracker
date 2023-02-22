package com.example.android.january2022.ui.rework

import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.db.entities.SessionExercise
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

data class HomeState(
  val sessions: Flow<List<SessionWrapper>>
)

data class SessionState(
  val currentSession: SessionWrapper,
  val expandedExercise: ExerciseWrapper?,
  val selectedExercises: List<ExerciseWrapper>
)

data class PickerState(
  val exercises: Flow<List<Exercise>>,
  val selectedExercises: List<Exercise>,
  val equipmentFilter: List<String>,
  val muscleFilter: List<String>,
  val filterUsed: Boolean,
  val filterSelected: Boolean,
  val searchText: String
)

data class SessionWrapper(
  val session: Session,
  val exercises: StateFlow<List<ExerciseWrapper>>,
  val muscleGroups: StateFlow<List<String>>
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

data class DatabaseModel(
  val sessions: List<Session>,
  val exercises: List<Exercise>,
  val sessionExercises: List<SessionExercise>,
  val sets: List<GymSet>
)