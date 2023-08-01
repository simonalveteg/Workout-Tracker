package com.example.android.january2022

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.android.january2022.ui.NavHost
import com.example.android.january2022.ui.theme.WorkoutTheme
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
    setContent {
      WorkoutTheme {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
          ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0)
        }
        val navController = rememberNavController()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        NavHost(navController)
      }
    }
  }
}