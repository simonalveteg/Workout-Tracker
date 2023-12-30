package com.example.android.january2022.ui.exercisepicker.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun MuscleButton(
    muscle: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val containerColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.23f),
        label = "",
    )

    Surface(
        onClick = onClick,
        color = containerColor,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.padding(vertical = 2.dp, horizontal = 12.dp),
    ) {
        Text(
            text = muscle.uppercase(),
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }
}
