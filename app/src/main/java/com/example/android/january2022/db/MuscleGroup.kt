package com.example.android.january2022.db

object MuscleGroup {
    const val BICEPS = "Biceps"
    const val CHEST = "Chest"
    const val TRICEPS = "Triceps"
    const val SHOULDERS = "Shoulders"
    const val FOREARMS = "Forearms"
    const val CALVES = "Calves"
    const val ABDOMINALS = "Abdominals"
    const val HIPS = "Hips"
    const val BACK = "Back"
    const val THIGHS = "Thighs"
    const val NECK = "Neck"
    const val NULL = "not specified"

    fun getAllMuscleGroups(): List<String> {
        return listOf(
            BICEPS,
            CHEST,
            TRICEPS,
            SHOULDERS,
            FOREARMS,
            CALVES,
            ABDOMINALS,
            HIPS,
            BACK,
            THIGHS,
            NECK
        )
    }
}