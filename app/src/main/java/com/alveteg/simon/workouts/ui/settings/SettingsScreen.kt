package com.alveteg.simon.workouts.ui.settings

import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alveteg.simon.workouts.ui.settings.components.InfoBox
import com.alveteg.simon.workouts.ui.settings.components.SettingsSection
import com.alveteg.simon.workouts.ui.settings.components.SliderInput
import com.alveteg.simon.workouts.utils.Routes
import com.alveteg.simon.workouts.utils.UiEvent
import java.time.LocalDateTime
import kotlin.math.roundToInt

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

  val targetFrequency by viewModel.targetFrequency.collectAsStateWithLifecycle()
  val secondaryWeight by viewModel.secondaryMuscleWeight.collectAsStateWithLifecycle()

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
      verticalArrangement = Arrangement.spacedBy(8.dp),
      horizontalAlignment = Alignment.Start
    ) {
      SettingsSection(
        title = "Preferences"
      ) {
        SliderInput(
          label = "Target Workout Frequency",
          description = "Number of times per week you aim to work out.",
          value = targetFrequency,
          valueRange = 0f .. 7f,
          roundToInt = true,
          steps = 6,
          onValueChange = { viewModel.onTargetFrequencyChange(it.roundToInt().toFloat()) }
        )
        SliderInput(
          label = "Secondary Muscle Weight",
          description = "Sets the importance of secondary muscles relative to primary muscles when calculating muscle usage.",
          value = secondaryWeight,
          valueRange = 0f .. 1f,
          steps = 10,
          onValueChange = { viewModel.onSecondaryMuscleWeightChange(it) }
        )
      }
      SettingsSection(
        title = "Backup and restore",
      ) {
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
}
