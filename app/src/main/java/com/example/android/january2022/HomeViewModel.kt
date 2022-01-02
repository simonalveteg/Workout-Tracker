package com.example.android.january2022

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.january2022.db.GymRepository
import com.example.android.january2022.db.entities.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.random.Random


class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GymRepository(application = application)
    val sessionId = MutableLiveData<Long>()

    //val session = MutableLiveData<Session>()
    val sessionList: LiveData<List<Session>> = repository.getSessions()
    val exerciseList: LiveData<List<Exercise>> = repository.getExercises()
    val sessionExerciseList: LiveData<List<SessionExerciseWithExercise>> =
        repository.getSessionExercises()
    val currentSessionExerciseList = MutableLiveData<List<SessionExerciseWithExercise>>()


    init {
        Log.d("HVM", "INIT with sessionList: ${sessionList.value}")
    }

    fun onNewExercise(exerciseTitle: String) {
        viewModelScope.launch {
            repository.insertExercise(Exercise(0, exerciseTitle))
        }
    }

    fun clearDatabase() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteAllData()
            }
        }
    }

    fun onExerciseClicked(exercise: Exercise) {
        val newSessionExercise =
            SessionExercise(
                parentExerciseId = exercise.exerciseId,
                parentSessionId = sessionId.value!!
            )
        viewModelScope.launch {
            repository.insertSessionExercise(newSessionExercise)
        }
        updateCurrentSessionExerciseList()
    }

    fun onSessionClicked(newSessionId: Long) {
        Log.d("HVM", "Session clicked!")
        sessionId.value = newSessionId
        updateCurrentSessionExerciseList()
    }

    fun onNewSession(session: Session = Session()) {
        // Use runBlocking when inserting a new session to ensure that
        // the sessionId gets it's value updated before anything else gets executed
        runBlocking { sessionId.value = insertSession(session) }
        Log.d("HVM", "New session created with id ${sessionId.value}")
        updateCurrentSessionExerciseList()
    }

    /**
     * ONLY WORKS AFTER CLEARING APP STORAGE ON PHONE!
     * depends on the Exercise ID being between 0 and exercises.size
     */
    fun populateDatabase() {
        val exercises = listOf(
            "Abduction",
            "Ab Crunch",
            "Adduction",
            "Armhävningar",
            "Axellyft",
            "Axelpress",
            "Axelrotation",
            "Bålrotator",
            "Bänkpress",
            "Benlyft",
            "Benpress",
            "Bicep Curls",
            "Biceps Pulldown",
            "Bilateral Arm Curl",
            "Bröstpress",
            "Cykel",
            "Deadbugs",
            "Deadlift",
            "Deltoid fly",
            "Deltoid Raise",
            "Dumbbell crunch",
            "Dumbbell flies",
            "Dumbell Lunges",
            "Forearm curls",
            "Forearm twist",
            "Hammer Curls",
            "Hantelpress",
            "Squats",
            "Kneeling one arm dumbbell row",
            "Lat Pulldown",
            "Leg Curl",
            "Leg Extension",
            "Leg Press",
            "Leg Raise",
            "Lounges",
            "Low Row",
            "Militärpress",
            "Medicine ball twist",
            "Narrow squats",
            "Pec fly",
            "Bulgarian squats",
            "Plank",
            "Sideplank",
            "Pullups",
            "Pushups",
            "Row",
            "Russian twist",
            "Rygglyft",
            "Leg curl"
        )
        exercises.forEach {
            onNewExercise(it)
        }
        // create sessions and add random SessionExercises to them
        val cal = Calendar.getInstance().timeInMillis
        val day = 76400000L
        runBlocking {
            val offset = withContext(Dispatchers.IO) {repository.getLastExercise().exerciseId}
            listOf(
                Session(0, cal - 25 * day),
                Session(0, cal - 16 * day),
                Session(0, cal - 14 * day),
                Session(0, cal - 12 * day),
                Session(0, cal - 8 * day),
                Session(0, cal)
            ).forEach {
                onNewSession(it)
                repeat(Random.nextInt(2, 8)) {
                    // create a new exercise object with a random ID within the range
                    // in OnExerciseClicked a new SessionExercise object is created which uses
                    // this id (not the object itself!)
                    val exercise = Exercise(offset + Random.nextLong(1L, exercises.size - 1L))
                    Log.d("HVM", "Exercise $exercise")
                    onExerciseClicked(exercise)
                }
            }
        }

    }

    private fun updateCurrentSessionExerciseList() {
        viewModelScope.launch {
            currentSessionExerciseList.value = withContext(Dispatchers.IO) {
                repository.getSessionExercisesForSession(sessionId.value!!)
            }
        }
    }

    private suspend fun insertSession(newSession: Session): Long {
        return withContext(Dispatchers.IO) {
            repository.insertSession(newSession)
        }
    }
}