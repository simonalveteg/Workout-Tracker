package com.alveteg.simon.workouts.db

import androidx.core.content.edit


import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.alveteg.simon.workouts.ui.theme.AppTheme
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
    val HAS_DENIED_NOTIFICATIONS = booleanPreferencesKey("has_denied_notifications")
    val RESISTANCE_UNIT = stringPreferencesKey("resistance_unit")
    val APP_THEME = stringPreferencesKey("app_theme")
    val USE_DYNAMIC_COLOR = booleanPreferencesKey("use_dynamic_color")
  }

  val targetFrequency: Flow<Float> = context.dataStore.data
    .map { preferences -> preferences[PreferencesKeys.TARGET_FREQUENCY] ?: 2f }

  val secondaryMuscleWeight: Flow<Float> = context.dataStore.data
    .map { preferences -> preferences[PreferencesKeys.SECONDARY_MUSCLE_WEIGHT] ?: 0.2f }

  val hasDeniedNotifications: Flow<Boolean> = context.dataStore.data
    .map { preferences -> preferences[PreferencesKeys.HAS_DENIED_NOTIFICATIONS] ?: false }

  val resistanceUnit: Flow<ResistanceUnit> = context.dataStore.data
    .map { preferences ->
      val name = preferences[PreferencesKeys.RESISTANCE_UNIT] ?: ResistanceUnit.KG.name
      runCatching { ResistanceUnit.valueOf(name) }.getOrDefault(ResistanceUnit.KG)
    }

  val appTheme: Flow<AppTheme> = context.dataStore.data
    .map { preferences ->
      val name = preferences[PreferencesKeys.APP_THEME] ?: AppTheme.DARK.name
      runCatching { AppTheme.valueOf(name) }.getOrDefault(AppTheme.DARK)
    }

  val useDynamicColor: Flow<Boolean> = context.dataStore.data
    .map { preferences -> preferences[PreferencesKeys.USE_DYNAMIC_COLOR] ?: false }

  suspend fun updateTargetFrequency(value: Float) {
    context.dataStore.edit { it[PreferencesKeys.TARGET_FREQUENCY] = value }
  }

  suspend fun updateSecondaryMuscleWeight(value: Float) {
    context.dataStore.edit { it[PreferencesKeys.SECONDARY_MUSCLE_WEIGHT] = value }
  }

  suspend fun updateHasDeniedNotifications(value: Boolean) {
    context.dataStore.edit { it[PreferencesKeys.HAS_DENIED_NOTIFICATIONS] = value }
  }

  suspend fun updateResistanceUnit(unit: ResistanceUnit) {
    context.dataStore.edit { it[PreferencesKeys.RESISTANCE_UNIT] = unit.name }
  }

  suspend fun updateAppTheme(theme: AppTheme) {
    context.dataStore.edit { it[PreferencesKeys.APP_THEME] = theme.name }
  }

  suspend fun updateUseDynamicColor(enabled: Boolean) {
    context.dataStore.edit { it[PreferencesKeys.USE_DYNAMIC_COLOR] = enabled }
  }
}
