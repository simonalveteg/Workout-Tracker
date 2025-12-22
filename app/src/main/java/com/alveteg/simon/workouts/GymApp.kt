package com.alveteg.simon.workouts

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.alveteg.simon.workouts.timer.TimerService
import com.alveteg.simon.workouts.worker.SessionReminderWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class GymApp : Application(), Configuration.Provider {
  @Inject lateinit var workerFactory: HiltWorkerFactory // 2. Inject Factory

  override val workManagerConfiguration: Configuration // 3. Override Config
    get() = Configuration.Builder()
      .setWorkerFactory(workerFactory)
      .build()

  override fun onCreate() {
    super.onCreate()
    createNotificationChannels()
  }


  private fun createNotificationChannels() {
    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    val workoutTimerGroup = "workoutTimer"
    notificationManager.createNotificationChannelGroup(
      NotificationChannelGroup(workoutTimerGroup, "Workout Timer")
    )

    val reminderGroup = "reminderGroup"
    notificationManager.createNotificationChannelGroup(
      NotificationChannelGroup(reminderGroup, "Reminders")
    )

    val timerLiveUpdateChannel = NotificationChannel(
      TimerService.CHANNEL_ID,
      "Timer Live Update",
      NotificationManager.IMPORTANCE_LOW
    ).apply {
      description = "Ongoing notification showing the current timer progress."
      group = workoutTimerGroup
      setShowBadge(false)
    }
    val timerAlarmChannel = NotificationChannel(
      TimerService.ALERT_CHANNEL_ID,
      "Timer Alarm",
      NotificationManager.IMPORTANCE_HIGH
    ).apply {
      description = "Alerts you when the timer is done"
      group = workoutTimerGroup
      setShowBadge(false)
    }

    val activeSessionReminderChannel = NotificationChannel(
      SessionReminderWorker.ALERT_CHANNEL_ID,
      "Active Session",
      NotificationManager.IMPORTANCE_HIGH
    ).apply {
      description = "Reminder to set an end time for your workout"
      group = reminderGroup
      enableVibration(true)
    }

    notificationManager.createNotificationChannel(timerLiveUpdateChannel)
    notificationManager.createNotificationChannel(timerAlarmChannel)
    notificationManager.createNotificationChannel(activeSessionReminderChannel)
  }
}