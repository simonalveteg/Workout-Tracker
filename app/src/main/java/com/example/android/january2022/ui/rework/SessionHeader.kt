package com.example.android.january2022.ui.rework

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.android.january2022.db.entities.Session

@Composable
fun SessionHeader(
  session: Session,
  scrollState: LazyListState,
  topPadding: Dp
  ) {
  Box(
    modifier = Modifier
      .padding(
        start = 12.dp,
        top = topPadding + 120.dp,
        bottom = 40.dp
      )
      .fillMaxWidth()
      .graphicsLayer {
        val scroll = scrollState.firstVisibleItemScrollOffset.toFloat()
        translationY = -scroll / 2f // Parallax effect
        alpha = 1 - scroll / 250f // Fade out text
        scaleX = 1 - scroll / 3000f
        scaleY = 1 - scroll / 3000f
      }
  ) {
    Text(
      text = session.toSessionTitle(),
      style = MaterialTheme.typography.headlineLarge
    )
  }
}