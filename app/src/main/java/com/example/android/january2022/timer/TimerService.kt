package com.example.android.january2022.timer

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.android.january2022.MainActivity
import com.example.android.january2022.R
import timber.log.Timber
import kotlin.math.roundToInt

class TimerService : Service() {

  private var running = false
  private var time = 0L
  private var maxTime = 60000L
  private val increment = 30 * 1000L

  private var showNotification = false

  private var timer: WorkoutTimer? = null

  private lateinit var notificationManager: NotificationManager

  override fun onBind(intent: Intent?): IBinder? {
    return null
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    notificationManager =
      ContextCompat.getSystemService(this, NotificationManager::class.java) as NotificationManager

    val action = intent?.action
    Timber.d("Start Timer with action: $action")
    when (action) {
      Actions.TOGGLE.toString() -> toggle()
      Actions.RESET.toString() -> reset()
      Actions.INCREMENT.toString() -> increment()
      Actions.DECREMENT.toString() -> decrement()
      Actions.STOP.toString() -> stop()
      Actions.MOVE_TO_FOREGROUND.toString() -> toForeground()
      Actions.MOVE_TO_BACKGROUND.toString() -> toBackground()
      Actions.QUERY.toString() -> sendStatus()
    }
    return super.onStartCommand(intent, flags, startId)
  }

  private fun toForeground() {
    if (running) {
      showNotification = true
      startForeground(1, buildStatusNotification())
    }
  }

  private fun toBackground() {
    showNotification = false
    stopForeground(STOP_FOREGROUND_REMOVE)
  }

  private fun toggle() {
    if (running) stop()
    else if (time <= 0L) start() else resume()
  }

  private fun start() {
    running = true
    timer?.cancel()
    timer = WorkoutTimer(maxTime).apply { start() }
    sendStatus()
  }

  private fun stop() {
    timer?.cancel()
    running = false
    sendStatus()
  }

  private fun resume() {
    timer?.cancel()
    timer = WorkoutTimer(time)
    timer?.start()
    running = true
    sendStatus()
  }

  private fun increment() {
    if (running) {
      stop()
      maxTime += increment
      time += increment
      resume()
    } else {
      maxTime += increment
      if (time > 0L) time += increment
    }
    sendStatus()
  }

  private fun decrement() {
    if (running) {
      stop()
      time = time.minus(increment).coerceAtLeast(0L)
      if (time <= 0L) reset() else resume()
    } else {
      maxTime = maxTime.minus(increment).coerceAtLeast(0L)
      time = time.minus(increment).coerceAtLeast(0L)
    }
    sendStatus()
  }

  private fun reset() {
    timer?.cancel()
    timer = null
    time = 0L
    running = false
    sendStatus()
  }

  private fun sendStatus() {
    val statusIntent = Intent().also {
      it.action = Intents.STATUS.toString()
      it.putExtra(Intents.Extras.IS_RUNNING.toString(), running)
      it.putExtra(Intents.Extras.TIME.toString(), time)
      it.putExtra(Intents.Extras.MAX_TIME.toString(), maxTime)
    }
    sendBroadcast(statusIntent)
    Timber.d("Broadcasting status: $running, $time, $maxTime")
  }

  private fun buildStatusNotification() = notification(
    subText = time.toTimerString(),
    channelId = CHANNEL_ID
  )

  private fun buildFinishedNotification() = notification(
    contentText = "Timer finished, tap to return.",
    progressBar = false,
    channelId = ALERT_CHANNEL_ID
  ).also {
    Timber.d("Built finished notification!")
  }

  private fun notification(
    contentText: String? = null,
    subText: String? = null,
    progressBar: Boolean = true,
    channelId: String
  ): Notification {
    val max = if (progressBar) maxTime.toInt() else 0
    val progress = if (progressBar) time.toInt() else 0

    val intent = Intent(this, MainActivity::class.java).also {
      it.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
    }
    val pendingIntent = PendingIntent.getActivity(
      this,
      0,
      intent,
      PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
    return NotificationCompat.Builder(this, channelId)
      .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
      .setContentIntent(pendingIntent)
      .setAutoCancel(true)
      .setProgress(max, progress, false)
      .setSmallIcon(R.drawable.ic_launcher_foreground)
      .setContentTitle("Workout Timer")
      .setContentText(contentText)
      .setSubText(subText)
      .build()
  }

  private fun notify(notification: Notification) {
    Timber.d("Notifying: $notification, $running")
    notificationManager.notify(1, notification)
  }

  private fun alert(notification: Notification) {
    notificationManager.notify(2, notification)
  }

  enum class Actions {
    TOGGLE, INCREMENT, DECREMENT, RESET, STOP, MOVE_TO_BACKGROUND, MOVE_TO_FOREGROUND, QUERY
  }

  enum class Intents {
    STATUS;

    enum class Extras {
      TIME, IS_RUNNING, MAX_TIME
    }
  }

  inner class WorkoutTimer(length: Long, interval: Long = 1000L) :
    CountDownTimer(length, interval) {

    override fun onTick(millisUntilFinished: Long) {
      time = millisUntilFinished
      if (showNotification) notify(buildStatusNotification())
      if (time <= 0L) onFinish()
      sendStatus()
    }

    override fun onFinish() {
      Timber.d("Timer finished")
      time = maxTime
      alert(buildFinishedNotification())
      reset()
      stopSelf()
    }
  }

  companion object {
    const val CHANNEL_ID = "workout_timer"
    const val ALERT_CHANNEL_ID = "workout_alert_timer"
  }
}

fun Long.toTimerString(): String {
  val totalSeconds = this.toFloat().div(1000).roundToInt()
  val minutes = totalSeconds / 60
  val seconds = totalSeconds % 60
  val displayedSeconds = if (seconds < 10) "0$seconds" else seconds
  return "$minutes:$displayedSeconds"
}

fun Context.sendTimerAction(action: TimerService.Actions) {
  this.sendTimerIntent {
    it.action = action.toString()
  }
}

fun Context.sendTimerIntent(also: (Intent) -> Unit) {
  Intent(this, TimerService::class.java).also {
    also(it)
    startService(it)
  }
}