package com.alveteg.simon.workouts.db

import androidx.core.content.edit


import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "settings")

@Singleton
class UserPreferencesRepository @Inject constructor(
  @ApplicationContext private val context: Context
) {
  private object PreferencesKeys {
    val TARGET_FREQUENCY = floatPreferencesKey("target_workout_frequency")
    val SECONDARY_MUSCLE_WEIGHT = floatPreferencesKey("secondary_muscle_weight")
  }

  val targetFrequency: Flow<Float> = context.dataStore.data
    .map { preferences -> preferences[PreferencesKeys.TARGET_FREQUENCY] ?: 2f }

  val secondaryMuscleWeight: Flow<Float> = context.dataStore.data
    .map { preferences -> preferences[PreferencesKeys.SECONDARY_MUSCLE_WEIGHT] ?: 0.2f }

  suspend fun updateTargetFrequency(value: Float) {
    context.dataStore.edit { it[PreferencesKeys.TARGET_FREQUENCY] = value }
  }

  suspend fun updateSecondaryMuscleWeight(value: Float) {
    context.dataStore.edit { it[PreferencesKeys.SECONDARY_MUSCLE_WEIGHT] = value }
  }
}
