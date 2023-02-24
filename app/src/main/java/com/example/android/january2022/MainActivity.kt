package com.example.android.january2022

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
        val navController = rememberNavController()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        NavHost(navController)
      }
    }
  }
}