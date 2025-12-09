package com.alveteg.simon.workouts.ui.session.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alveteg.simon.workouts.ui.ExerciseWrapper
import com.alveteg.simon.workouts.ui.SessionWrapper
import com.alveteg.simon.workouts.ui.session.SessionEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseBottomSheet(
  exerciseWrapper: ExerciseWrapper,
  sheetState: SheetState,
  setHistory: List<Pair<SessionWrapper, ExerciseWrapper>>,
  onDeleteExercise: () -> Unit,
  onEvent: (SessionEvent) -> Unit,
  onDismissRequest: () -> Unit
) {

}

@Composable
fun MuscleList(
  modifier: Modifier = Modifier,
  label: String,
  items: List<String>
) {
  BottomSheetDetailsContainer(
    text = label,
    modifier = modifier
  ) {
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(4.dp),
      contentPadding = PaddingValues(horizontal = 8.dp),
      modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
    ) {
      items(items) {
        SmallPill(it)
      }
    }
  }
}