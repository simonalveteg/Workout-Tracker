package com.example.android.january2022.ui.home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.SessionExerciseWithExercise


@Composable
fun SessionContent(
    sessionExercise: SessionExerciseWithExercise,
    sets: List<GymSet>
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Text(sessionExercise.exercise.exerciseTitle, modifier = Modifier.weight(1f))
        val string = StringBuilder()
        sets.forEach { set ->
            val reps = set.reps
            val weight = set.weight


            val isInt = set.weight - set.weight.toInt() <= 0

            if (set.parentSessionExerciseId == sessionExercise.sessionExercise.sessionExerciseId) {
                if (reps > -1 && weight > -1) {
                    string.append(reps)
                    string.append("x")
                    string.append(if (isInt) weight.toInt() else weight)
                    string.append(", ")
                }
            }
        }
        val concat = string.toString().removeSuffix(", ")
        Text(concat, modifier = Modifier.weight(1f), textAlign = TextAlign.Justify)
    }
}