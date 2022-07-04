package com.example.android.january2022.ui.exercises

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle


@Composable
fun SmallChip(
    modifier: Modifier = Modifier,
    title: String,
    isSelected: Boolean = false,
    color: Color = Color.Transparent,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    shape: Shape = RoundedCornerShape(0),
    onToggle: (String) -> Unit = {}
) {
    val chipColor = animateColorAsState(
        targetValue = if (isSelected) selectedColor else color
    )
    Surface(
        shape = shape,
        color = chipColor.value,
    ) {
        Box(
            modifier = Modifier
                .toggleable(
                    value = isSelected,
                    onValueChange = {
                        onToggle(title)
                    }
                )
        ) {
            Text(
                text = title,
                style = style,
                modifier = modifier
            )
        }
    }
}
