package com.example.android.january2022.db

import android.app.Application
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.GymSet
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

    fun getSets() =
        db.gymDatabaseDAO.getAllSets()

    fun getSessionExercises() =
        db.gymDatabaseDAO.getSessionExercisesWithExercise()

    fun getSessionExercisesForSession(sessionId: Long) =
        db.gymDatabaseDAO.getSessionExercisesWithExerciseForSession(sessionId)

    fun deleteAllData() =
        db.clearAllTables()

    suspend fun insertSet(item: GymSet) =
        db.gymDatabaseDAO.insertSet(item)

    suspend fun insertSession(item: Session) : Long =
        db.gymDatabaseDAO.insertSession(item)

    suspend fun insertExercise(exercise: Exercise) =
        db.gymDatabaseDAO.insertExercise(exercise)

    suspend fun insertSessionExercise(sessionExercise: SessionExercise) =
        db.gymDatabaseDAO.insertSessionExercise(sessionExercise)
}
