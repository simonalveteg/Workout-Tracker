package com.alveteg.simon.workouts.ui.settings

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alveteg.simon.workouts.db.GymRepository
import com.alveteg.simon.workouts.db.entities.GymSet
import com.alveteg.simon.workouts.db.entities.Rpe
import com.alveteg.simon.workouts.ui.OldDatabaseModel
import com.alveteg.simon.workouts.utils.Event
import com.alveteg.simon.workouts.utils.UiEvent
import com.fatboyindustrial.gsonjavatime.Converters
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
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
          importDatabaseLegacy(event.uri, event.context)
        }
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

  fun exportDatabase(context: Context, destinationUri: Uri) {
    viewModelScope.launch(Dispatchers.IO) {
      try {
        repo.checkpointAndClose()

        val dbFile = repo.getDatabaseFile(context)
        context.contentResolver.openOutputStream(destinationUri)?.use { output ->
          dbFile.inputStream().use { input ->
            input.copyTo(output)
          }
        }
        Timber.d("Database exported successfully to binary format")
      } catch (e: Exception) {
        Timber.e(e, "Error exporting database")
      }
    }
  }

  fun importDatabase(context: Context, sourceUri: Uri) {
    viewModelScope.launch(Dispatchers.IO) {
      try {
        repo.checkpointAndClose()

        val dbFile = repo.getDatabaseFile(context)
        context.contentResolver.openInputStream(sourceUri)?.use { input ->
          dbFile.outputStream().use { output ->
            input.copyTo(output)
          }
        }

        // Delete temporary WAL files to prevent version mismatch or corruption
        File(dbFile.path + "-shm").delete()
        File(dbFile.path + "-wal").delete()

        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        val mainIntent = Intent.makeRestartActivityTask(intent?.component)
        context.startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
      } catch (e: Exception) {
        Timber.e(e, "Error restoring database.")
      }
    }
  }

  private fun importDatabaseLegacy(uri: Uri, context: Context) {
    viewModelScope.launch {
      val gson = Converters.registerAll(GsonBuilder().setPrettyPrinting()).create()
      loadFromFile(uri, context.contentResolver)?.let {
        val importedDatabase = gson.fromJson(it, OldDatabaseModel::class.java)
        Timber.d("$importedDatabase")
        importedDatabase.sessions.forEach { session ->
          repo.insertSession(session)
        }
        importedDatabase.exercises.forEach { exercise ->
          repo.insertExercise(exercise)
        }
        importedDatabase.sessionExercises.filter { sessionExercise ->
          sessionExercise.parentSessionId in importedDatabase.sessions.map { session -> session.sessionId }
        }.forEach { sessionExercise ->
          repo.insertSessionExercise(sessionExercise)
        }
        importedDatabase.sets.filter { set ->
          set.parentSessionExerciseId in importedDatabase.sessionExercises.map { sessionExercise -> sessionExercise.sessionExerciseId }
        }.forEach { set ->
          repo.insertSet(
            GymSet(
              parentSessionExerciseId = set.parentSessionExerciseId,
              reps = set.reps,
              weight = set.weight,
              time = set.time,
              distance = set.distance,
              rpe = when (set.setType) {
                "Warmup" -> Rpe.Level4
                "Easy" -> Rpe.Level6
                "Hard" -> Rpe.Level10
                else -> null
              }
            )
          )
        }
      }
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