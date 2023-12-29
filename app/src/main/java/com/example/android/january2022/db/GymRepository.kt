package com.example.android.january2022.db

import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.db.entities.SessionExercise
import com.example.android.january2022.db.entities.SessionExerciseWithExercise
import com.example.android.january2022.ui.DatabaseModel
import com.example.android.january2022.utils.turnTargetIntoMuscleGroups
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapNotNull
import timber.log.Timber

class GymRepository(
    private val dao: GymDAO,
) {

    fun getSessionById(sessionId: Long) = dao.getSessionById(sessionId)

    fun getAllSessions() = dao.getAllSessions()

    fun getAllSets() = dao.getAllSets()
    fun getAllExercises() = dao.getAllExercises()

    fun getLastSession() = dao.getLastSession()

    fun getAllSessionExercises() = dao.getAllSessionExercises()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getExercisesForSession(session: Flow<Session>): Flow<List<SessionExerciseWithExercise>> {
        return session.flatMapLatest {
            dao.getExercisesForSession(it.sessionId)
        }
    }

    fun getExercisesForSession(session: Session): Flow<List<SessionExerciseWithExercise>> {
        Timber.d("Retrieving exercises for session: $session")
        return dao.getExercisesForSession(session.sessionId)
    }

    fun getSetsForExercise(sessionExerciseId: Long) = dao.getSetsForExercise(sessionExerciseId)

    fun getMuscleGroupsForSession(session: Session): Flow<List<String>> {
        val list = dao.getMuscleGroupsForSession(session.sessionId).mapNotNull {
            Timber.d("MuscleGroup flow created.")
            try {
                turnTargetIntoMuscleGroups(it)
            } catch (_: Exception) {
                Timber.d("Error when converting target.")
                emptyList()
            }
        }
        return list
    }

    suspend fun insertExercise(exercise: Exercise) = dao.insertExercise(exercise)

    suspend fun insertSession(session: Session) = dao.insertSession(session)

    suspend fun removeSession(session: Session) = dao.removeSession(session)

    suspend fun updateSession(session: Session) = dao.updateSession(session)

    suspend fun insertSessionExercise(sessionExercise: SessionExercise) =
        dao.insertSessionExercise(sessionExercise)

    suspend fun removeSessionExercise(sessionExercise: SessionExercise) =
        dao.removeSessionExercise(sessionExercise)

    suspend fun insertSet(gymSet: GymSet) = dao.insertSet(gymSet)

    suspend fun updateSet(set: GymSet) = dao.updateSet(set)
    suspend fun deleteSet(set: GymSet) = dao.deleteSet(set)

    suspend fun createSet(sessionExercise: SessionExercise) =
        dao.insertSet(GymSet(parentSessionExerciseId = sessionExercise.sessionExerciseId))

    fun getDatabaseModel() =
        DatabaseModel(
            sessions = dao.getSessionList(),
            exercises = dao.getExerciseList(),
            sessionExercises = dao.getSessionExerciseList(),
            sets = dao.getSetList(),
        )

    suspend fun clearDatabase() {
        dao.clearSessions()
        dao.clearSessionExercises()
        dao.clearExercises()
        dao.clearSets()
    }
}
