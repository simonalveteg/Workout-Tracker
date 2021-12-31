package com.example.android.january2022.db

import android.app.Application
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.SessionExercise
import com.example.android.january2022.db.entities.Session


class GymRepository(
    application: Application
) {    private var db: GymDatabase

    init {
        val database = GymDatabase.getInstance(application)
        db = database
    }

    fun getSessions() =
        db.gymDatabaseDAO.getAllSessions()

    fun getExercises() =
        db.gymDatabaseDAO.getAllExercises()

    fun getSessionExercises(sessionId: Long) =
        db.gymDatabaseDAO.getSessionExercisesWithExercise(sessionId)

    suspend fun deleteAllSessions() =
        db.gymDatabaseDAO.clearSessions()

    suspend fun insertSession(item: Session) : Long =
        db.gymDatabaseDAO.insertSession(item)

    suspend fun insertExercise(exercise: Exercise) =
        db.gymDatabaseDAO.insertExercise(exercise)

    suspend fun insertSessionExercise(sessionExercise: SessionExercise) =
        db.gymDatabaseDAO.insertSessionExercise(sessionExercise)
}
