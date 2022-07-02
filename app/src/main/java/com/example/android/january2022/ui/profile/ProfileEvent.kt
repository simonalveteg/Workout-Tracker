package com.example.android.january2022.ui.profile

import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.SessionExerciseWithExercise
import com.example.android.january2022.ui.session.SessionEvent
import com.example.android.january2022.utils.Event

sealed class ProfileEvent : Event {
    object NavigateToExercises: ProfileEvent()
    object ExportDatabase: ProfileEvent()
}