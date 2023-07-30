package com.example.android.january2022.db

object MuscleGroup {
    const val CORE = "Core"
    const val HIPS = "Hips"

    const val CALVES = "Calves"
    const val GLUTES = "Glutes"
    const val HAMSTRINGS = "Hamstrings"
    const val QUADRICEPS = "Quadriceps"

    const val BACK = "Back"
    const val NECK = "Neck"

    const val BICEPS = "Biceps"
    const val FOREARMS = "Forearms and Wrists"
    const val TRICEPS = "Triceps"

    const val CHEST = "Chest"
    const val SHOULDERS = "Shoulders"

    fun getAllMuscleGroups(): List<String> {
        return listOf(
            CORE,
            HIPS,
            CALVES,
            GLUTES,
            HAMSTRINGS,
            QUADRICEPS,
            BACK,
            NECK,
            BICEPS,
            FOREARMS,
            TRICEPS,
            CHEST,
            SHOULDERS
        )
    }
}