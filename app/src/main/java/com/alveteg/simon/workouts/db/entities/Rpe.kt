package com.alveteg.simon.workouts.db.entities

sealed class Rpe(val value: Int) {
  data object Level1 : Rpe(1)
  data object Level2 : Rpe(2)
  data object Level3 : Rpe(3)
  data object Level4 : Rpe(4)
  data object Level5 : Rpe(5)
  data object Level6 : Rpe(6)
  data object Level7 : Rpe(7)
  data object Level8 : Rpe(8)
  data object Level9 : Rpe(9)
  data object Level10 : Rpe(10)

  companion object {
    private val levels = listOf(
      Level1, Level2, Level3, Level4, Level5,
      Level6, Level7, Level8, Level9, Level10
    )

    private val valueMap = levels.associateBy { it.value }

    /**
     * Converts an integer from the database into a type-safe [Rpe] object.
     * Returns null if the value is not a valid RPE level.
     */
    fun fromInt(value: Int?): Rpe? {
      return value?.let { valueMap[it] }
    }

    /**
     * Returns all possible RPE levels.
     */
    fun getLevels(): List<Rpe> = levels
  }
}

