package com.example.android.january2022.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * A SessionExercise can have multiple Set:s associated with it.
 * Each Set is a number of reps with a specific weight (if applicable)
 */
@Entity(tableName = "sets")
data class GymSet(
    @PrimaryKey(autoGenerate = true)
    val setId: Long = 0L,
    val parentSessionExerciseId: Long,
    val reps: Int = -1,
    val weight: Float = -1f,
    val mood: Int = -1
    //TODO: add Time and Distance? Maybe pace and heart-rate? In a second data-class perhaps?
)