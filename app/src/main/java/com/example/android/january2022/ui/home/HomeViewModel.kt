package com.example.android.january2022.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import com.example.android.january2022.db.GymRepository
import com.example.android.january2022.db.entities.*
import com.example.android.january2022.ui.session.SessionEvent
import com.example.android.january2022.utils.Event
import com.example.android.january2022.utils.Routes
import com.example.android.january2022.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: GymRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val sessionId = MutableLiveData<Long>()

    var selectedSession by mutableStateOf(-1L)
        private set

    private val _removedSession = MutableLiveData<Session>()
    val removedSession: LiveData<Session>
        get() = _removedSession


    val sessionList: LiveData<List<Session>> = repository.getSessions()
    val exerciseList: LiveData<List<Exercise>> = repository.getAllExercises()
    val sessionExerciseList: LiveData<List<SessionExerciseWithExercise>> =
        repository.getSessionExercises()
    val sets: LiveData<List<GymSet>> = repository.getSets()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()



    fun onEvent(event: Event) {
        when (event) {
            is DrawerEvent.OnHomeClicked -> {
                sendUiEvent(UiEvent.Navigate(Routes.HOME_SCREEN))
            }
            is DrawerEvent.OnExercisesClicked -> {
                sendUiEvent(UiEvent.Navigate(Routes.EXERCISE_SCREEN))
            }
            is HomeEvent.OnSessionClick -> {
                val sessionId = event.session.sessionId
                Log.d("HVM","OnSessionClick: $sessionId")
                sendUiEvent(event = UiEvent.Navigate(Routes.SESSION_SCREEN + "?sessionId=${sessionId}"))
            }
            is HomeEvent.OnAddSessionClick -> {
                val newSession = Session()
                runBlocking {
                    sessionId.value = withContext(Dispatchers.IO) {
                        repository.insertSession(newSession)
                    }!!
                }
                sendUiEvent(UiEvent.Navigate(Routes.SESSION_SCREEN + "?sessionId=${sessionId.value}"))
            }
            is HomeEvent.SetSelectedSession -> {
                val newId = event.session.sessionId
                selectedSession = if(newId != selectedSession) newId else -1L
            }
            is HomeEvent.OnDeleteSession -> {
                _removedSession.value = event.session
                selectedSession = -1L
                viewModelScope.launch {
                    repository.removeSession(event.session)
                }
                sendUiEvent(UiEvent.ShowSnackbar(
                    message = "Session removed",
                    actionLabel = "Undo",
                    action = HomeEvent.RestoreRemovedSession
                ))
            }
            is HomeEvent.RestoreRemovedSession -> {
                viewModelScope.launch {
                    removedSession.value?.let { repository.insertSession(it) }
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    fun getMuscleGroupsForSession(sessionId: Long): Flow<List<String>> {
        return flow {
            val muscleGroups = repository.getMuscleGroupsForSession(sessionId)
            emit(muscleGroups)
        }.flowOn(Dispatchers.IO)
    }

}