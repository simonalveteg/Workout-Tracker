package com.example.android.january2022.db

import com.example.android.january2022.db.entities.Exercise


class GymRepository(
  private val dao: GymDAO
) {

  fun getAllSessions() = dao.getAllSessions()

  suspend fun insertExercise(exercise: Exercise) = dao.insertExercise(exercise)
}
