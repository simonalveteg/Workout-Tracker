package com.alveteg.simon.workouts.ui.session.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.alveteg.simon.workouts.ui.ExerciseWrapper
import com.alveteg.simon.workouts.ui.SessionWrapper

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SetHistory(
  setHistory: List<Pair<SessionWrapper, ExerciseWrapper>>,
  modifier: Modifier = Modifier
) {

  BottomSheetDetailsContainer(
    modifier = modifier
  ) {
    LazyRow(
      modifier = modifier
        .padding(vertical = 4.dp)
        .height(60.dp)
    ) {
      items(setHistory) { pair ->
        val (sessionWrapper, exerciseWrapper) = pair
        SetHistoryCard(
          modifier = Modifier
            .padding(horizontal = 4.dp)
            .animateItem(),
          sessionWrapper = sessionWrapper,
          exerciseWrapper = exerciseWrapper
        )
      }
      if (setHistory.isEmpty()) {
        item {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
              .fillMaxHeight()
              .padding(bottom = 8.dp)
              .width(LocalConfiguration.current.screenWidthDp.dp)
          ) {
            Text(
              text = "No history available.",
              style = MaterialTheme.typography.titleMediumEmphasized,
            )
            Text(
              text = "Previous sessions will show up here.",
              style = MaterialTheme.typography.labelMedium
            )
          }
        }
      }
    }
  }
}