package com.alveteg.simon.workouts.ui.settings

import android.content.Context
import android.net.Uri
import com.alveteg.simon.workouts.utils.Event

sealed class SettingsEvent : Event {
  object ClearDatabase: SettingsEvent()
}
