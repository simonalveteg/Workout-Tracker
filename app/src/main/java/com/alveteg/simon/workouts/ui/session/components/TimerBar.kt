package com.alveteg.simon.workouts.ui.session.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.gestures.forEach
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.alveteg.simon.workouts.timer.toTimerString
import com.alveteg.simon.workouts.ui.TimerState
import com.alveteg.simon.workouts.ui.session.SessionEvent
import com.alveteg.simon.workouts.utils.Event
import timber.log.Timber

@Composable
fun TimerBar(
  modifier: Modifier = Modifier,
  timerState: TimerState,
  onEvent: (Event) -> Unit
) {

  val timerTime = timerState.time
  val timerRunning = timerState.running
  val timerMaxTime = timerState.maxTime
  val timerToggleIcon = if (timerRunning) Icons.Default.Pause else Icons.Default.PlayArrow
  val timerTimeText =
    if (timerTime > 0L) timerTime.toTimerString() else timerMaxTime.toTimerString()

  val primaryColor = MaterialTheme.colorScheme.primary
  val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
  val surfaceColor = MaterialTheme.colorScheme.surfaceVariant
  val onSurfaceColor = MaterialTheme.colorScheme.onSurface

  val animatedPrimaryColor by animateColorAsState(
    targetValue = if (timerRunning) primaryColor else surfaceColor,
    label = "TimerBarColor"
  )
  val textColor by animateColorAsState(
    targetValue = if (timerRunning) onPrimaryColor else onSurfaceColor
  )

  Surface(
    modifier = modifier
      .fillMaxWidth()
      .height(50.dp),
    shape = MaterialTheme.shapes.medium,
    color = MaterialTheme.colorScheme.surfaceContainerHigh,
  ) {
    BoxWithConstraints(
      modifier = Modifier.fillMaxSize()
    ) {
      val timerWidth by animateDpAsState(
        targetValue = if (timerMaxTime > 0) maxWidth * (timerTime.toFloat() / timerMaxTime) else 0.dp,
        label = "TimerWidthAnimation"
      )
      Surface(
        modifier = Modifier
          .fillMaxHeight()
          .width(timerWidth),
        color = animatedPrimaryColor
      ) {}
      TimerControls(
        timerTimeText = timerTimeText,
        timerToggleIcon = timerToggleIcon,
        baseColor = onSurfaceColor,
        onEvent = onEvent
      )
      TimerControls(
        modifier = Modifier
          .clip(TimerClipShape(timerWidth))
          .pointerInput(Unit) {},
        timerTimeText = timerTimeText,
        timerToggleIcon = timerToggleIcon,
        baseColor = textColor,
        onEvent = onEvent
      )
    }
  }
}

@Composable
private fun TimerControls(
  modifier: Modifier = Modifier,
  timerTimeText: String,
  timerToggleIcon: androidx.compose.ui.graphics.vector.ImageVector,
  baseColor: Color,
  onEvent: (Event) -> Unit
) {
  Row(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 12.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = { onEvent(SessionEvent.TimerDecreased) }) {
        Icon(
          imageVector = Icons.Default.Remove,
          contentDescription = "Decrease time",
          tint = baseColor
        )
      }
      Text(
        text = timerTimeText,
        textAlign = TextAlign.Center,
        color = baseColor,
        modifier = Modifier.width(50.dp)
      )
      IconButton(onClick = { onEvent(SessionEvent.TimerIncreased) }) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = "Increase time",
          tint = baseColor
        )
      }
    }
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = { onEvent(SessionEvent.TimerReset) }) {
        Icon(
          imageVector = Icons.Default.Refresh,
          contentDescription = "Reset Timer",
          tint = baseColor
        )
      }
      IconButton(onClick = { onEvent(SessionEvent.TimerToggled) }) {
        Icon(
          imageVector = timerToggleIcon,
          contentDescription = "Toggle Timer",
          tint = baseColor
        )
      }
    }
  }
}


class TimerClipShape(
  private val fillWidth: Dp
) : Shape {
  override fun createOutline(
    size: Size,
    layoutDirection: LayoutDirection,
    density: Density
  ): Outline {

    val w = with(density) { fillWidth.toPx() }
    val clipped = w.coerceIn(0f, size.width)

    return Outline.Rectangle(
      Rect(
        left = 0f,
        top = 0f,
        right = clipped,
        bottom = size.height
      )
    )
  }
}