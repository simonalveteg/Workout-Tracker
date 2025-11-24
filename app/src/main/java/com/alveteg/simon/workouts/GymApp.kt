package com.alveteg.simon.workouts

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.alveteg.simon.workouts.timer.TimerService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GymApp : Application() {
  override fun onCreate() {
    super.onCreate()
    val channel = NotificationChannel(
      TimerService.CHANNEL_ID,
      "Workout Timer",
      NotificationManager.IMPORTANCE_DEFAULT
    ).also {
      it.setSound(null, null)
    }
    val alertChannel = NotificationChannel(
      TimerService.ALERT_CHANNEL_ID,
      "Workout Timer Alerts",
      NotificationManager.IMPORTANCE_HIGH
    ).also {
      it.enableVibration(true)
    }
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
    notificationManager.createNotificationChannel(alertChannel)
  }
}