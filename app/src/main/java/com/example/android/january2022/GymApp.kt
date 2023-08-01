package com.example.android.january2022

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GymApp: Application() {
  override fun onCreate() {
    super.onCreate()
    val channel = NotificationChannel(
      "workout_timer",
      "Workout Timer Notifications",
      NotificationManager.IMPORTANCE_HIGH
    )
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
  }
}