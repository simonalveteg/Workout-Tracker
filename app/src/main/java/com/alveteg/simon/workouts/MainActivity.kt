package com.alveteg.simon.workouts

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.rememberNavController
import com.alveteg.simon.workouts.db.LocalResistanceUnit
import com.alveteg.simon.workouts.db.ResistanceUnit
import com.alveteg.simon.workouts.db.UserPreferencesRepository
import com.alveteg.simon.workouts.timer.TimerService
import com.alveteg.simon.workouts.timer.sendTimerIntent
import com.alveteg.simon.workouts.ui.NavHost
import com.alveteg.simon.workouts.ui.theme.WorkoutTheme
import com.alveteg.simon.workouts.utils.Routes
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  @Inject
  lateinit var userPreferencesRepository: UserPreferencesRepository

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (BuildConfig.DEBUG) {
      Timber.plant(DebugTree())
    }
    enableEdgeToEdge()
    setContent {
      WorkoutTheme {
        val resistanceUnit by userPreferencesRepository.resistanceUnit.collectAsState(initial = ResistanceUnit.KG)

        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val hasDeniedNotifications by userPreferencesRepository
          .hasDeniedNotifications
          .collectAsState(initial = false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasDeniedNotifications) {
          var showRationale by remember { mutableStateOf(false) }

          val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
          ) { isGranted ->
            if (isGranted) {
              Timber.d("Notification permission granted")
            } else {
              Timber.w("Notification permission denied")
            }
          }

          LaunchedEffect(Unit) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            val activity = context as Activity

            when {
              context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED -> {
                Timber.d("Already granted")
              }

              ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) -> {
                showRationale = true
              }

              else -> {
                permissionLauncher.launch(permission)
              }
            }
          }

          if (showRationale) {
            AlertDialog(
              onDismissRequest = { showRationale = false },
              title = { Text("Enable notifications?") },
              text = {
                Text("Notifications keep your workout timer visible when the app is in the background, alert you when your rest is over, and remind you to set an end time for active sessions.")
              },
              confirmButton = {
                TextButton(onClick = {
                  showRationale = false
                  permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }) { Text("Allow") }
              },
              dismissButton = {
                TextButton(onClick = {
                  showRationale = false
                  scope.launch {
                    userPreferencesRepository.updateHasDeniedNotifications(true)
                  }
                }) { Text("Not Now") }
              }
            )
          }
        }

        val navController = rememberNavController()

        LaunchedEffect(Unit) {
          val sessionId = intent.getLongExtra("SESSION_ID", -1L)
          if (sessionId != -1L) {
            navController.navigate("${Routes.SESSION}/$sessionId") {
              launchSingleTop = true
            }
          }
        }
        CompositionLocalProvider(LocalResistanceUnit provides resistanceUnit) {
          NavHost(navController)
        }
      }
    }
  }

  override fun onStart() {
    super.onStart()
    this.sendTimerIntent {
      it.action = TimerService.Actions.MOVE_TO_BACKGROUND.toString()
    }
  }

  override fun onPause() {
    super.onPause()
    this.sendTimerIntent {
      it.action = TimerService.Actions.MOVE_TO_FOREGROUND.toString()
    }
  }
}
