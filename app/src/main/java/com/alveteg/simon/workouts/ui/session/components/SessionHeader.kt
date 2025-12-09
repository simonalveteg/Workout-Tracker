package com.alveteg.simon.workouts.ui.session.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alveteg.simon.workouts.ui.SessionWrapper
import com.alveteg.simon.workouts.ui.TimerState
import com.alveteg.simon.workouts.ui.home.components.SessionDate
import com.alveteg.simon.workouts.utils.ScaleAndSlideHorizontallyVisibility
import com.alveteg.simon.workouts.utils.ScaleVisibility
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SessionHeader(
  sessionWrapper: SessionWrapper,
  screenUnlocked: Boolean,
  muscleGroups: List<String>,
  onDeleteSession: () -> Unit,
  onEndTime: () -> Unit,
  onStartTime: () -> Unit,
  onToggleEdit: () -> Unit,
  timerState: TimerState,
  timerVisible: Boolean,
  onTimerButtonClick: () -> Unit,
  dateModifier: Modifier = Modifier,
  titleModifier: Modifier = Modifier,
  modifier: Modifier = Modifier
) {

  val muscleTitle by remember(muscleGroups) {
    derivedStateOf {
      muscleGroups.take(1).joinToString().uppercase()
    }
  }
  val muscleSubtitle by remember(muscleGroups) {
    derivedStateOf {
      muscleGroups.drop(1).take(3).joinToString(separator = ", ").uppercase()
    }
  }

  val timerButtonColor by animateColorAsState(
    targetValue =
      if (timerVisible) MaterialTheme.colorScheme.surfaceVariant
      else if (timerState.running) MaterialTheme.colorScheme.primary
      else MaterialTheme.colorScheme.surfaceContainer
  )
  val editButtonColor by animateColorAsState(
    targetValue = if (screenUnlocked) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.secondaryContainer
  )
  val editButtonIcon = if (screenUnlocked) Icons.Default.Check else Icons.Default.Edit
  val iconScale = remember { Animatable(1f) }
  val iconRotation = remember { Animatable(0f) }
  val motionSpec = MaterialTheme.motionScheme.fastSpatialSpec<Float>()
  val coroutineScope = rememberCoroutineScope()

  fun triggerShakeAnimation() {
    coroutineScope.launch {
      iconScale.animateTo(1.05f, animationSpec = motionSpec)
      iconScale.animateTo(1f, animationSpec = motionSpec)
    }
    coroutineScope.launch {
      iconRotation.animateTo(0f, animationSpec = keyframes {
        durationMillis = 200
        5f at 50
        -10f at 150
        0f at 200
      })
    }
  }

  LaunchedEffect(screenUnlocked) {
    iconScale.animateTo(
      targetValue = 1.02f,
      animationSpec = motionSpec
    )
    iconScale.animateTo(
      1f,
      animationSpec = motionSpec
    )
  }

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(bottom = 8.dp)
  ) {
    Row(
      modifier = modifier
        .padding(vertical = 16.dp),
      verticalAlignment = Alignment.Bottom,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
          .padding(horizontal = 12.dp)
      ) {
        SessionDate(
          session = sessionWrapper.session,
          modifier = Modifier
            .padding(top = 10.dp)
            .then(dateModifier),
        )
        Text(
          text = sessionWrapper.session.start.year.toString(),
          style = MaterialTheme.typography.bodySmall,
          modifier = Modifier.padding(top = 8.dp)
        )
      }
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 8.dp, end = 32.dp),
        horizontalAlignment = Alignment.Start
      ) {
        Text(
          text = muscleTitle,
          style = MaterialTheme.typography.displayMedium,
          maxLines = 1,
          autoSize = TextAutoSize.StepBased(
            maxFontSize = MaterialTheme.typography.displayMedium.fontSize,
            minFontSize = 10.sp,
          ),
          modifier = titleModifier,
        )
        if (muscleSubtitle.isNotBlank()) {
          Text(
            text = muscleSubtitle,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 2.dp)
          )
        }
      }
    }
    Row(
      modifier = modifier
        .fillMaxWidth()
        .padding(top = 4.dp, bottom = 16.dp)
        .padding(horizontal = 6.dp),
    ) {
      HeaderItem(
        modifier = Modifier
          .padding(end = 8.dp)
          .width(42.dp)
          .scale(iconScale.value)
          .rotate(iconRotation.value),
        color = editButtonColor,
        onClick = onToggleEdit
      ) {
        Icon(
          imageVector = editButtonIcon,
          contentDescription = "Toggle Edit Mode.",
          modifier = Modifier
            .size(18.dp),
        )
      }
      ScaleAndSlideHorizontallyVisibility(visible = screenUnlocked) {
        HeaderItem(
          modifier = Modifier
            .padding(end = 8.dp)
            .width(42.dp),
          color = MaterialTheme.colorScheme.error,
          onClick = onDeleteSession
        ) {
          Icon(
            imageVector = Icons.Outlined.Delete,
            contentDescription = "Delete Session.",
            modifier = Modifier
              .size(18.dp),
          )
        }
      }
      Spacer(
        modifier = Modifier
          .weight(1f)
          .padding(end = 8.dp)
      )
      ScaleVisibility(visible = screenUnlocked) {
        HeaderItem(
          modifier = Modifier
            .padding(end = 8.dp)
            .width(42.dp),
          color = timerButtonColor,
          onClick = onTimerButtonClick
        ) {
          Icon(
            imageVector = Icons.Outlined.Timer,
            contentDescription = "Toggle visibility of timer.",
            modifier = Modifier
              .size(18.dp),
          )
        }
      }
      TimeCard(
        time = sessionWrapper.session.start,
        modifier = Modifier.padding(end = 8.dp),
        onClick = {
          if (screenUnlocked) onStartTime() else triggerShakeAnimation()
        }
      )
      TimeCard(
        time = sessionWrapper.session.end,
        onClick = {
          if (screenUnlocked) onEndTime() else triggerShakeAnimation()
        }
      )
    }
    HorizontalDivider()
  }
}

@Composable
fun TimeCard(
  time: LocalDateTime?, modifier: Modifier = Modifier, onClick: () -> Unit = {}
) {
  val timeString =
    if (time != null) time.toLocalTime()?.format(DateTimeFormatter.ofPattern("HH:mm"))
      .toString() else "--:--"


  HeaderItem(
    modifier = modifier.requiredWidth(76.dp),
    text = timeString,
    textStyle = MaterialTheme.typography.titleMedium,
    onClick = onClick
  )
}

@Composable
fun HeaderItem(
  modifier: Modifier = Modifier,
  text: String = "",
  textStyle: TextStyle = MaterialTheme.typography.titleSmall,
  color: Color = MaterialTheme.colorScheme.surfaceContainer,
  onClick: () -> Unit = {},
  content: @Composable () -> Unit = {}
) {
  Surface(
    modifier = modifier
      .fillMaxWidth()
      .requiredHeight(40.dp),
    color = color,
    shape = MaterialTheme.shapes.medium,
    onClick = onClick
  ) {
    Box(
      contentAlignment = Alignment.Center,
    ) {
      if (text.isBlank()) content() else {
        Text(
          text = text,
          style = textStyle,
        )
      }
    }
  }
}