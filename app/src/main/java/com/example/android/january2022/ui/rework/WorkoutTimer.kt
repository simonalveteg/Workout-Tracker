package com.example.android.january2022.ui.rework

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData

class WorkoutTimer {

  val timerIsRunning = MutableLiveData(false)
  val timerTime = MutableLiveData(0L)
  val timerMaxTime = MutableLiveData(60000L)
  var timer: WorkoutTimer? = null
    private set

  fun startTimer() {
    timer?.cancel()
    timer = WorkoutTimer(timerMaxTime.value ?: 0).apply { start() }
    timerIsRunning.value = true
  }

  fun resumeTimer() {
    timer?.cancel()
    timer = WorkoutTimer(timerTime.value ?: 0).apply { start() }
    timerIsRunning.value = true
  }

  fun stopTimer() {
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