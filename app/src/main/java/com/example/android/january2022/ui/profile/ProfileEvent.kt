package com.example.android.january2022.ui.profile

import android.content.Context
import android.net.Uri
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.SessionExerciseWithExercise
import com.example.android.january2022.ui.session.SessionEvent
import com.example.android.january2022.utils.Event

sealed class ProfileEvent : Event {
    object NavigateToExercises: ProfileEvent()
    data class ExportDatabase(val context: Context, val uri: Uri): ProfileEvent()
    data class ImportDatabase(val context: Context, val uri: Uri): ProfileEvent()
    object CreateFile: ProfileEvent()
    object ImportFile: ProfileEvent()
}