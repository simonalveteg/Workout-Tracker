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
      selectedExercise = null
    )
  )

  val uiState: StateFlow<UiState> = _uiState.asStateFlow()

  init {
    Timber.d("Initializing ViewModel")
    viewModelScope.launch {
      withContext(Dispatchers.IO) {
        _uiState.value.sessions.collectLatest { sessions ->
          Timber.d("Updating uiState")
          val session = sessions.lastOrNull()
          session?.let {
            _uiState.update { state ->
              state.copy(
                currentSession = SessionWrapper(
                  session = it,
                  exercises = repo.getExercisesForSession(it)
                )
              )
            }
          }
        }
      }
    }
  }

  fun onEvent(event: Event) {
    Timber.d("Received event: $event")
    when (event) {
      is SessionEvent.ExerciseSelection -> {
        event.exercise.sessionExercise.let { se ->
          _uiState.update {
            it.copy(
              selectedExercise = if (se != it.selectedExercise) se else null
            )
          }
        }
      }
      is SessionEvent.SetChanged -> {
        viewModelScope.launch {
          withContext(Dispatchers.IO) {
            repo.updateSet(event.updatedSet)
          }
        }
      }
      is SessionEvent.SetCreated -> {
        viewModelScope.launch {
          withContext(Dispatchers.IO) {
            repo.createSet(event.sessionExercise)
          }
        }
      }
    }
  }

  data class UiState(
    val sessions: Flow<List<Session>>,
    val currentSession: SessionWrapper,
    val selectedExercise: SessionExercise?
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