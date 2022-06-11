package com.example.android.january2022.ui.session

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.ui.exercises.picker.SubTitleText
import com.example.android.january2022.ui.theme.Shapes
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun SessionInfo(_session: Session?, muscleGroups: State<List<String>>?, onEvent: (SessionEvent) -> Unit) {
    val session = _session ?: Session()
    val startTime = DateTimeFormatter.ofPattern("HH:mm").format(session.start)
    val endTime = DateTimeFormatter.ofPattern("HH:mm").format(session.end)

    // Fetching local context
    val mContext = LocalContext.current

    // Creating a TimePicker dialog
    val startTimePickerDialog = TimePickerDialog(
        mContext,
        { _,hour,minute ->
            val newDateTime = session.end.withHour(hour).withMinute(minute)
            onEvent(SessionEvent.StartTimeChanged(newDateTime))
        }, session.start.hour, session.start.minute, true
    )
    val endTimePickerDialog = TimePickerDialog(
        mContext,
        { _,hour,minute ->
            val newDateTime = session.end.withHour(hour).withMinute(minute)
            onEvent(SessionEvent.EndTimeChanged(newDateTime))
        }, LocalDateTime.now().hour, LocalDateTime.now().minute, true
    )


    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
        ) {
            Row{
                Text(
                    text = startTime,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .clickable {
                            startTimePickerDialog.show()
                        }
                )
                Text(
                    text = "-",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(start = 4.dp)
                )
                Text(
                    text = endTime,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .clickable {
                            endTimePickerDialog.show()
                        }
                )
            }
        }
        FlowRow(
            mainAxisAlignment = FlowMainAxisAlignment.Center
        ) {
            muscleGroups?.value?.filter { it.isNotEmpty() }?.forEach { s ->
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

    Column {
        SubTitleText(text = session.trainingType)
        Row {

        }
    }


}