package com.example.android.january2022.db

import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.db.entities.SessionExercise
import com.example.android.january2022.db.entities.SessionExerciseWithExercise
import com.example.android.january2022.ui.DatabaseModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

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
