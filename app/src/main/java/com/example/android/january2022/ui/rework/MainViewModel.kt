package com.example.android.january2022.ui.rework

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.january2022.db.GymRepository
import com.example.android.january2022.db.entities.Session
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val repo: GymRepository
) : ViewModel() {

  private val _uiState = MutableStateFlow(
    UiState(
      sessions = repo.getAllSessions(),
      currentSession = MutableStateFlow(Session(14)),
    )
  )
  val uiState: StateFlow<UiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      withContext(Dispatchers.IO) {
        _uiState.value.sessions.collectLatest { sessions ->
          _uiState.update { state ->
            state.copy(
              currentSession = flowOf(sessions.last())
            )
          }
        }
      }
    }
  }


  data class UiState(
    val sessions: Flow<List<Session>>,
    val currentSession: Flow<Session>
  )

}