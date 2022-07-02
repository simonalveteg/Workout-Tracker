package com.example.android.january2022.ui.profile

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.january2022.db.GymRepository
import com.example.android.january2022.utils.Event
import com.example.android.january2022.utils.Routes
import com.example.android.january2022.utils.UiEvent
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: GymRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    private suspend fun getDatabaseAsJson(): String {
        val jsonString = withContext(Dispatchers.IO){
            "[\n ${repository.getSessionList()},\n" +
                    "${repository.getExerciseList()},\n" +
                    "${repository.getSessionExerciseList()},\n" +
                    "${repository.getSetList()}\n]"
        }
        Log.d("PVM","database converted to jsonString")
        return jsonString
    }

    fun onEvent(event: Event) {
        when(event) {
            is ProfileEvent.NavigateToExercises -> {
                sendUiEvent(UiEvent.Navigate(Routes.EXERCISE_SCREEN))
            }
            is ProfileEvent.ExportDatabase -> {
                viewModelScope.launch {
                    Log.d("PVM","export database button pressed")
                    sendUiEvent(UiEvent.ShareIntent(getDatabaseAsJson()))
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}