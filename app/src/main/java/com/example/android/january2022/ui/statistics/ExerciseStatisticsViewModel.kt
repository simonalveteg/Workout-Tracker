package com.example.android.january2022.ui.statistics

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.january2022.db.GymRepository
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.utils.Event
import com.example.android.january2022.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ExerciseStatisticsViewModel @Inject constructor(
    private val repository: GymRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    var currentExercise by mutableStateOf(Exercise())
        private set

    init {
        // did we get here from an existing exercise?
        val exerciseId = savedStateHandle.get<Long>("exerciseId")!!
        Log.d("SVM", "Exercise Id is $exerciseId")
        if (exerciseId != -1L) {
            viewModelScope.launch {
                currentExercise = withContext(Dispatchers.IO) {
                    repository.getExercise(exerciseId)
                }
            }
        }
    }


    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: Event) {
        when (event) {

            else -> Unit
        }
    }
}