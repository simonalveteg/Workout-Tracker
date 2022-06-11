package com.example.android.january2022.ui.statistics

import android.util.Half.toFloat
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.SessionWithSessionExerciseWithExercise
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

@Composable
fun ExerciseChart(
    data: Map<SessionWithSessionExerciseWithExercise, List<GymSet>>,
    modifier: Modifier = Modifier
) {
    val graphColor = MaterialTheme.colorScheme.primary
    val transparentGraphColor = remember {
        graphColor.copy(alpha = 0.5f)
    }
    val lowerValue = 0f
    val y = mutableListOf<Float>()
    val sortedEntries = data.entries.sortedBy { it.key.session.start }
    sortedEntries.forEach { entry ->
        entry.value.forEach { gymSet ->
            y.add(gymSet.weight)
        }
    }

    if (sortedEntries.isNotEmpty()) {
        Canvas(modifier = modifier) {
            val height = size.height


            val width = size.width


            // jj
            val firstDay = sortedEntries.first().key.session.start
            val lastDay = sortedEntries.last().key.session.start
            val days = ChronoUnit.DAYS.between(firstDay, lastDay).plus(1)
            val dayStep = width / days
            Log.d("ExerciseChart","dayStep: $dayStep, days: $days")

            val strokePath = Path().apply {
                var xvalue = 0f
                sortedEntries.forEach { entry ->
                    xvalue = ChronoUnit.DAYS.between(firstDay, entry.key.session.start).toFloat() * dayStep
                    val yvalue = height - entry.value.filter { !it.deleted }.map { it.weight }.maxOf{ it }*4
                    Log.d("ExerciseChart","x: $xvalue, y: $yvalue")
                    if (this.isEmpty) {
                        moveTo(xvalue, yvalue)
                    }
                    lineTo(xvalue, yvalue)
                }
            }
            drawPath(
                path = strokePath,
                color = graphColor,
                style = Stroke(
                    width = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )
        }
    }


}