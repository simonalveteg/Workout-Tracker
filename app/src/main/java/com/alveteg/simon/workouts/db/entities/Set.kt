package com.alveteg.simon.workouts.db.entities

import androidx.room.ColumnInfo
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
  @ColumnInfo(index = true)
  val parentSessionExerciseId: Long,
  val reps: Int? = null,
  val weight: Float? = null,
  val time: Long? = null,
  val distance: Float? = null,
  val rpe: Rpe? = null
)