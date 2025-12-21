package com.alveteg.simon.workouts.ui.settings

import android.content.Context
import android.net.Uri
import com.alveteg.simon.workouts.utils.Event

sealed class SettingsEvent : Event {
  data class ImportDatabase(val context: Context, val uri: Uri): SettingsEvent()
  object ClearDatabase: SettingsEvent()
}
