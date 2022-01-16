package com.example.android.january2022.ui.exercises

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val repository: GymRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var currentSession by mutableStateOf<Session?>(null)
        private set

    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val exerciseList: LiveData<List<Exercise>> = repository.getExercises()

    init {
        val sessionId = savedStateHandle.get<Long>("sessionId")?: -1L
        if(sessionId != -1L) {
            viewModelScope.launch {
                currentSession = withContext(Dispatchers.IO) {
                    repository.getSession(sessionId)
                }
            }
        }
    }


    fun onEvent(event: Event) {
        when(event) {
            is ExerciseEvent.NewExerciseClicked -> {
                viewModelScope.launch {
                    // TODO: Remove or implement functionality. "add exercise to db"
                }
            }
            is ExerciseEvent.ExerciseSelected -> {
                viewModelScope.launch {
                    Log.d("EVM","Exercise selected, sID: ${currentSession?.sessionId}")
                    val newSessionExercise = SessionExercise(
                        parentSessionId = currentSession?.sessionId?: -1L,
                        parentExerciseId = event.exercise.exerciseId
                    )
                    repository.insertSessionExercise(newSessionExercise)
                }
                sendUiEvent(UiEvent.PopBackStack)
            }
            is ExerciseEvent.ExerciseInfoClicked -> {
                val exerciseId = event.exercise.exerciseId


                sendUiEvent(UiEvent.Navigate(Routes.EXERCISE_DETAIL_SCREEN + "?exerciseId=${exerciseId}"))
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

}