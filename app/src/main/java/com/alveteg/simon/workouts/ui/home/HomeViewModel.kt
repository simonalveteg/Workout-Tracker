package com.alveteg.simon.workouts.ui.home

import android.app.Application
import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.alveteg.simon.workouts.R
import com.alveteg.simon.workouts.db.GymRepository
import com.alveteg.simon.workouts.db.UserPreferencesRepository
import com.alveteg.simon.workouts.db.entities.Session
import com.alveteg.simon.workouts.ui.SessionWrapper
import com.alveteg.simon.workouts.utils.Event
import com.alveteg.simon.workouts.utils.Routes
import com.alveteg.simon.workouts.utils.UiEvent
import com.alveteg.simon.workouts.utils.sortedListOfMuscleGroups
import com.alveteg.simon.workouts.worker.SessionReminderWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val repo: GymRepository,
  private val prefsRepo: UserPreferencesRepository,
  private val application: Application
) : ViewModel() {

  val sessions = combine(
    repo.getAllSessionExercises(),
    repo.getAllSessions(),
    prefsRepo.secondaryMuscleWeight
  ) { sewes, sessions, secondaryWeight ->
    sessions.map { session ->
      val muscleGroups = sewes
        .filter { it.sessionExercise.parentSessionId == session.sessionId }
        .sortedListOfMuscleGroups(secondaryWeight.toDouble())
      SessionWrapper(session, muscleGroups)
    }
  }.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    emptyList()
  )

  val tagline: StateFlow<String> = combine(sessions, prefsRepo.targetFrequency) { allSessions, targetFrequency ->
    val cutOffDate = LocalDate.now().minusWeeks(2)
    val recentSessions = allSessions.count { it.session.start.toLocalDate().isAfter(cutOffDate) }
    val isStarter = allSessions.isEmpty() || (recentSessions < targetFrequency.times(2))

    val arrayId = if (isStarter) R.array.home_taglines_starters else R.array.home_taglines
    application.resources.getStringArray(arrayId).random() ?: ""
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = ""
  )
  val greeting: String
   get() = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
    in 5..11 -> "Good Morning"
    in 12..16 -> "Good Afternoon"
    in 17..23 -> "Good Evening"
    else -> "Stay Focused"
  }
  private val _uiEvent = Channel<UiEvent>()
  val uiEvent = _uiEvent.receiveAsFlow()

  fun onEvent(event: Event) {
    when (event) {
      is HomeEvent.SessionClicked -> {
        sendUiEvent(UiEvent.Navigate("${Routes.SESSION}/${event.sessionWrapper.session.sessionId}"))
      }
      is HomeEvent.OpenSettings -> {
        sendUiEvent(UiEvent.Navigate(Routes.SETTINGS))
      }
      is HomeEvent.NewSession -> {
        viewModelScope.launch {
          withContext(Dispatchers.IO) {
            repo.insertSession(Session())
            val session = repo.getLastSession()
            sendUiEvent(UiEvent.Navigate("${Routes.SESSION}/${session.sessionId}"))

            val data = workDataOf("SESSION_ID" to session.sessionId)

            val reminderRequest = OneTimeWorkRequestBuilder<SessionReminderWorker>()
              .setInitialDelay(3, TimeUnit.HOURS)
              .setInputData(data)
              .addTag("session_reminder_${session.sessionId}")
              .build()

            WorkManager.getInstance(application).enqueueUniqueWork(
              "reminder_${session.sessionId}",
              ExistingWorkPolicy.REPLACE,
              reminderRequest
            )
          }
        }
      }
      else -> Unit
    }
  }

  private fun sendUiEvent(event: UiEvent) {
    viewModelScope.launch {
      _uiEvent.send(event)
    }
  }
}