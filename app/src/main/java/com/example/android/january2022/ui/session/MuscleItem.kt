package com.example.android.january2022.ui.session

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.ui.theme.Shapes
import com.example.android.january2022.utils.turnTargetIntoMuscleGroup

@Composable
fun MuscleItem(
    muscleGroup: String,
    selectedExercises: Set<Exercise>,
    onSelection: () -> Unit
) {
    // count the number of exercises that have been selected for the given muscle group
    val selectionCount by derivedStateOf {
        selectedExercises.count {
            it.targets.map { turnTargetIntoMuscleGroup(it) }.toString().lowercase()
                .filterNot { it.isWhitespace() }
                .contains(muscleGroup.lowercase().filterNot { it.isWhitespace() })
        }
    }
    Surface(
        shape = Shapes.large,
        tonalElevation = if (selectionCount > 0) 10.dp else 1.dp,
        modifier = Modifier
            .height(100.dp)
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .clickable { onSelection() }
    ) {
        Box {
            Text(
                text = muscleGroup,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Center)
            )
            Text(
                text = "${selectionCount.takeIf { it > 0 } ?: ""}",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 10.dp)
            )
        }

    }
}