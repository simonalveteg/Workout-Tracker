package com.example.android.january2022.ui.session.actions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun OpenInNewAction(
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick) {
        Icon(imageVector = Icons.Outlined.OpenInNew, contentDescription = "Open exercise guide.")
    }
}
