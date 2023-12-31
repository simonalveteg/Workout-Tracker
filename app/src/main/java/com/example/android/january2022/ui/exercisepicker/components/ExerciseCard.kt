package com.example.android.january2022.ui.exercisepicker.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.ui.exercisepicker.PickerEvent
import com.example.android.january2022.ui.session.actions.OpenInNewAction
import com.example.android.january2022.ui.session.actions.OpenStatsAction
import com.example.android.january2022.ui.session.components.SmallPill
import com.example.android.january2022.utils.Event

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseCard(
    exercise: Exercise,
    selected: Boolean,
    onEvent: (Event) -> Unit,
    onClick: () -> Unit,
) {
    val targets = exercise.getMuscleGroups()
    val equipment = exercise.equipment
    val tonalElevation by animateDpAsState(targetValue = if (selected) 2.dp else 0.dp)
    val indicatorColor by
        animateColorAsState(if (selected) MaterialTheme.colorScheme.primary else Color.Transparent)

    val localDensity = LocalDensity.current
    var rowHeightDp by remember { mutableStateOf(0.dp) }

    val indicatorHeight by
        animateDpAsState(targetValue = if (selected) rowHeightDp else 0.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .onGloballyPositioned { coordinates ->
                // Set column height using the LayoutCoordinates
                rowHeightDp = with(localDensity) {
                    coordinates.size.height
                        .minus(95)
                        .toDp()
                }
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            color = indicatorColor,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .width(3.dp)
                .height(indicatorHeight),
        ) {}
        Spacer(modifier = Modifier.width(4.dp))
        Surface(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 80.dp),
            tonalElevation = tonalElevation,
            shape = MaterialTheme.shapes.medium,
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 14.dp, top = 4.dp, bottom = 4.dp, end = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = exercise.title,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .fillMaxWidth(0.65f),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Row(
                        modifier = Modifier.padding(bottom = 4.dp),
                    ) {
                        targets.forEach { target ->
                            SmallPill(text = target, modifier = Modifier.padding(end = 4.dp))
                        }
                        equipment.forEach { eq ->
                            SmallPill(text = eq)
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OpenStatsAction {}
                    OpenInNewAction { onEvent(PickerEvent.OpenGuide(exercise)) }
                }
            }
        }
    }
}
