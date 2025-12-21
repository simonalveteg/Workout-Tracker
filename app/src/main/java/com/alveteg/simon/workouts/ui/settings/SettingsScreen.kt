package com.alveteg.simon.workouts.ui.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.alveteg.simon.workouts.utils.UiEvent

@Composable
fun SettingsScreen(
  viewModel: SettingsViewModel = hiltViewModel()
) {
  val mContext = LocalContext.current
  val importLauncherOld = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent(),
    onResult = { uri ->
      uri?.let {
        viewModel.onEvent(SettingsEvent.ImportDatabase(mContext, it))
      }
    }
  )
  val exportLauncher = rememberLauncherForActivityResult(
    contract = CreateDocument("application/octet-stream")
  ) { uri ->
    uri?.let { viewModel.exportDatabase(mContext, it) }
  }

  val importLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.OpenDocument()
  ) { uri ->
    uri?.let { viewModel.importDatabase(mContext, it) }
  }

  LaunchedEffect(key1 = true) {
    viewModel.uiEvent.collect { event ->
      when (event) {
        else -> Unit
      }
    }
  }

  Scaffold { padding ->
    Column(
      Modifier
        .fillMaxSize()
        .padding(padding),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text("Settings")
      FilledTonalButton(onClick = {
        importLauncherOld.launch("application/json")
      }) {
        Text("Import Database")
      }
      FilledTonalButton(onClick = {
        viewModel.onEvent(SettingsEvent.ClearDatabase)
      }) {
        Text("Delete Database")
      }
      Button(onClick = { exportLauncher.launch("workout_backup.db") }) { Text("Backup") }
      Button(onClick = { importLauncher.launch(arrayOf("*/*")) }) { Text("Restore") }
    }
  }
}