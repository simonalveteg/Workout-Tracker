package com.example.android.january2022.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.january2022.db.Equipment
import com.example.android.january2022.db.SetType


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
    val time: Long = -1L,
    val distance: Float = -1f,
    val mood: Int = -1,
    val deleted: Boolean = false,
    @ColumnInfo(defaultValue = SetType.NORMAL)
    val setType: String = SetType.NORMAL
    //TODO: add Time and Distance? Maybe pace and heart-rate? In a second data-class perhaps?
)