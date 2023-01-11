package com.example.android.january2022.db

import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.ui.rework.ExerciseWrapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import timber.log.Timber


class GymRepository(
  private val dao: GymDAO
) {

  fun getAllSessions() = dao.getAllSessions()

  @OptIn(ExperimentalCoroutinesApi::class)
  fun getExercisesForSession(session: Session): Flow<List<ExerciseWrapper>> {
    Timber.d("Retrieving exercises for session: $session")
    return dao.getExercisesForSession(session.sessionId).mapLatest { list ->
      Timber.d("Wrapping latest available list")
      list.map {
        ExerciseWrapper(
          sessionExercise = it.sessionExercise,
          exercise = it.exercise,
          sets = dao.getSetsForExercise(it.sessionExercise.sessionExerciseId)
        )
      }
    }
  }

  suspend fun insertExercise(exercise: Exercise) = dao.insertExercise(exercise)
}
