package com.example.android.january2022.ui.profile

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.january2022.db.GymRepository
import com.example.android.january2022.utils.Event
import com.example.android.january2022.utils.Routes
import com.example.android.january2022.utils.UiEvent
import com.fatboyindustrial.gsonjavatime.Converters
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
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
        val gson = Converters.registerAll(GsonBuilder().setPrettyPrinting()).create()
        val map = mapOf(
            "sessions" to gson.toJson(repository.getSessionList()),
            "exercises" to gson.toJson(repository.getExerciseList()),
            "sessionExercises" to gson.toJson(repository.getSessionExerciseList()),
            "sets" to gson.toJson(repository.getSetList())
        )
        // ugly hack to convert the map into json
        var json = "{" // open json object
        map.entries.forEachIndexed { index, it ->
            json += "\"${it.key}\": ${it.value}"
            if(index+1 < map.entries.size) json += ","
        }
        json += "}" // close json object
        saveToFile(uri, context.contentResolver, json)
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