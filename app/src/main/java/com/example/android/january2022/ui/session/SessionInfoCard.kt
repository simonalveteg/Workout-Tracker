package com.example.android.january2022.ui.session

import android.app.AlertDialog
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
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SessionInfoCard(_session: Session?, muscleGroups: State<List<String>>?, onEvent: (SessionEvent) -> Unit) {
    val session = _session ?: Session()
    val startTime = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(session.startTimeMilli)
    val endTime = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(session.endTimeMilli)

    // Fetching local context
    val mContext = LocalContext.current

    // Declaring and initializing a calendar
    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]

    // Creating a TimePicker dialog
    val mTimePickerDialog = TimePickerDialog(
        mContext,
        { _,hour,minute ->
            mCalendar.set(Calendar.HOUR_OF_DAY, hour)
            mCalendar.set(Calendar.MINUTE, minute)
            onEvent(SessionEvent.EndTimeChanged(mCalendar.timeInMillis))
        }, mHour, mMinute, true
    )

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
        ) {
            Text(
                text = "$startTime - $endTime",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .clickable {
                        mTimePickerDialog.show()
                    }
            )
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