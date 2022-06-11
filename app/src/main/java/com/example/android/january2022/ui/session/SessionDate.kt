package com.example.android.january2022.ui.session

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.db.entities.SessionExercise
import java.time.format.TextStyle
import java.util.*

@Composable
fun SessionDate(session: Session) {

    val startMonth = session.start.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
    val startDay = session.start.dayOfMonth.toString()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(end = 16.dp)
    ) {
        Text(
            text = startMonth,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = startDay,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}