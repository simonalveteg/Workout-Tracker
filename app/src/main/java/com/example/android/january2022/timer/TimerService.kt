package com.example.android.january2022.timer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.android.january2022.R
import java.util.*

class TimerService : Service() {
  private lateinit var timer: Timer

  override fun onBind(intent: Intent?): IBinder? {
    return null
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    when (intent?.action) {
      Actions.START.toString() -> start()
      Actions.STOP.toString() -> stopSelf()
    }
    return super.onStartCommand(intent, flags, startId)
  }

  private fun start() {
    val notification = NotificationCompat.Builder(this, "workout_timer")
      .setSmallIcon(R.drawable.ic_launcher_foreground)
      .setContentTitle("Timer Running")
      .setContentText("Elapsed time ...")
      .build()
    startForeground(1, notification)
  }

  enum class Actions {
    START, STOP, INCREMENT, DECREMENT, RESTART
  }
}
