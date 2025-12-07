package com.alveteg.simon.workouts.db.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
  tableName = "sessionExercises",
  foreignKeys = [
    ForeignKey(
      entity = Session::class,
      parentColumns = ["sessionId"],
      childColumns = ["parentSessionId"],
      onDelete = ForeignKey.CASCADE
    )
  ]
)
@Parcelize
data class SessionExercise(
  @PrimaryKey(autoGenerate = true)
  val sessionExerciseId: Long = 0,
  @ColumnInfo(index = true)
  val parentSessionId: Long,
  @ColumnInfo(index = true)
  val parentExerciseId: Long,
  @ColumnInfo(defaultValue = "-1")
  val exerciseOrder: Int = -1,
  val comment: String? = null
) : Parcelable

/**
 * Holds a sessionExercise and it's associated exercise. Embedded = bad? it works though.
 */
data class SessionExerciseWithExercise(
  @Embedded
  val sessionExercise: SessionExercise,
  @Embedded
  val exercise: Exercise
)

data class SessionWithSessionExerciseWithExercise(
  @Embedded val session: Session,
  @Embedded val sessionExercise: SessionExercise,
  @Embedded val exercise: Exercise
)
