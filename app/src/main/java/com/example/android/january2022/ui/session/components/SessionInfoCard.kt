package com.example.android.january2022.ui.session.components

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.ui.exercises.picker.SubTitleText
import com.example.android.january2022.ui.session.SessionEvent
import com.example.android.january2022.ui.theme.Shapes
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun SessionInfo(
    startTime: LocalDateTime,
    endTime: LocalDateTime,
    muscleGroups: List<String>,
    onEvent: (SessionEvent) -> Unit
) {
    val startTimeForm by derivedStateOf { DateTimeFormatter.ofPattern("HH:mm").format(startTime) }
    val endTimeForm by derivedStateOf { DateTimeFormatter.ofPattern("HH:mm").format(endTime) }

    // Fetching local context
    val mContext = LocalContext.current

    // Creating a TimePicker dialog
    val startTimePickerDialog = TimePickerDialog(
        mContext,
        { _, hour, minute ->
            val newDateTime = endTime.withHour(hour).withMinute(minute)
            onEvent(SessionEvent.StartTimeChanged(newDateTime))
        }, startTime.hour, startTime.minute, true
    )
    val endTimePickerDialog = TimePickerDialog(
        mContext,
        { _, hour, minute ->
            val newDateTime = endTime.withHour(hour).withMinute(minute)
            onEvent(SessionEvent.EndTimeChanged(newDateTime))
        }, LocalDateTime.now().hour, LocalDateTime.now().minute, true
    )
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
        Row {
            Text(
                text = startTimeForm,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .clickable { startTimePickerDialog.show() }
            )
            Text(
                text = "-",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 4.dp)
            )
            Text(
                text = if (endTime == startTime) "ongoing" else endTimeForm,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .clickable { endTimePickerDialog.show() }
            )
        }
        FlowRow(
            mainAxisAlignment = FlowMainAxisAlignment.Center
        ) {
            muscleGroups.forEach { s ->
                Surface(
                    shape = Shapes.small,
                    tonalElevation = 1.dp,
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(
                        text = s.uppercase(),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }
    }
}