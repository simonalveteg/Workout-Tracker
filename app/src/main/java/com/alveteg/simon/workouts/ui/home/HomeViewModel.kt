package com.alveteg.simon.workouts.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alveteg.simon.workouts.db.GymRepository
import com.alveteg.simon.workouts.db.entities.Session
import com.alveteg.simon.workouts.ui.SessionWrapper
import com.alveteg.simon.workouts.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val repo: GymRepository
) : ViewModel() {

  val sessions = combine(repo.getAllSessionExercises(), repo.getAllSessions()) { sewes, sessions ->
    sessions.map { session ->
      val muscleGroups = sewes.filter { it.sessionExercise.parentSessionId == session.sessionId }
        .sortedListOfMuscleGroups()
      SessionWrapper(session, muscleGroups)
    }
  }

  private val _uiEvent = Channel<UiEvent>()
  val uiEvent = _uiEvent.receiveAsFlow()

  fun onEvent(event: Event) {
    when (event) {
      is HomeEvent.SessionClicked -> {
        sendUiEvent(UiEvent.Navigate("${Routes.SESSION}/${event.sessionWrapper.session.sessionId}"))
      }
      is HomeEvent.OpenSettings -> {
        sendUiEvent(UiEvent.Navigate(Routes.SETTINGS))
      }
      is HomeEvent.NewSession -> {
        viewModelScope.launch {
          withContext(Dispatchers.IO) {
            repo.insertSession(Session())
            val session = repo.getLastSession()
            sendUiEvent(UiEvent.Navigate("${Routes.SESSION}/${session.sessionId}"))
          }
        }
      }
      else -> Unit
    }
  }

  private fun sendUiEvent(event: UiEvent) {
    viewModelScope.launch {
      _uiEvent.send(event)
    }
  }
}