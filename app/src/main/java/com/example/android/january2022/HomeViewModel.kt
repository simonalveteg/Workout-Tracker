package com.example.android.january2022

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.january2022.db.GymRepository
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.db.entities.SessionExerciseWithExercise
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GymRepository(application = application)
    val session = MutableLiveData<Session>() // holds info about the Session to be accessed by binding
    val sessionList : LiveData<List<Session>> = repository.getSessions()
    val sessionExerciseList = MutableLiveData<List<SessionExerciseWithExercise>>()



    init {
        Log.d("HVM", "INIT with sessionList: ${sessionList.value}")
    }


    fun clearSessions() {
        viewModelScope.launch {
            repository.deleteAllSessions()
        }
    }

    fun onSessionClicked(newSession: Session) {
        Log.d("HVM","Session clicked!")
        updateSession(newSession)
    }

    fun onNewSession(){
        updateSession(insertSession())
    }

    fun updateSession(newSession: Session) {
        session.value = newSession
        getSessionExerciseList(newSession.sessionId)
    }

    private fun getSessionExerciseList(sessionId: Long) {
        viewModelScope.launch {
            sessionExerciseList.value = withContext(Dispatchers.IO){
                repository.getSessionExercises(sessionId)
            }
        }
    }

    private fun insertSession() : Session {
        val newSession = Session()
        viewModelScope.launch {
            repository.insertSession(newSession)
        }
        return newSession
    }
}