package com.example.android.january2022.ui.rework

import android.os.CountDownTimer
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import kotlin.math.roundToInt

class WorkoutTimer {

  val isRunning = MutableStateFlow(false)
  val time = MutableStateFlow(0L)
  val maxTime = MutableStateFlow(60000L)
  private val increment = 30*1000L
  private val maxLength = 60*60*1000L
  private var timer: WorkoutTimer? = null

  fun toggle() {
    if (isRunning.value) stop()
    else if (time.value == 0L) start() else resume()
  }

  fun increment() {
    maxTime.value += increment
    time.value += increment
  }

  fun decrement() {
    if (!isRunning.value) {
      maxTime.value -= increment
    } else if (time.value <= increment) reset() else {
      time.value -= increment
    }
  }

  fun start() {
    timer?.cancel()
    timer = WorkoutTimer().apply { start() }
    isRunning.value = true
  }

  fun resume() {
    timer?.cancel()
    timer = WorkoutTimer()
    timer?.start()
    isRunning.value = true
  }

  fun stop() {
    timer?.cancel()
    isRunning.value = false
  }

  fun reset() {
    timer?.cancel()
    timer = null
    time.value = 0L
    isRunning.value = false
  }

  inner class WorkoutTimer(
    length: Long = maxLength,
    interval: Long = 1000L
  ) : CountDownTimer(length, interval) {


    override fun onTick(millisUntilFinished: Long) {
      time.value = maxTime.value-(maxLength-millisUntilFinished)
      Timber.d("onTick: ${time.value}")
      if (time.value <= 0L) onFinish()
    }

    override fun onFinish() {
      Timber.d("onFinish")
      reset()
    }
  }
}

fun Long.toTimerString(): String {
  val totalSeconds = this.toFloat().div(1000).roundToInt()
  val minutes = totalSeconds / 60
  val seconds = totalSeconds % 60
  val displayedSeconds = if (seconds < 10) "0$seconds" else seconds
  return "$minutes:$displayedSeconds"
}