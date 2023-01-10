package com.example.android.january2022

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.core.view.WindowCompat
import androidx.navigation.*
import com.example.android.january2022.ui.rework.NavHost
import com.example.compose.WorkoutTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  @OptIn(ExperimentalAnimationApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      WorkoutTheme {
        val navController = rememberAnimatedNavController()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        NavHost(navController)
      }
    }
  }
}