package com.example.android.january2022.ui.session.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InputLabel(text: String) {
    Text(
        text = text,
        color = LocalContentColor.current.copy(alpha = 0.8f),
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier
            .padding(start = 4.dp, top = 6.dp)
            .fillMaxHeight(),
    )
}
