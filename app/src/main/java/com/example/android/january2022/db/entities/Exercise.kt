package com.example.android.january2022.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.january2022.db.Equipment
import com.example.android.january2022.utils.FuzzySearch
import com.example.android.january2022.utils.turnTargetIntoMuscleGroups


@Entity(tableName = "exercises")
data class Exercise(
  @PrimaryKey(autoGenerate = true)
  var id: Long = 0L,
  @ColumnInfo(name = "title")
  var title: String = "Exercise",
  var force: String = "",
  @ColumnInfo(defaultValue = Equipment.NULL)
  var equipment: String = Equipment.NULL,
  var targets: List<String> = emptyList(),
  var synergists: List<String> = emptyList(),
  var stabilizers: List<String> = emptyList(),
  var chosenTitle: String = "",
  var favorite: Boolean = false,
  var hidden: Boolean = false
) {
  fun getMuscleGroups(exercise: Exercise = this): List<String> {
    return exercise.targets.flatMap {
      turnTargetIntoMuscleGroups(it)
    }.distinct()
  }

  fun getStringMatch(string: String): Boolean {
    return FuzzySearch.regexMatch(string, title)
  }
}