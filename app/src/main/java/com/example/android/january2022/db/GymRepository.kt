package com.example.android.january2022.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.android.january2022.db.entities.*


class GymRepository(
    private val dao: GymDAO
) {

    fun getLastExercise() =
        dao.getLastExercise()

    fun getExercise(id: Long) =
        dao.getExercise(id)

    fun getExercisesByQuery(searchString: String = "") =
        dao.getExercisesByQuery(searchString)

    fun getSession(id: Long) =
        dao.getSession(id)

    fun getSessions() =
        dao.getAllSessions()

    fun getExercises() =
        dao.getAllExercises()

    fun getSets() =
        dao.getAllSets()

    fun getSessionExercises() =
        dao.getSessionExercisesWithExercise()

    fun getSessionExercisesForSession(sessionId: Long) : LiveData<List<SessionExerciseWithExercise>> =
        dao.getSessionExercisesWithExerciseForSession(sessionId)

    fun getMuscleGroupsForSession(sessionId: Long) : List<String> {
        val muscleGroups = dao.getSessionMuscleGroups(sessionId)
        val sb = StringBuilder()
        muscleGroups.forEach {
            sb.append(it)
            sb.append(", ")
        }
        val muscleGroupsByCount = sb.split(", ").groupingBy { it }.eachCount()
        return muscleGroupsByCount.entries.sortedBy { it.value }.map { it.key }
    }

    fun getSetsForSessionExercise(id: Long) =
        dao.getSetsForSessionExercise(id)

    fun getSetsForSession(id: Long) =
        dao.getSetsForSession(id)

    suspend fun updateSet(set: GymSet) =
        dao.updateSet(set)

    suspend fun insertSet(item: GymSet) =
        dao.insertSet(item)

    suspend fun insertSession(item: Session) : Long =
        dao.insertSession(item)

    suspend fun insertExercise(exercise: Exercise) =
        dao.insertExercise(exercise)

    suspend fun insertSessionExercise(sessionExercise: SessionExercise) =
        dao.insertSessionExercise(sessionExercise)

    suspend fun removeSet(set: GymSet) =
        dao.removeSet(set)

    suspend fun removeSessionExercise(sessionExercise: SessionExercise) =
        dao.removeSessionExercise(sessionExercise)

}
