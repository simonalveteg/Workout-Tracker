package com.example.android.january2022.ui.rework

import android.os.CountDownTimer
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.coroutineContext
import kotlin.math.roundToInt

class WorkoutTimer {

  val isRunning = MutableStateFlow(false)
  val time = MutableStateFlow(0L)
  val maxTime = MutableStateFlow(60000L)
  val finished = Channel<Boolean>()
  private val increment = 30 * 1000L
  private var timer: WorkoutTimer? = null

  fun toggle() {
    if (isRunning.value) stop()
    else if (time.value <= 0L) start() else resume()
  }

  fun increment() {
    if (isRunning.value) {
      stop()
      maxTime.value += increment
      time.value += increment
      resume()
    } else {
      maxTime.value += increment
      if (time.value > 0L) time.value += increment
    }
  }

  fun decrement() {
    if (isRunning.value) {
      stop()
      time.value = time.value.minus(increment).coerceAtLeast(0L)
      if (time.value <= 0L) reset() else resume()
    } else {
      maxTime.value = maxTime.value.minus(increment).coerceAtLeast(0L)
      time.value = time.value.minus(increment).coerceAtLeast(0L)
    }
  }

  fun start() {
    timer?.cancel()
    timer = WorkoutTimer(maxTime.value).apply { start() }
    isRunning.value = true
  }

  fun resume() {
    timer?.cancel()
    timer = WorkoutTimer(time.value)
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
    length: Long,
    interval: Long = 1000L
  ) : CountDownTimer(length, interval) {


    override fun onTick(millisUntilFinished: Long) {
      time.value = millisUntilFinished
      if (time.value <= 0L) onFinish()
    }

    override fun onFinish() {
      runBlocking {
        finished.send(true)
      }
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