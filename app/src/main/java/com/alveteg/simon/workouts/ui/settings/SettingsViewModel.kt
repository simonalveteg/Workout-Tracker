package com.alveteg.simon.workouts.ui.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alveteg.simon.workouts.db.GymRepository
import com.alveteg.simon.workouts.utils.Event
import com.alveteg.simon.workouts.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
  private val repo: GymRepository
) : ViewModel() {

  fun onEvent(event: Event) {
    when (event) {

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
        repo.checkpoint()

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
}