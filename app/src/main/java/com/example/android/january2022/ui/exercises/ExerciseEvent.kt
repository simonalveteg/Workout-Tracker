package com.example.android.january2022.ui.exercises

import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.utils.Event

sealed class ExerciseEvent : Event {
    data class NewExerciseClicked(val title: String): ExerciseEvent()
    data class ExerciseSelected(val exercise: Exercise): ExerciseEvent()

}
