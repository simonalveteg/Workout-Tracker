package com.alveteg.simon.workouts.ui.session

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alveteg.simon.workouts.db.GymRepository
import com.alveteg.simon.workouts.db.entities.Exercise
import com.alveteg.simon.workouts.db.entities.GymSet
import com.alveteg.simon.workouts.db.entities.Session
import com.alveteg.simon.workouts.ui.ExerciseWrapper
import com.alveteg.simon.workouts.ui.SessionWrapper
import com.alveteg.simon.workouts.ui.SetWrapper
import com.alveteg.simon.workouts.utils.Event
import com.alveteg.simon.workouts.utils.Routes
import com.alveteg.simon.workouts.utils.UiEvent
import com.alveteg.simon.workouts.utils.sortedListOfMuscleGroups
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
  private val repo: GymRepository, savedStateHandle: SavedStateHandle
) : ViewModel() {

  val _session = MutableStateFlow(Session())
  val session = _session.asStateFlow().map {
    SessionWrapper(it, emptyList())
  }
  private val _exercises = MutableStateFlow<List<ExerciseWrapper>>(emptyList())
  val exercises = _exercises.asStateFlow()

  val muscleGroups = exercises.map { exercises ->
    exercises.map { it.exercise }.sortedListOfMuscleGroups()
  }

  private val _uiEvent = Channel<UiEvent>()
  val uiEvent = _uiEvent.receiveAsFlow()

  init {
    savedStateHandle.get<Long>("session_id")?.let { sessionId ->
      Timber.d("Session ID: $sessionId")
      viewModelScope.launch {
        withContext(Dispatchers.IO) {
          _session.value = repo.getSessionById(sessionId)

          combine(
            repo.getExercisesForSession(_session), repo.getAllSets()
          ) { exercises, sets ->
            exercises.map { sewe ->
              ExerciseWrapper(
                sessionExercise = sewe.sessionExercise,
                exercise = sewe.exercise,
                sets = sets.filter { set ->
                  set.parentSessionExerciseId == sewe.sessionExercise.sessionExerciseId
                })
            }
          }.collect { exerciseList ->
            _exercises.value = exerciseList
          }
        }
      }
    }
  }

  suspend fun getHistoryForExercise(exercise: Exercise): List<Pair<SessionWrapper, ExerciseWrapper>> {
    return withContext(Dispatchers.IO) {

      val allSessionExercises = repo.getAllSessionExercises().first()
      val relevantSessionExercises = allSessionExercises.filter { it.exercise.id == exercise.id }

      relevantSessionExercises
        .mapNotNull { sessionExercise ->
          repo.getSessionById(sessionExercise.sessionExercise.parentSessionId)?.let { session ->
            val sets =
              repo.getSetsForExercise(sessionExercise.sessionExercise.sessionExerciseId).first()
            val sessionWrapper = SessionWrapper(session, emptyList())
            val exerciseWrapper = ExerciseWrapper(
              sessionExercise = sessionExercise.sessionExercise, exercise = exercise, sets = sets
            )

            sessionWrapper to exerciseWrapper
          }
        }
        .sortedByDescending { it.first.session.start }
    }
  }


  fun onEvent(event: Event) {
    when (event) {
      is SessionEvent.ChangeSet -> {
        viewModelScope.launch {
          withContext(Dispatchers.IO) {
            repo.updateSet(event.updatedSet)
          }
        }
      }

      is SessionEvent.CreateSet -> {
        viewModelScope.launch {
          withContext(Dispatchers.IO) {
            val id = repo.createSet(event.sessionExercise.sessionExercise)
            val gymSet = repo.getSetById(id)
            val setWrapper = SetWrapper(
              set = gymSet, exerciseWrapper = event.sessionExercise
            )
            sendUiEvent(UiEvent.SetCreated(setWrapper))
          }
        }
      }

      is SessionEvent.DeleteSet -> {
        viewModelScope.launch {
          withContext(Dispatchers.IO) {
            repo.deleteSet(event.set)
          }
        }
      }

      is SessionEvent.TimerToggled -> sendUiEvent(UiEvent.ToggleTimer)
      is SessionEvent.TimerReset -> sendUiEvent(UiEvent.ResetTimer)
      is SessionEvent.TimerIncreased -> sendUiEvent(UiEvent.IncrementTimer)
      is SessionEvent.TimerDecreased -> sendUiEvent(UiEvent.DecrementTimer)
      is SessionEvent.OpenGuide -> {
        openGuide(event.exercise.exercise)
      }

      is SessionEvent.AddExercise -> {
        _session.value.sessionId.let { id ->
          sendUiEvent(UiEvent.Navigate("${Routes.EXERCISE_PICKER}/$id"))
        }
      }

      is SessionEvent.RemoveExercise -> {
        viewModelScope.launch {
          repo.removeSessionExercise(event.exercise.sessionExercise)
        }
      }

      is SessionEvent.RemoveSession -> {
        sendUiEvent(UiEvent.Navigate(Routes.HOME, popBackStack = true))
        viewModelScope.launch {
          repo.removeSession(_session.value)
        }
      }

      is SessionEvent.SetEndTime -> {
        val session = _session.value
        val date = session.end?.toLocalDate() ?: session.start.toLocalDate()
        val newEndTime = LocalDateTime.of(date, event.newTime)
        viewModelScope.launch {
          repo.updateSession(
            session.copy(
              end = newEndTime
            )
          )
          withContext(Dispatchers.IO) {
            _session.value = repo.getSessionById(_session.value.sessionId)
          }
        }
      }

      is SessionEvent.SetStartTime -> {
        val session = _session.value
        val newStartTime = LocalDateTime.of(session.start.toLocalDate(), event.newTime)
        viewModelScope.launch {
          repo.updateSession(
            session.copy(
              start = newStartTime
            )
          )
          withContext(Dispatchers.IO) {
            _session.value = repo.getSessionById(_session.value.sessionId)
          }
        }
      }

      is SessionEvent.ReorderExercises -> {
        val currentList = _exercises.value.toMutableList()
        val reorderedItem = currentList.removeAt(event.from)
        currentList.add(event.to, reorderedItem)

        _exercises.value = currentList

        viewModelScope.launch(Dispatchers.IO) {
          val updatedList = currentList.mapIndexed { index, wrapper ->
            wrapper.sessionExercise.copy(exerciseOrder = index)
          }
          repo.updateSessionExercises(updatedList)
        }
      }

      else -> Unit
    }
  }

  private fun openGuide(exercise: Exercise) {
    sendUiEvent(UiEvent.OpenWebsite(url = "https://duckduckgo.com/?q=! exrx ${exercise.title}"))
  }

  private fun sendUiEvent(event: UiEvent) {
    viewModelScope.launch {
      _uiEvent.send(event)
    }
  }
}
