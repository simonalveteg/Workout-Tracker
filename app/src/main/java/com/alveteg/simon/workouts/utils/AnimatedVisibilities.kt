package com.alveteg.simon.workouts.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ScaleVisibility(
  visible: Boolean,
  additionalEnterTransition: EnterTransition = EnterTransition.None,
  additionalExitTransition: ExitTransition = ExitTransition.None,
  content: @Composable () -> Unit,
) {
  AnimatedVisibility(
    visible = visible,
    enter = scaleIn(animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()) + additionalEnterTransition,
    exit = scaleOut(animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()) + additionalExitTransition
  ) {
    content()
  }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FadeInVisibility(
  visible: Boolean,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit,
) {
  AnimatedVisibility(
    visible = visible,
    enter = fadeIn(animationSpec = MaterialTheme.motionScheme.slowEffectsSpec()),
    exit = fadeOut(animationSpec = MaterialTheme.motionScheme.slowEffectsSpec())
  ) {
    content()
  }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ScaleAndSlideHorizontallyVisibility(
  visible: Boolean,
  slideRight: Boolean = true,
  content: @Composable () -> Unit,
) {
  val direction = if (slideRight) -1 else 1

  ScaleVisibility(
    visible = visible,
    additionalEnterTransition = slideInHorizontally(
      animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec(),
      initialOffsetX = { direction * it / 2 }
    ),
    additionalExitTransition = slideOutHorizontally(
      animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec(),
      targetOffsetX = { direction * it / 2 }
    )
  ) {
    content()
  }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ScaleAndSlideVerticallyVisibility(
  visible: Boolean,
  slideDown: Boolean = true,
  content: @Composable () -> Unit,
) {
  val direction = if (slideDown) -1 else 1

  ScaleVisibility(
    visible = visible,
    additionalEnterTransition = slideInVertically(
      animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec(),
      initialOffsetY = { direction * it / 2 }
    ),
    additionalExitTransition = slideOutVertically(
      animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec(),
      targetOffsetY = { direction * it / 2 }
    )
  ) {
    content()
  }
}