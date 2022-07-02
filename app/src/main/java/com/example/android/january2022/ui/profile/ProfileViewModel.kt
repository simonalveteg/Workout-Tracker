package com.example.android.january2022.ui.profile

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
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
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: GymRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    private fun exportDatabase(uri: Uri, context: Context) {
        val jsonString = "[\n ${repository.getSessionList()},\n" +
                    "${repository.getExerciseList()},\n" +
                    "${repository.getSessionExerciseList()},\n" +
                    "${repository.getSetList()}\n]"
        Log.d("PVM", "database converted to jsonString")
        saveToFile(uri, context.contentResolver, jsonString)

    }

    fun onEvent(event: Event) {
        when (event) {
            is ProfileEvent.NavigateToExercises -> {
                sendUiEvent(UiEvent.Navigate(Routes.EXERCISE_SCREEN))
            }
            is ProfileEvent.ExportDatabase -> {
                viewModelScope.launch(Dispatchers.IO) {
                    Log.d("PVM", "export database button pressed")
                    exportDatabase(event.uri, event.context)
                }
            }
            is ProfileEvent.CreateFile -> {
                val date = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE)
                sendUiEvent(UiEvent.FileCreated("workout_db_$date.json"))
            }
        }
    }

    private fun saveToFile(uri: Uri, contentResolver: ContentResolver, content: String) {
        try {
            contentResolver.openFileDescriptor(uri, "w")?.use { parcelFileDescriptor ->
                FileOutputStream(parcelFileDescriptor.fileDescriptor).use {
                    it.write(content.toByteArray())
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}