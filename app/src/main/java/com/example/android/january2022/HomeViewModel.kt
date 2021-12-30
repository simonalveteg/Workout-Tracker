package com.example.android.january2022

import android.app.Application
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.android.january2022.db.GymRepository
import com.example.android.january2022.db.entities.Session
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GymRepository(application = application)
    val sessionList : LiveData<List<Session>> = repository.getAllSessions()

    init {
        Log.d("HVM", "INIT with sessionList: ${sessionList.value}")
    }


    fun clearSessions() {
        viewModelScope.launch {
            repository.deleteAllSessions()
        }
    }

    fun insertSession(session: Session) {
        viewModelScope.launch {
            repository.insertSession(session)
        }
    }
}