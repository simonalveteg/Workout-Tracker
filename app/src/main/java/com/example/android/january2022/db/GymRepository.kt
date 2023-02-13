package com.example.android.january2022.db

import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.db.entities.SessionExercise
import com.example.android.january2022.ui.rework.DatabaseModel
import com.example.android.january2022.ui.rework.ExerciseWrapper
import com.example.android.january2022.ui.rework.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.toList
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

  fun getMuscleGroupsForSession(session: Session): Flow<List<String>> {
    val list = dao.getExercisesForSession(session.sessionId).map { list ->
      list.map {
        it.exercise.getMuscleGroup()
      }
        .groupingBy { it }.eachCount().toList()
        .sortedByDescending { (_, v) -> v }
        .map { it.first }
    }
    return list
  }

  fun getDatabaseModel() =
    DatabaseModel(
      sessions = dao.getSessionList(),
      exercises = dao.getExerciseList(),
      sessionExercises = dao.getSessionExerciseList(),
      sets = dao.getSetList()
    )

  suspend fun insertExercise(exercise: Exercise) = dao.insertExercise(exercise)

  suspend fun insertSession(session: Session) = dao.insertSession(session)

  suspend fun insertSessionExercise(sessionExercise: SessionExercise) =
    dao.insertSessionExercise(sessionExercise)

  suspend fun insertSet(gymSet: GymSet) = dao.insertSet(gymSet)

  suspend fun clearDatabase() {
    dao.clearSessions()
    dao.clearSessionExercises()
    dao.clearExercises()
    dao.clearSets()
  }

  suspend fun updateSet(set: GymSet) = dao.updateSet(set)

  suspend fun createSet(sessionExercise: SessionExercise) =
    dao.insertSet(GymSet(parentSessionExerciseId = sessionExercise.sessionExerciseId))
}
