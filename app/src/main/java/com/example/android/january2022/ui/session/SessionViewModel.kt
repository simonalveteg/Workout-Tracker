package com.example.android.january2022.ui.session

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import com.example.android.january2022.db.GymRepository
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.db.entities.SessionExerciseWithExercise
import com.example.android.january2022.utils.Event
import com.example.android.january2022.utils.Routes
import com.example.android.january2022.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val repository: GymRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var currentSession by mutableStateOf<Session?>(null)
        private set

    var currentSessionExerciseList = MutableLiveData<List<SessionExerciseWithExercise>>()
    val setsList: LiveData<List<GymSet>> = repository.getSets()

    init {
        // did we get here from an existing session?
        val sessionId = savedStateHandle.get<Long>("sessionId")!!
        Log.d("SVM", "Session Id is $sessionId")
        if(sessionId != -1L) {
            viewModelScope.launch {
                currentSession = withContext(Dispatchers.IO) {
                    repository.getSession(sessionId)
                }
                currentSessionExerciseList.value = withContext(Dispatchers.IO) {
                    repository.getSessionExercisesForSession(sessionId)
                }
            }

        }
    }

    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: Event) {
        when(event) {
            is SessionEvent.MoodChanged -> {
                viewModelScope.launch {
                    repository.updateSet(event.set.copy(mood = event.newMood))
                }
            }
            is SessionEvent.WeightChanged -> {
                viewModelScope.launch {
                    repository.updateSet(event.set.copy(weight = event.newWeight))
                }
            }
            is SessionEvent.RepsChanged -> {
                viewModelScope.launch {
                    repository.updateSet(event.set.copy(reps = event.newReps))
                }
            }
            is SessionEvent.RemoveSelectedSet -> {
                viewModelScope.launch {
                    repository.updateSet(event.set.copy(deleted = true))
                }
            }
            is SessionEvent.OnAddSet -> {
                viewModelScope.launch {
                    repository.insertSet(
                        GymSet(parentSessionExerciseId = event.sessionExercise.sessionExercise.sessionExerciseId)
                    )
                }
            }
            is SessionEvent.OnAddSessionExerciseClicked -> {
                sendUiEvent(UiEvent.Navigate(Routes.EXERCISE_PICKER_SCREEN))
            }
        }
    }

    private val _removedSet = MutableLiveData<GymSet>()
    val removedSet: LiveData<GymSet>
        get() = _removedSet

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

}