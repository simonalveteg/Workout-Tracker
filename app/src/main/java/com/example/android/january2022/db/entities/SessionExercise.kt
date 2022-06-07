package com.example.android.january2022.db.entities

import androidx.room.*

@Entity(
    tableName = "sessionExercises",
)
data class SessionExercise(
    @PrimaryKey(autoGenerate = true) var sessionExerciseId: Long = 0,

    val sessionExerciseText: String = "cock",

    @ColumnInfo(index = true) val parentSessionId: Long,
    @ColumnInfo(index = true)
    val parentExerciseId: Long,
    val comment: String = ""
)

/**
 * Holds a sessionExercise and it's associated exercise. Embedded = bad? it works though.
 */
data class SessionExerciseWithExercise(
    @Embedded
    val sessionExercise: SessionExercise,
    @Embedded
    val exercise: Exercise
)
