package com.alveteg.simon.workouts.ui.settings

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alveteg.simon.workouts.db.ResistanceUnit
import com.alveteg.simon.workouts.ui.settings.components.InfoBox
import com.alveteg.simon.workouts.ui.settings.components.SegmentedInput
import com.alveteg.simon.workouts.ui.settings.components.SettingsSection
import com.alveteg.simon.workouts.ui.settings.components.SliderInput
import com.alveteg.simon.workouts.ui.theme.AppTheme
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
  val resistanceUnit by viewModel.resistanceUnit.collectAsStateWithLifecycle()
  val appTheme by viewModel.appTheme.collectAsStateWithLifecycle()
  val useDynamicColor by viewModel.useDynamicColor.collectAsStateWithLifecycle()

  LaunchedEffect(key1 = true) {
    viewModel.uiEvent.collect { event ->
      when (event) {
        else -> Unit
      }
    }
  }

  val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

  Scaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
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
        scrollBehavior = scrollBehavior
      )
    },
  ) { padding ->
    Column(
      Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)
        .padding(padding)
        .verticalScroll(rememberScrollState()),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      horizontalAlignment = Alignment.Start
    ) {
      SettingsSection(
        title = "Preferences"
      ) {
        SegmentedInput(
          label = "Resistance Unit",
          description = "Choose the unit that should be displayed for weights.",
          options = ResistanceUnit.entries,
          selectedOption = resistanceUnit,
          onOptionSelect = { viewModel.onResistanceUnitChange(it) },
          labelProvider = { it.label }
        )
        SliderInput(
          label = "Target Workout Frequency",
          description = "Number of times per week you aim to work out.",
          value = targetFrequency,
          valueRange = 0f..7f,
          roundToInt = true,
          steps = 6,
          onValueChange = { viewModel.onTargetFrequencyChange(it.roundToInt().toFloat()) }
        )
        SliderInput(
          label = "Secondary Muscle Weight",
          description = "Sets the importance of secondary muscles relative to primary muscles when calculating muscle usage. Default value is 0.2.",
          value = secondaryWeight,
          valueRange = 0f..1f,
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
      SettingsSection(
        title = "Theme and Colors"
      ) {
        val supportsDynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        SegmentedInput(
          label = "App Theme",
          description = "Choose whether the app should be in Light, Dark, or follow System settings.",
          options = AppTheme.entries,
          selectedOption = appTheme,
          onOptionSelect = { viewModel.onThemeChange(it) },
          labelProvider = { it.label }
        )
        if (supportsDynamicColor) {
          SegmentedInput(
            label = "Color Palette",
            description = "Choose whether to use the default Workouts theme or colors generated from your wallpaper (Material You).",
            options = listOf(false, true),
            selectedOption = useDynamicColor,
            onOptionSelect = { viewModel.onDynamicColorChange(it) },
            labelProvider = {
              if (it) "Dynamic" else "Workouts"
            }
          )
        }
      }
      SettingsSection(
        title = "Notifications"
      ) {
        OutlinedButton(
          modifier = Modifier.fillMaxWidth(),
          onClick = {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
              putExtra(Settings.EXTRA_APP_PACKAGE, mContext.packageName)
            }
            mContext.startActivity(intent)
          }
        ) {
          Text("Open Notification Settings")
        }
      }
    }
  }
}
