package com.example.android.january2022.ui.session

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.dp
import com.example.android.january2022.ui.rework.ExerciseWrapper
import com.example.android.january2022.utils.Event
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseCard(
  exerciseWrapper: ExerciseWrapper,
  expanded: Boolean = false,
  onEvent: (Event) -> Unit,
  onClick: () -> Unit
) {
  val exercise = exerciseWrapper.exercise
  val sets = exerciseWrapper.sets.collectAsState(initial = emptyList())
  LaunchedEffect(key1 = exercise) {
    Timber.d("ExerciseCard received new exercise")
  }

  Surface(
    onClick = { onClick() },
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 8.dp, horizontal = 8.dp),
    tonalElevation = 2.dp,
    shape = MaterialTheme.shapes.medium
  ) {
    Column(
      Modifier
        .padding(vertical = 12.dp)
        .fillMaxWidth()
    ) {
      Text(
        text = exercise.title,
        modifier = Modifier.padding(horizontal = 12.dp),
        style = MaterialTheme.typography.titleMedium
      )
      Spacer(Modifier.height(4.dp))
      AnimatedVisibility(expanded) {
        ExpandedExerciseContent(
          sets = sets.value,
          onEvent = onEvent,
          onSetCreated = {
            onEvent(SessionEvent.SetCreated(exerciseWrapper.sessionExercise))
          }
        )
      }
      AnimatedVisibility(!expanded) {
        val listState = rememberLazyListState()
        val width by remember {
          derivedStateOf { listState.layoutInfo.viewportSize.width }
        }
        val startWidth = 50f
        val endWidth by animateFloatAsState(
          targetValue = width.toFloat() - if (listState.canScrollForward) 300f else startWidth
        )
        Box(
          contentAlignment = Alignment.CenterEnd
        ) {
          LazyRow(
            modifier = Modifier
              .fillMaxWidth()
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
            state = listState
          ) {
            item {
              Spacer(Modifier.width(12.dp))
            }
            items(sets.value) { set ->
              CompactSetCard(set)
            }
          }
          Column {
            AnimatedVisibility(
              visible = listState.canScrollForward,
              enter = slideInHorizontally(initialOffsetX = { it/2 }) + fadeIn(),
              exit = slideOutHorizontally(targetOffsetX = { it/2 }) + fadeOut()
            ) {
              Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "More sets in list.",
                modifier = Modifier.padding(end = 8.dp),
                tint = LocalContentColor.current.copy(alpha = 0.5f)
              )
            }
          }
        }
      }
    }
  }
}