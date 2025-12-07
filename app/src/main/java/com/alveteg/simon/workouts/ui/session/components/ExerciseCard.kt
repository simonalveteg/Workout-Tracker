package com.alveteg.simon.workouts.ui.session.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.alveteg.simon.workouts.ui.ExerciseWrapper
import com.alveteg.simon.workouts.ui.SetWrapper
import com.alveteg.simon.workouts.ui.session.SessionEvent
import com.alveteg.simon.workouts.ui.theme.ArchivoBlack
import com.alveteg.simon.workouts.utils.Event
import com.alveteg.simon.workouts.utils.ScaleVisibility
import com.alveteg.simon.workouts.utils.ignoreTouchEvents


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExerciseCard(
  modifier: Modifier = Modifier,
  exerciseWrapper: ExerciseWrapper,
  editable: Boolean = false,
  onEvent: (Event) -> Unit = {},
  onSetClicked: (SetWrapper) -> Unit = {},
  onClick: (ExerciseWrapper) -> Unit = {}
) {
  val exercise = exerciseWrapper.exercise
  val sets = exerciseWrapper.sets

  val setListState = rememberLazyListState()
  val width by remember {
    derivedStateOf { setListState.layoutInfo.viewportSize.width }
  }
  val startWidth = 50f
  val endWidth by animateFloatAsState(
    targetValue = width.toFloat() - if (setListState.canScrollForward) 225f else startWidth
  )

  Surface(
    onClick = { onClick(exerciseWrapper) },
    color = MaterialTheme.colorScheme.surfaceContainer,
    modifier = Modifier
      .fillMaxWidth()
      .then(modifier),
    shape = MaterialTheme.shapes.medium
  ) {
    Column {
      Text(
        text = exercise.title,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
          .padding(top = 16.dp, bottom = 14.dp),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.titleMedium.copy(fontFamily = ArchivoBlack)
      )
      HorizontalDivider()
      LazyRow(
        state = setListState,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 8.dp)
          .graphicsLayer { alpha = 0.99f }
          .drawWithContent {
            val colors = listOf(Color.Black, Color.Transparent)
            drawContent()
            drawRect(
              brush = Brush.horizontalGradient(
                colors = colors,
                startX = endWidth
              ),
              blendMode = BlendMode.DstIn
            )
            drawRect(
              brush = Brush.horizontalGradient(
                colors = colors.reversed(),
                endX = startWidth
              ),
              blendMode = BlendMode.DstIn
            )
          },
      ) {
        item {
          Spacer(modifier = Modifier.width(4.dp))
        }
        items(sets) { set ->
          SetCard(
            set = set,
            isClickable = editable,
            onClick = { onSetClicked(SetWrapper(set, exerciseWrapper)) }
          )
        }
        item {
          ScaleVisibility(visible = editable) {
            IconButton(
              onClick = {
                onEvent(SessionEvent.CreateSet(exerciseWrapper))
              }
            ) {
              Icon(imageVector = Icons.Default.Add, contentDescription = "Add new set.")
            }
          }
        }
      }
    }
  }
}
