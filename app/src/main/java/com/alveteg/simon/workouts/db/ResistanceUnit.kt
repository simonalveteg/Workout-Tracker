package com.alveteg.simon.workouts.db

import androidx.compose.runtime.compositionLocalOf

enum class ResistanceUnit(val label: String) {
  KG("kg"),
  LB("lb");

}

val LocalResistanceUnit = compositionLocalOf { ResistanceUnit.KG }