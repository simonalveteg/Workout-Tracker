package com.example.android.january2022.ui.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.android.january2022.db.entities.Session
import java.time.format.TextStyle
import java.util.*

@Composable
fun SessionDate(
    session: Session,
    modifier: Modifier = Modifier,
) {
    val month by remember {
        derivedStateOf {
            session.start.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
        }
    }
    val day by remember { derivedStateOf { session.start.dayOfMonth.toString() } }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        DateText(text = month)
        DateText(text = day)
    }
}

@Composable
fun DateText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.SemiBold,
    )
}
