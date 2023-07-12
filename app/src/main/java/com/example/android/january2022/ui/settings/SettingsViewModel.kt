package com.example.android.january2022.ui.settings

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.january2022.db.GymRepository
import com.example.android.january2022.ui.DatabaseModel
import com.example.android.january2022.utils.Event
import com.example.android.january2022.utils.UiEvent
import com.fatboyindustrial.gsonjavatime.Converters
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
  private val repo: GymRepository
) : ViewModel() {

  fun onEvent(event: Event) {
    when (event) {
      is SettingsEvent.ImportDatabase -> {
        viewModelScope.launch(Dispatchers.IO) {
          importDatabase(event.uri, event.context)
        }
      }
      is SettingsEvent.ExportDatabase -> {
        viewModelScope.launch(Dispatchers.IO) {
          exportDatabase(event.uri, event.context)
        }
      }
      is SettingsEvent.CreateFile -> {
        val date = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE)
        sendUiEvent(UiEvent.FileCreated("workout_db_$date.json"))
      }
      is SettingsEvent.ClearDatabase -> {
        viewModelScope.launch(Dispatchers.IO) {
          repo.clearDatabase()
        }
      }
    }
  }

  private val _uiEvent = Channel<UiEvent>()
  val uiEvent = _uiEvent.receiveAsFlow()

  private fun sendUiEvent(event: UiEvent) {
    viewModelScope.launch {
      _uiEvent.send(event)
    }
  }

  private fun exportDatabase(uri: Uri, context: Context) {
    val gson = Converters.registerAll(GsonBuilder().setPrettyPrinting()).create()
    val databaseModel = repo.getDatabaseModel()
    val ob = gson.toJson(databaseModel)
    saveToFile(uri, context.contentResolver, ob)
  }

  private fun importDatabase(uri: Uri, context: Context) {
    viewModelScope.launch {
      val gson = Converters.registerAll(GsonBuilder().setPrettyPrinting()).create()
      loadFromFile(uri, context.contentResolver)?.let {
        val importedDatabase = gson.fromJson(it, DatabaseModel::class.java)
        Timber.d("$importedDatabase")
        importedDatabase.sessions.forEach { session ->
          repo.insertSession(session)
        }
        importedDatabase.exercises.forEach { exercise ->
          repo.insertExercise(exercise)
        }
        importedDatabase.sessionExercises.forEach { sessionExercise ->
          repo.insertSessionExercise(sessionExercise)
        }
        importedDatabase.sets.forEach { set ->
          repo.insertSet(set)
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

  private fun loadFromFile(uri: Uri, contentResolver: ContentResolver): String? {
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
}