package com.example.android.january2022.ui.rework

import android.os.CountDownTimer
import kotlinx.coroutines.flow.MutableStateFlow

class WorkoutTimer {

  val timerIsRunning = MutableStateFlow(false)
  val timerTime = MutableStateFlow(0L)
  val timerMaxTime = MutableStateFlow(60000L)
  var timer: WorkoutTimer? = null
    private set

  fun toggle() {
    if (timerIsRunning.value) stop() else resume()
  }

  fun start() {
    timer?.cancel()
    timer = WorkoutTimer(timerMaxTime.value).apply { start() }
    timerIsRunning.value = true
  }

  fun resume() {
    timer?.cancel()
    timer = WorkoutTimer(timerTime.value).apply { start() }
    timerIsRunning.value = true
  }

  fun stop() {
    timer?.cancel()
    timerIsRunning.value = false
  }

  fun resetTimer() {
    timer?.cancel()
    timer = null
    timerTime.value = 0L
    timerIsRunning.value = false
  }

  inner class WorkoutTimer(
    time: Long,
    interval: Long = 1000L
  ) : CountDownTimer(time, interval) {

    override fun onTick(millisUntilFinished: Long) {
      timerTime.value = millisUntilFinished
      if (timerTime.value == 0L) onFinish()
    }

    override fun onFinish() {
      timerIsRunning.value = false
      timer = null
      timerTime.value = 0
    }
  }
}