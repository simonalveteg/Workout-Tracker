package com.example.android.january2022.db

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.january2022.db.entities.*
import com.example.android.january2022.utils.turnTargetIntoMuscleGroup
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class GymRepository(
    private val dao: GymDAO
) {

    suspend fun updateSession(session: Session) = dao.updateSession(session)

    fun getLastExercise() =
        dao.getLastExercise()

    fun getExercise(id: Long) =
        dao.getExercise(id)

    fun getExercisesWithCount() =
        dao.getExercisesWithCount()

    suspend fun getExercisesByQuery(
        muscleGroup: String = "%",
        equipment: String = "%",
        query: String = "%",
    ): Flow<List<Exercise>> {
        val equip = "%${equipment}%"
        val que = "%${query}%"

        return flow {
            dao.getExercisesByQuery(equip, que).collect {
                emit(it.filter { exercise ->
                    var match = false
                    exercise.targets.map { turnTargetIntoMuscleGroup(it) }.forEach {
                        if (!it.lowercase().contains(muscleGroup.lowercase())) return@forEach
                        match = true
                    }
                    match
                })
            }
        }
    }

    fun getSession(id: Long) =
        dao.getSession(id)

    fun getAllSessions() =
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
        val muscleGroups = mutableListOf<String>()
        dao.getSessionMuscleGroups(sessionId).forEach {
            turnTargetIntoMuscleGroup(it).split(", ").forEach { muscleGroups.add(it) }
        }
        val muscleGroupsByCount = muscleGroups.groupingBy { it }.eachCount()
        return muscleGroupsByCount.entries.sortedBy { it.value }.map { it.key }.reversed()
    }

    fun getSessionExercisesForExercise(id: Long) =
        dao.getSessionExercisesForExercise(id)

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

    suspend fun insertExercise(exercise: Exercise): Long =
        dao.insertExercise(exercise)

    suspend fun insertSessionExercise(sessionExercise: SessionExercise) =
        dao.insertSessionExercise(sessionExercise)

    suspend fun removeSession(session: Session) =
        dao.removeSession(session)

    suspend fun removeSet(set: GymSet) =
        dao.removeSet(set)

    suspend fun removeSessionExercise(sessionExercise: SessionExercise) =
        dao.removeSessionExercise(sessionExercise)

    fun getSessionList() = dao.getSessionList()
    fun getExerciseList() = dao.getExerciseList()
    fun getSessionExerciseList() = dao.getSessionExerciseList()
    fun getSetList() = dao.getSetList()

    suspend fun clearDatabase() {
        dao.clearSessions()
        dao.clearSessionExercises()
        dao.clearExercises()
        dao.clearSets()
    }


    /** Workout timer and it's related functions*/

    val timerIsRunning = MutableLiveData(false)
    val timerTime = MutableLiveData(0L)
    val timerMaxTime = MutableLiveData(60000L)
    var timer: WorkoutTimer? = null
        private set

    fun startTimer() {
        timer?.cancel()
        timer = WorkoutTimer(timerMaxTime.value ?: 0).apply { start() }
        timerIsRunning.value = true
    }

    fun resumeTimer() {
        timer?.cancel()
        timer = WorkoutTimer(timerTime.value ?: 0).apply { start() }
        timerIsRunning.value = true
    }

    fun stopTimer() {
        timer?.cancel()
        timerIsRunning.value = false
    }

    fun resetTimer() {
        timer?.cancel()
        timer = null
        timerTime.value = 0L
        timerIsRunning.value = false
    }

    inner class WorkoutTimer(
        time: Long,
        interval: Long = 1000L
    ) : CountDownTimer(time, interval) {

        override fun onTick(millisUntilFinished: Long) {
            timerTime.value = millisUntilFinished
            if (timerTime.value == 0L) onFinish()
        }

        override fun onFinish() {
            timerIsRunning.value = false
            timer = null
            timerTime.value = 0
        }
    }

}
