package com.example.android.january2022.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cloud.pablos.overload.ui.navigation.WorkoutsRoute
import com.example.android.january2022.db.GymRepository
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.ui.SessionWrapper
import com.example.android.january2022.utils.Event
import com.example.android.january2022.utils.UiEvent
import com.example.android.january2022.utils.sortedListOfMuscleGroups
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
    private val repo: GymRepository,
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
                sendUiEvent(UiEvent.Navigate("${WorkoutsRoute.SESSION}/${event.sessionWrapper.session.sessionId}"))
            }
            is HomeEvent.OpenSettings -> {
                sendUiEvent(UiEvent.Navigate(WorkoutsRoute.SETTINGS))
            }
            is HomeEvent.NewSession -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        repo.insertSession(Session())
                        val session = repo.getLastSession()
                        sendUiEvent(UiEvent.Navigate("${WorkoutsRoute.SESSION}/${session.sessionId}"))
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
