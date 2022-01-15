package com.example.android.january2022.ui.home

import android.util.Log
import androidx.lifecycle.*
import com.example.android.january2022.db.GymRepository
import com.example.android.january2022.db.entities.*
import com.example.android.january2022.utils.Event
import com.example.android.january2022.utils.Routes
import com.example.android.january2022.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: GymRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val sessionId = MutableLiveData<Long>()

    val sessionList: LiveData<List<Session>> = repository.getSessions()
    val exerciseList: LiveData<List<Exercise>> = repository.getExercises()
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
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}