package com.example.android.january2022.db

import androidx.lifecycle.LiveData
import com.example.android.january2022.db.entities.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.forEach


class GymRepository(
    private val dao: GymDAO
) {

    fun getLastExercise() =
        dao.getLastExercise()

    fun getExercise(id: Long) =
        dao.getExercise(id)

    suspend fun getExercisesByQuery(muscleGroup: List<String> = emptyList()): Flow<List<Exercise>> {
        return flow {
            if(muscleGroup.isEmpty()) {
                dao.getExercisesByQuery("%").collect {
                    emit(it)
                }
            } else {
                muscleGroup.forEach { muscle ->
                    dao.getExercisesByQuery(muscle).collect {
                        emit(it)
                    }
                }
            }
        }
    }

    fun getSession(id: Long) =
        dao.getSession(id)

    fun getSessions() =
        dao.getAllSessions()

    fun getAllExercises() =
        dao.getAllExercises()

    fun getSets() =
        dao.getAllSets()

    fun getSessionExercises() =
        dao.getSessionExercisesWithExercise()

    fun getSessionExercisesForSession(sessionId: Long): LiveData<List<SessionExerciseWithExercise>> =
        dao.getSessionExercisesWithExerciseForSession(sessionId)

    fun getMuscleGroupsForSession(sessionId: Long): List<String> {
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

    suspend fun insertSession(item: Session): Long =
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
