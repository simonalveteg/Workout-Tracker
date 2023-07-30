package com.example.android.january2022.ui.session.components

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.ui.ExerciseWrapper
import com.example.android.january2022.ui.session.SessionEvent
import com.example.android.january2022.utils.Event

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExerciseCard(
  exerciseWrapper: ExerciseWrapper,
  expanded: Boolean = false,
  selected: Boolean = false,
  onEvent: (Event) -> Unit,
  onLongClick: () -> Unit,
  onSetDeleted: (GymSet) -> Unit,
  onClick: () -> Unit
) {
  val exercise = exerciseWrapper.exercise
  val sets = exerciseWrapper.sets
  val tonalElevation by animateDpAsState(targetValue = if (selected) 2.dp else 0.dp)
  val localHaptic = LocalHapticFeedback.current

  Surface(
    tonalElevation = tonalElevation,
    shape = MaterialTheme.shapes.medium,
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 8.dp, horizontal = 8.dp)
      .clip(MaterialTheme.shapes.medium)
      .combinedClickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple(bounded = true),
        onLongClick = {
          localHaptic.performHapticFeedback(HapticFeedbackType.LongPress)
          onLongClick()
        },
        onClick = onClick
      )
  ) {
    Column(
      Modifier
        .padding(top = 12.dp, bottom = 6.dp)
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
          sets = sets,
          onEvent = onEvent,
          onSetCreated = {
            onEvent(SessionEvent.SetCreated(exerciseWrapper))
          },
          onSetDeleted = onSetDeleted
        )
      }
      AnimatedVisibility(!expanded) {
        val listState = rememberLazyListState()
        val width by remember {
          derivedStateOf { listState.layoutInfo.viewportSize.width }
        }
        val startWidth = 50f
        val endWidth by animateFloatAsState(
          targetValue = width.toFloat() - if (listState.canScrollForward) 225f else startWidth
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
            items(sets) { set ->
              CompactSetCard(set)
            }
          }
          Column {
            AnimatedVisibility(
              visible = listState.canScrollForward,
              enter = slideInHorizontally(initialOffsetX = { it / 2 }) + fadeIn(),
              exit = slideOutHorizontally(targetOffsetX = { it / 2 }) + fadeOut()
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
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
      ) {
        exercise.getMuscleGroups().forEach {
          SmallPill(text = it, modifier = Modifier.padding(end = 4.dp))
        }
        exercise.equipment.forEach { eq ->
          SmallPill(text = eq, modifier = Modifier.padding(end = 4.dp))
        }
      }
    }
  }
}