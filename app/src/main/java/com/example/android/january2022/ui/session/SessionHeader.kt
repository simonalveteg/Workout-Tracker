package com.example.android.january2022.ui.session

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.android.january2022.ui.rework.SessionWrapper
import com.example.android.january2022.ui.theme.Shapes
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import java.time.format.DateTimeFormatter

@Composable
fun SessionHeader(
  sessionWrapper: SessionWrapper,
  muscleGroups: List<String>,
  scrollState: LazyListState,
  height: Dp,
  topPadding: Dp
) {
  val session = sessionWrapper.session
  val startTime = DateTimeFormatter.ofPattern("HH:mm").format(session.start)
  val endTime = DateTimeFormatter.ofPattern("HH:mm").format(session.end)

  Box(
    modifier = Modifier
      .padding(
        start = 12.dp,
        top = topPadding,
        end = 12.dp
      )
      .height(height)
      .fillMaxWidth()
      .graphicsLayer {
        val scroll = if(scrollState.layoutInfo.visibleItemsInfo.firstOrNull()?.index == 0) {
          scrollState.firstVisibleItemScrollOffset.toFloat()
        } else {
          10000f
        }
        translationY = -scroll / 2f // Parallax effect
        alpha = 1 - scroll / 250f // Fade out text
        scaleX = 1 - scroll / 3000f
        scaleY = 1 - scroll / 3000f
      }
  ) {
    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Bottom
    ) {
      Text(
        text = session.toSessionTitle(),
        style = MaterialTheme.typography.headlineLarge
      )
      Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .padding(8.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(0.5f)
        ) {
          Text(
            text = startTime,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
              .padding(start = 4.dp)
          )
          Text(
            text = "-",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(start = 4.dp)
          )
          Text(
            text = if (endTime <= startTime) "ongoing" else endTime,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
              .padding(start = 4.dp)
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
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(4.dp)
              )
            }
          }
        }
      }
    }
  }
}