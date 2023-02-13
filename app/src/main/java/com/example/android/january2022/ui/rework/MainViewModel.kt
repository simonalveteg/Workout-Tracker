package com.example.android.january2022.ui.rework

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.january2022.db.GymRepository
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.ui.home.HomeEvent
import com.example.android.january2022.ui.session.SessionEvent
import com.example.android.january2022.utils.Event
import com.example.android.january2022.utils.Routes
import com.example.android.january2022.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val repo: GymRepository,
  private val workoutTimer: WorkoutTimer
) : ViewModel() {


  private val _uiEvent = Channel<UiEvent>()
  val uiEvent = _uiEvent.receiveAsFlow()

  private val _homeState = MutableStateFlow(
    HomeState(
      sessions = repo.getAllSessions().map {
        it.map { session ->
          SessionWrapper(
            session = session,
            exercises = repo.getExercisesForSession(session),
            muscleGroups = repo.getMuscleGroupsForSession(session)
          )
        }
      }
    )
  )
  val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

  private val _sessionState = MutableStateFlow(
    SessionState(
      currentSession = SessionWrapper(Session(), emptyFlow(), emptyFlow()),
      selectedExercise = null
    )
  )
  val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

  private val _timerState = MutableStateFlow(
    TimerState(
      time = workoutTimer.time,
      isRunning = workoutTimer.isRunning,
      maxTime = workoutTimer.maxTime,
      finishedEvent = workoutTimer.finished
    )
  )
  val timerState = _timerState.asStateFlow()

  init {
    Timber.d("Initializing ViewModel")
    viewModelScope.launch {
      withContext(Dispatchers.IO) {
        _homeState.value.sessions.collectLatest { sessions ->
          Timber.d("Updating uiState")
          val session = sessions.lastOrNull()
          session?.let {
            _sessionState.update { state ->
              state.copy(
                currentSession = it
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
      is HomeEvent.SessionClicked -> {
        _sessionState.update {
          it.copy(
            currentSession = event.sessionWrapper
          )
        }
        sendUiEvent(UiEvent.Navigate(Routes.SESSION))
      }
      is SessionEvent.ExerciseSelection -> {
        event.exercise.let { se ->
          _sessionState.update {
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
            repo.createSet(event.sessionExercise.sessionExercise)
          }
        }
      }
      is SessionEvent.TimerToggled -> workoutTimer.toggle()
      is SessionEvent.TimerReset -> workoutTimer.reset()
      is SessionEvent.TimerIncreased -> workoutTimer.increment()
      is SessionEvent.TimerDecreased -> workoutTimer.decrement()
      is SessionEvent.OpenGuide -> {
        sessionState.value.selectedExercise?.exercise?.title?.let {
          sendUiEvent(UiEvent.OpenWebsite(url = "https://duckduckgo.com/?q=! exrx $it"))
        }
      }
    }
  }

  private fun sendUiEvent(event: UiEvent) {
    viewModelScope.launch {
      _uiEvent.send(event)
    }
  }
}