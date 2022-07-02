package com.example.android.january2022.ui.profile

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.january2022.db.GymRepository
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.db.entities.SessionExercise
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
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.time.LocalDate
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


    data class DatabaseModel(
        val sessions: List<Session>,
        val exercises: List<Exercise>,
        val sessionExercises: List<SessionExercise>,
        val sets: List<GymSet>
    )

    private fun exportDatabase(uri: Uri, context: Context) {
        val gson = Converters.registerAll(GsonBuilder().setPrettyPrinting()).create()
        val databaseModel = DatabaseModel(
            sessions = repository.getSessionList(),
            exercises = repository.getExerciseList(),
            sessionExercises = repository.getSessionExerciseList(),
            sets = repository.getSetList()
        )
        val ob = gson.toJson(databaseModel)
        saveToFile(uri, context.contentResolver, ob)
    }

    private fun importDatabase(uri: Uri,context: Context) {
        viewModelScope.launch {
            val gson = Converters.registerAll(GsonBuilder().setPrettyPrinting()).create()
            loadFromFile(uri, context.contentResolver)?.let {
                val importedDatabase = gson.fromJson(it, DatabaseModel::class.java)
                importedDatabase.sessions.forEach { session ->
                    repository.insertSession(session)
                }
                importedDatabase.exercises.forEach { exercise ->
                    repository.insertExercise(exercise)
                }
                importedDatabase.sessionExercises.forEach { sessionExercise ->
                    repository.insertSessionExercise(sessionExercise)
                }
                importedDatabase.sets.forEach { set->
                    repository.insertSet(set)
                }

            }
        }
    }

    fun onEvent(event: Event) {
        when (event) {
            is ProfileEvent.NavigateToExercises -> {
                sendUiEvent(UiEvent.Navigate(Routes.EXERCISE_SCREEN))
            }
            is ProfileEvent.ExportDatabase -> {
                viewModelScope.launch(Dispatchers.IO) {
                    exportDatabase(event.uri, event.context)
                }
            }
            is ProfileEvent.ImportDatabase -> {
                viewModelScope.launch(Dispatchers.IO) {
                    importDatabase(event.uri, event.context)
                }
            }
            is ProfileEvent.CreateFile -> {
                val date = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE)
                sendUiEvent(UiEvent.FileCreated("workout_db_$date.json"))
            }
            is ProfileEvent.ClearDatabase -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.clearDatabase()
                }
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

    fun loadFromFile(uri: Uri, contentResolver: ContentResolver): String? {
        try {
            contentResolver.openFileDescriptor(uri, "r")?.use { parcelFileDescriptor ->
                FileInputStream(parcelFileDescriptor.fileDescriptor).use {
                    return it.readBytes().decodeToString()
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}