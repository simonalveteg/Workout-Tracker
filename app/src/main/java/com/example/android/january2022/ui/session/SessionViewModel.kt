package com.example.android.january2022.ui.session

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import com.example.android.january2022.db.GymRepository
import com.example.android.january2022.db.SetType
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.db.entities.SessionExercise
import com.example.android.january2022.db.entities.SessionExerciseWithExercise
import com.example.android.january2022.utils.Event
import com.example.android.january2022.utils.Routes
import com.example.android.january2022.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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

    var selectedSessionExercise by mutableStateOf(-1L)
        private set

    val setsList: LiveData<List<GymSet>> = repository.getSets()

    private val _removedSet = MutableLiveData<GymSet>()
    val removedSet: LiveData<GymSet>
        get() = _removedSet

    private val _removedSessionExercise = MutableLiveData<SessionExercise>()
    val removedSessionExercise: LiveData<SessionExercise>
        get() = _removedSessionExercise

    init {
        // did we get here from an existing session?
        val sessionId = savedStateHandle.get<Long>("sessionId")!!
        Log.d("SVM", "Session Id is $sessionId")
        if(sessionId != -1L) {
            viewModelScope.launch {
                currentSession = withContext(Dispatchers.IO) {
                    repository.getSession(sessionId)
                }
            }

        }
    }

    fun getSessionExercisesForSession() : LiveData<List<SessionExerciseWithExercise>> {
        return repository.getSessionExercisesForSession(currentSession?.sessionId ?: -1)
    }
    fun getMuscleGroupsForSession(sessionId: Long): Flow<List<String>> {
        return flow {
            val muscleGroups = repository.getMuscleGroupsForSession(sessionId)
            emit(muscleGroups)
        }.flowOn(Dispatchers.IO)
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
            is SessionEvent.SetTypeChanged -> {
                viewModelScope.launch {
                    repository.updateSet(event.set.copy(setType = SetType.next(event.set.setType)))
                }
            }
            is SessionEvent.OnAddSet -> {
                viewModelScope.launch {
                    repository.insertSet(
                        GymSet(parentSessionExerciseId = event.sessionExercise.sessionExercise.sessionExerciseId)
                    )
                }
            }
            is SessionEvent.RemoveSelectedSet -> {
                viewModelScope.launch {
                    repository.updateSet(event.set.copy(deleted = true))
                }
                _removedSet.value = event.set
                sendUiEvent(UiEvent.ShowSnackbar(
                    message = "Set removed from session",
                    actionLabel = "Undo",
                    action = SessionEvent.RestoreRemovedSet
                ))
            }
            is SessionEvent.RestoreRemovedSet -> {
                viewModelScope.launch {
                    repository.updateSet(removedSet.value!!.copy(deleted = false))
                }
            }
            is SessionEvent.RestoreRemovedSessionExercise -> {
                viewModelScope.launch {
                    removedSessionExercise.value?.let { repository.insertSessionExercise(it) }
                }
            }
            is SessionEvent.OnAddSessionExerciseClicked -> {
                sendUiEvent(UiEvent.Navigate(Routes.EXERCISE_PICKER_SCREEN+"?sessionId=${currentSession?.sessionId}"))
            }
            is SessionEvent.OnSessionExerciseInfoClicked -> {
                sendUiEvent(UiEvent.Navigate(Routes.EXERCISE_DETAIL_SCREEN+"?exerciseId=${event.exerciseId}"))
            }
            is SessionEvent.SetSelectedSessionExercise -> {
                val newId = event.sessionExercise.sessionExercise.sessionExerciseId
                selectedSessionExercise = if(newId != selectedSessionExercise) newId else -1L
            }
            is SessionEvent.OnDeleteSessionExercise -> {
                _removedSessionExercise.value = event.sessionExercise.sessionExercise
                selectedSessionExercise = -1L
                viewModelScope.launch {
                    repository.removeSessionExercise(event.sessionExercise.sessionExercise)
                }
                sendUiEvent(UiEvent.ShowSnackbar(
                    message = "Exercise removed from session",
                    actionLabel = "Undo",
                    action = SessionEvent.RestoreRemovedSessionExercise
                ))
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

}