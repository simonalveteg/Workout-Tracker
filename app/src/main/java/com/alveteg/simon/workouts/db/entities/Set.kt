package com.alveteg.simon.workouts.db.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


/**
 * A SessionExercise can have multiple Set:s associated with it.
 * Each Set is a number of reps with a specific weight (if applicable)
 */
@Parcelize
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
) : Parcelable