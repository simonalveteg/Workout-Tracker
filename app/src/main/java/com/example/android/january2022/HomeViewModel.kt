package com.example.android.january2022

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.january2022.db.GymRepository
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.db.entities.SessionExercise
import com.example.android.january2022.db.entities.SessionExerciseWithExercise
import kotlinx.coroutines.*


class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GymRepository(application = application)
    val sessionId = MutableLiveData<Long>()
    //val session = MutableLiveData<Session>()
    val sessionList: LiveData<List<Session>> = repository.getSessions()
    val exerciseList: LiveData<List<Exercise>> = repository.getExercises()
    val sessionExerciseList = MutableLiveData<List<SessionExerciseWithExercise>>()


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
        updateSessionExerciseList()
    }

    fun onSessionClicked(newSession: Session) {
        Log.d("HVM", "Session clicked!")
        sessionId.value = newSession.sessionId
        updateSessionExerciseList()
    }

    fun onNewSession() {
        // Use runBlocking when inserting a new session to ensure that
        // the sessionId gets it's value updated before anything else gets executed
        runBlocking {  sessionId.value = insertSession(Session()) }
        Log.d("HVM", "New session created with id ${sessionId.value}")
        updateSessionExerciseList()
    }

    fun importExercises() {
        val exercises = listOf<String>(
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
    }

    private fun updateSessionExerciseList() {
        viewModelScope.launch {
            sessionExerciseList.value = withContext(Dispatchers.IO) {
                repository.getSessionExercises(sessionId.value!!)
            }
        }
    }

    private suspend fun insertSession(newSession: Session): Long {
        return withContext(Dispatchers.IO) {
            repository.insertSession(newSession)
        }
    }
}