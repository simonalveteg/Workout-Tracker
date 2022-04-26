package com.example.android.january2022.ui.session

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.ui.exercises.picker.SubTitleText
import com.example.android.january2022.ui.theme.Shapes
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SessionInfoCard(_session: Session?, muscleGroups: State<List<String>>?) {
    val session = _session?:Session()
    val startTime = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(session.startTimeMilli)
    val endTime = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(session.endTimeMilli)

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
        ) {
            SubTitleText(text = "$startTime - $endTime", indent = 4)
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