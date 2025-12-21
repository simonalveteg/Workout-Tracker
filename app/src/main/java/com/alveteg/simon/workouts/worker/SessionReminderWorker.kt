package com.alveteg.simon.workouts.worker

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.alveteg.simon.workouts.MainActivity
import com.alveteg.simon.workouts.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import com.alveteg.simon.workouts.db.GymRepository
import com.alveteg.simon.workouts.timer.TimerService

@HiltWorker
class SessionReminderWorker @AssistedInject constructor(
  @Assisted context: Context,
  @Assisted workerParams: WorkerParameters,
  private val repository: GymRepository
) : CoroutineWorker(context, workerParams) {

  override suspend fun doWork(): Result {
    val sessionId = inputData.getLong("SESSION_ID", -1L)
    if (sessionId == -1L) return Result.failure()

    val session = repository.getSessionById(sessionId)

    if (session.end == null) {
      sendReminderNotification(sessionId)
    }

    return Result.success()
  }

  private fun sendReminderNotification(sessionId: Long) {
    val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val intent = Intent(applicationContext, MainActivity::class.java).apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
      putExtra("SESSION_ID", sessionId)
    }

    val pendingIntent = PendingIntent.getActivity(
      applicationContext,
      sessionId.toInt(),
      intent,
      PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val notification = NotificationCompat.Builder(applicationContext, TimerService.ALERT_CHANNEL_ID)
      .setSmallIcon(R.drawable.ic_launcher_foreground)
      .setContentTitle("Workout still active")
      .setContentText("Your workout is still active, did you forget to set an end time?")
      .setPriority(NotificationCompat.PRIORITY_HIGH)
      .setAutoCancel(true)
      .setContentIntent(pendingIntent)
      .build()

    notificationManager.notify(sessionId.toInt(), notification)
  }

  companion object {
    val ALERT_CHANNEL_ID = "session_reminder"
  }
}
