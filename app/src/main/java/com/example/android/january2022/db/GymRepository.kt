package com.example.android.january2022.db

import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.db.entities.SessionExercise
import com.example.android.january2022.ui.rework.DatabaseModel
import com.example.android.january2022.ui.rework.ExerciseWrapper
import com.example.android.january2022.ui.rework.MainViewModel
import com.example.android.january2022.utils.turnTargetIntoMuscleGroup
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
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
    val list = dao.getMuscleGroupsForSession(session.sessionId).map {
      Timber.d("COCK")
      it.split("|").map { muscle ->
        turnTargetIntoMuscleGroup(muscle)
      }.distinct()
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
