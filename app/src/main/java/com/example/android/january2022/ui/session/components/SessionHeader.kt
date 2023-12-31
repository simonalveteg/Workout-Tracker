package com.example.android.january2022.ui.session.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.android.january2022.ui.SessionWrapper
import com.example.android.january2022.ui.session.toSessionTitle
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import java.time.format.DateTimeFormatter

@Composable
fun SessionHeader(
    sessionWrapper: SessionWrapper,
    muscleGroups: List<String>,
    scrollState: LazyListState,
    height: Dp,
    topPadding: Dp,
    onEndTime: () -> Unit,
    onStartTime: () -> Unit,
) {
    val session = sessionWrapper.session
    val startTime = DateTimeFormatter.ofPattern("HH:mm").format(session.start)
    val endTime = session.end?.let { DateTimeFormatter.ofPattern("HH:mm").format(it) } ?: "ongoing"

    Box(
        modifier = Modifier
            .padding(
                start = 12.dp,
                top = topPadding,
                end = 12.dp,
            )
            .height(height)
            .fillMaxWidth()
            .graphicsLayer {
                val scroll = if (scrollState.layoutInfo.visibleItemsInfo.firstOrNull()?.index == 0) {
                    scrollState.firstVisibleItemScrollOffset.toFloat()
                } else {
                    10000f
                }
                translationY = scroll / 3f // Parallax effect
                alpha = 1 - scroll / 250f // Fade out text
                scaleX = 1 - scroll / 3000f
                scaleY = 1 - scroll / 3000f
            },
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
        ) {
            Text(
                text = session.toSessionTitle(),
                style = MaterialTheme.typography.headlineLarge,
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(0.5f),
                ) {
                    Text(
                        text = startTime,
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .clickable {
                                onStartTime()
                            },
                    )
                    Text(
                        text = "-",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(start = 4.dp),
                    )
                    Text(
                        text = endTime,
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .clickable {
                                onEndTime()
                            },
                    )
                }
                FlowRow(
                    mainAxisAlignment = FlowMainAxisAlignment.Center,
                ) {
                    muscleGroups.forEach { muscle ->
                        SmallPill(text = muscle, modifier = Modifier.padding(4.dp))
                    }
                }
            }
        }
    }
}
