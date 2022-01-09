package com.example.android.january2022.ui.exercises

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android.january2022.db.GymRepository
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.db.entities.SessionExercise
import com.example.android.january2022.db.entities.SessionExerciseWithExercise
import com.example.android.january2022.utils.Event
import com.example.android.january2022.utils.Routes
import com.example.android.january2022.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    val repository: GymRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var currentSession by mutableStateOf<Session?>(null)
        private set


    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val sessionId = savedStateHandle.get<Long>("sessionId")!!
        if(sessionId != -1L) {
            viewModelScope.launch {
                currentSession = repository.getSession(sessionId)
            }
        }
    }


    fun onEvent(event: Event) {
        when(event) {
            is ExerciseEvent.NewExerciseClicked -> {
                viewModelScope.launch {
                    repository.insertExercise(Exercise(exerciseTitle = event.title))
                }
            }
            is ExerciseEvent.ExerciseSelected -> {
                viewModelScope.launch {
                    val newSessionExercise = SessionExercise(
                        parentSessionId = currentSession?.sessionId?: -1L,
                        parentExerciseId = event.exercise.exerciseId
                    )
                    repository.insertSessionExercise(newSessionExercise)
                }
                sendUiEvent(UiEvent.PopBackStack)
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

}