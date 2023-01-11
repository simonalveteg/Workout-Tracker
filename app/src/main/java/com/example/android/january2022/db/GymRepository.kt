package com.example.android.january2022.db

import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.ui.rework.ExerciseWrapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest


class GymRepository(
  private val dao: GymDAO
) {

  fun getAllSessions() = dao.getAllSessions()

  @OptIn(ExperimentalCoroutinesApi::class)
  fun getExercisesForSession(session: Session): Flow<List<ExerciseWrapper>> {
    return dao.getExercisesForSession(session.sessionId).mapLatest { list ->
      list.map {
        ExerciseWrapper(
          exercise = it.exercise,
          sets = dao.getSetsForExercise(it.sessionExercise.sessionExerciseId)
        )
      }
    }

  }

  suspend fun insertExercise(exercise: Exercise) = dao.insertExercise(exercise)
}
