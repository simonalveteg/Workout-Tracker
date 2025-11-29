package com.alveteg.simon.workouts

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.alveteg.simon.workouts.timer.TimerService
import com.alveteg.simon.workouts.timer.sendTimerIntent
import com.alveteg.simon.workouts.ui.NavHost
import com.alveteg.simon.workouts.ui.theme.WorkoutTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import timber.log.Timber.DebugTree


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (BuildConfig.DEBUG) {
      Timber.plant(DebugTree())
    }
    enableEdgeToEdge()
    setContent {
      WorkoutTheme {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
          ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            0
          )
        }
        val navController = rememberNavController()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        NavHost(navController)
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
