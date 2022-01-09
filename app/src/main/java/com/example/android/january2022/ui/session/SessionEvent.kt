package com.example.android.january2022.ui.session

import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.db.entities.SessionExercise
import com.example.android.january2022.db.entities.SessionExerciseWithExercise
import com.example.android.january2022.ui.home.HomeEvent
import com.example.android.january2022.utils.Event

sealed class SessionEvent : Event {
    data class RemoveSelectedSet(val set: GymSet): SessionEvent()
    data class MoodChanged(val set: GymSet, val newMood: Int): SessionEvent()
    data class WeightChanged(val set: GymSet, val newWeight: Float): SessionEvent()
    data class RepsChanged(val set: GymSet, val newReps: Int): SessionEvent()
    object OnAddSessionExerciseClicked: SessionEvent()
    data class SetSelectedSessionExercise(val sessionExercise: SessionExerciseWithExercise): SessionEvent()
    data class OnAddSet(val sessionExercise: SessionExerciseWithExercise): SessionEvent()
}
