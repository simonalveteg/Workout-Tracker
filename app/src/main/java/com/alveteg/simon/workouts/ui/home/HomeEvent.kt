package com.alveteg.simon.workouts.ui.home

import com.alveteg.simon.workouts.ui.SessionWrapper
import com.alveteg.simon.workouts.utils.Event

sealed class HomeEvent : Event {
  data class SessionClicked(val sessionWrapper: SessionWrapper) : HomeEvent()
  object NewSession : HomeEvent()
  object OpenSettings : HomeEvent()
}