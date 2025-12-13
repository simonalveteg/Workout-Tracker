package com.alveteg.simon.workouts.db.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alveteg.simon.workouts.utils.FuzzySearch
import com.alveteg.simon.workouts.utils.turnTargetIntoMuscleGroups
import kotlinx.parcelize.Parcelize


@Entity(tableName = "exercises")
@Parcelize
data class Exercise(
  @PrimaryKey(autoGenerate = true)
  var id: Long = 0L,
  var title: String = "Exercise",
  var type: String? = null,
  var force: List<String> = emptyList(),
  var equipment: List<String> = emptyList(),
  var targets: List<String> = emptyList(),
  var synergists: List<String> = emptyList(),
  var stabilizers: List<String> = emptyList()
) : Parcelable {
  fun getPrimaryMuscleGroups(exercise: Exercise = this): List<String> {
    return exercise.targets.flatMap {
      turnTargetIntoMuscleGroups(it)
    }.distinct()
  }

  fun getSecondaryMuscleGroups(exercise: Exercise = this): List<String> {
    return exercise.synergists.flatMap {
      turnTargetIntoMuscleGroups(it)
    }.distinct().filterNot {
      getPrimaryMuscleGroups().contains(it)
    }
  }

  fun getStringMatch(string: String): Boolean {
    return FuzzySearch.regexMatch(string, title)
  }
}