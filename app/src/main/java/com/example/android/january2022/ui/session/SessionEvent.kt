package com.example.android.january2022.ui.session

import com.example.android.january2022.db.entities.*
import com.example.android.january2022.ui.home.HomeEvent
import com.example.android.january2022.utils.Event
import java.time.LocalDate
import java.time.LocalDateTime

sealed class SessionEvent : Event {
    data class RemoveSelectedSet(val set: GymSet): SessionEvent()
    data class DateChanged(val newDate: LocalDateTime): SessionEvent()
    data class MoodChanged(val set: GymSet, val newMood: Int): SessionEvent()
    data class SetTypeChanged(val set: GymSet): SessionEvent()
    data class WeightChanged(val set: GymSet, val newWeight: Float): SessionEvent()
    data class RepsChanged(val set: GymSet, val newReps: Int): SessionEvent()
    object OnAddSessionExerciseClicked: SessionEvent()
    data class SetSelectedSessionExercise(val sessionExercise: SessionExerciseWithExercise): SessionEvent()
    data class OnAddSet(val sessionExercise: SessionExerciseWithExercise): SessionEvent()
    object RestoreRemovedSet: SessionEvent()
    object RestoreRemovedSessionExercise: SessionEvent()
    data class EndTimeChanged(val newTime: LocalDateTime): SessionEvent()
    data class StartTimeChanged(val newTime: LocalDateTime): SessionEvent()
    data class OnSessionExerciseInfoClicked(val exerciseId: Long): SessionEvent()
    data class OnSessionExerciseHistoryClicked(val exerciseId: Long): SessionEvent()
    data class OnDeleteSessionExercise(val sessionExercise: SessionExerciseWithExercise): SessionEvent()
}
