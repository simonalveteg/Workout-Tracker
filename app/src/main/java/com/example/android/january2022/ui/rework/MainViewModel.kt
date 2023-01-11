package com.example.android.january2022.ui.rework

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.january2022.db.GymRepository
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.db.entities.SessionExercise
import com.example.android.january2022.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val repo: GymRepository
) : ViewModel() {

  private val _uiState = MutableStateFlow(
    UiState(
      sessions = repo.getAllSessions(),
      currentSession = SessionWrapper(Session(), emptyFlow()),
      selectedExercise = SessionExercise(
        parentExerciseId = 0,
        parentSessionId = 0
      )
    )
  )

  val uiState: StateFlow<UiState> = _uiState.asStateFlow()

  init {
    Timber.d("Initializing ViewModel")
    viewModelScope.launch {
      withContext(Dispatchers.IO) {
        _uiState.value.sessions.collectLatest { sessions ->
          Timber.d("Updating uiState")
          _uiState.update { state ->
            val session = sessions.last()
            state.copy(
              currentSession = SessionWrapper(
                session = session,
                exercises = repo.getExercisesForSession(session)
              )
            )
          }
        }
      }
    }
  }

  fun onEvent(event: Event) {
    Timber.d("Received event: $event")
    when (event) {
      is SessionEvent.ExerciseSelection -> {
        _uiState.update {
          it.copy(
            selectedExercise = event.exercise.sessionExercise
          )
        }
      }
    }
  }

  data class UiState(
    val sessions: Flow<List<Session>>,
    val currentSession: SessionWrapper,
    val selectedExercise: SessionExercise
  )
}

data class SessionWrapper(
  val session: Session,
  val exercises: Flow<List<ExerciseWrapper>>
)

data class ExerciseWrapper(
  val sessionExercise: SessionExercise,
  val exercise: Exercise,
  val sets: Flow<List<GymSet>>
)