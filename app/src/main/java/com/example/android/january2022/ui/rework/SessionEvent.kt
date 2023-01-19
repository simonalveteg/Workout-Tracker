package com.example.android.january2022.ui.rework

import com.example.android.january2022.db.SetType
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.utils.Event

sealed class SessionEvent : Event {
  data class ExerciseSelection(val exercise: ExerciseWrapper) : SessionEvent()
  data class SetTypeChanged(val set: GymSet, val setType: String) : SessionEvent()
}