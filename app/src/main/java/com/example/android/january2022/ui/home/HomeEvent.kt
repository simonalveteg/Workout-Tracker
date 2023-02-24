package com.example.android.january2022.ui.home

import com.example.android.january2022.ui.SessionWrapper
import com.example.android.january2022.utils.Event

sealed class HomeEvent : Event {
  data class SessionClicked(val sessionWrapper: SessionWrapper) : HomeEvent()
  object NewSession : HomeEvent()
  object OpenSettings : HomeEvent()
}