package com.example.android.january2022

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.android.january2022.ui.rework.NavHost
import com.example.compose.WorkoutTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      WorkoutTheme {
        val navController = rememberNavController()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        NavHost(navController)
      }
    }
  }
}