package com.example.android.january2022.ui.exercises

import com.example.android.january2022.db.Equipment
import com.example.android.january2022.db.MuscleGroup
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.utils.Event

sealed class ExerciseEvent : Event {
    data class NewExerciseClicked(
        val title: String,
        val muscleGroup: String,
        val equipment: String
    ) : ExerciseEvent()

    data class ExerciseSelected(val exercise: Exercise) : ExerciseEvent()
    data class ExerciseInfoClicked(val exercise: Exercise) : ExerciseEvent()
    data class ExerciseUpdated(val exercise: Exercise) : ExerciseEvent()
    data class FilterExerciseList(val searchString: String) : ExerciseEvent()
    object AddExercisesToSession : ExerciseEvent()
    data class MuscleGroupSelectionChange(val muscleGroup: String) : ExerciseEvent()
    data class EquipmentSelectionChange(val equipment: String) : ExerciseEvent()
}
