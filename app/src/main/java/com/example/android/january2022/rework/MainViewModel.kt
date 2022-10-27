package com.example.android.january2022.rework

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import com.example.android.january2022.db.GymRepository
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val repository: GymRepository,
) : ViewModel() {

  private val _uiState = MutableStateFlow(
    UiState(
      sessions = repository.getAllSessions().asFlow()
    )
  )
  val uiState: StateFlow<UiState> = _uiState.asStateFlow()


  fun onEvent(event: Event) {
    Log.d("VIEW_MODEL", event.toString())
    when (event) {
      else -> Unit
    }
  }

  data class UiState(
    val sessions: Flow<List<Session>>
  )

}