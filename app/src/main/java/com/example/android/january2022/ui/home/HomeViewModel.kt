package com.example.android.january2022.ui.home

import androidx.lifecycle.ViewModel
import com.example.android.january2022.db.GymRepository
import com.example.android.january2022.utils.turnTargetIntoMuscleGroups
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  repo: GymRepository,
) : ViewModel() {

  val sessions = combine(repo.getAllSessionExercises(), repo.getAllSessions()) { sewes, sessions ->
    sessions.map { session ->
      val muscleGroups = sewes.filter { sewe ->
        sewe.sessionExercise.parentSessionId == session.sessionId
      }.map { sewe ->
        turnTargetIntoMuscleGroups(sewe.exercise.targets)
      }.flatten()
      Pair(session, muscleGroups)
    }
  }
}