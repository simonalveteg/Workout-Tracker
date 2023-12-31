package com.example.android.january2022

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import com.example.android.january2022.timer.TimerService
import com.example.android.january2022.timer.sendTimerIntent
import com.example.android.january2022.ui.WorkoutsApp
import com.example.android.january2022.ui.theme.WorkoutTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import timber.log.Timber.DebugTree

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            WorkoutTheme {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        0,
                    )
                }

                WorkoutsApp()
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
