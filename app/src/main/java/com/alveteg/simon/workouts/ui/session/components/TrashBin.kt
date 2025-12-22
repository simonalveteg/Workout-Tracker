package com.alveteg.simon.workouts.ui.session.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alveteg.simon.workouts.utils.ScaleVisibility

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TrashBin(
  availableHeight: Dp,
  highlighted: Boolean,
  visible: Boolean,
  onGloballyPositioned: (LayoutCoordinates) -> Unit
) {
  val containerColor by animateColorAsState(
    targetValue = if (highlighted) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.background,
    animationSpec = MaterialTheme.motionScheme.defaultEffectsSpec()
  )
  val contentColor by animateColorAsState(
    targetValue = if (highlighted) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onBackground,
    animationSpec = MaterialTheme.motionScheme.defaultEffectsSpec()
  )
  val iconScale by animateFloatAsState(
    targetValue = if (highlighted) 1.5f else 1.0f,
    animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()
  )
  val minimumHeight = 120.dp
  var isPositioned by remember(visible) { mutableStateOf(false) }

  if (visible) {
    Box(
      modifier = Modifier.height(availableHeight.coerceAtLeast(minimumHeight)),
      contentAlignment = Alignment.BottomCenter
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(minimumHeight)
          .onGloballyPositioned {
            onGloballyPositioned(it)
            isPositioned = true
          }
          .background(containerColor),
        contentAlignment = Alignment.Center) {
        ScaleVisibility(
          visible = isPositioned,
        ) {
          Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.scale(iconScale)
          )
        }
      }
    }
  }
}