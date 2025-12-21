package com.alveteg.simon.workouts.ui.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alveteg.simon.workouts.ui.session.SessionViewModel
import com.alveteg.simon.workouts.utils.Routes
import com.alveteg.simon.workouts.utils.UiEvent
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
  onNavigate: (UiEvent.Navigate) -> Unit,
  viewModel: SettingsViewModel = hiltViewModel()
) {
  val mContext = LocalContext.current
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

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text("Settings")
        },
        navigationIcon = {
          IconButton(
            onClick = { onNavigate(UiEvent.Navigate(Routes.HOME, popBackStack = true)) }
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Go back."
            )
          }
        },
      )
    },

    ) { padding ->
    Column(
      Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)
        .padding(padding),
      verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterVertically),
      horizontalAlignment = Alignment.Start
    ) {
      Text(
        text = "Backup and restore",
        modifier = Modifier.padding(bottom = 12.dp),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.secondary
      )
      SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth(),
      ) {
        OutlinedButton(
          modifier = Modifier
            .height(48.dp)
            .weight(1f),
          shape = RoundedCornerShape(topStart = 100f, bottomStart = 100f),
          onClick = { exportLauncher.launch("workout_backup_${LocalDateTime.now()}.db") }
        ) { Text("Create backup") }
        OutlinedButton(
          modifier = Modifier
            .height(48.dp)
            .weight(1f),
          shape = RoundedCornerShape(topEnd = 100f, bottomEnd = 100f),
          onClick = { importLauncher.launch(arrayOf("*/*")) }
        ) { Text("Restore backup") }
      }
      InfoBox(text = "Restoring a backup will replace all your existing data, and can not be undone. Create a backup first if you're not sure.")
    }
  }
}
