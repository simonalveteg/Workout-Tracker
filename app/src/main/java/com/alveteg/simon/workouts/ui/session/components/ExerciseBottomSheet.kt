package com.alveteg.simon.workouts.ui.session.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alveteg.simon.workouts.db.entities.Exercise
import com.alveteg.simon.workouts.ui.ExerciseWrapper
import com.alveteg.simon.workouts.ui.SessionWrapper
import com.alveteg.simon.workouts.ui.session.SessionEvent
import com.alveteg.simon.workouts.utils.Event

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseBottomSheet(
  modifier: Modifier = Modifier,
  sessionWrapper: SessionWrapper? = null,
  sheetState: SheetState,
  onEvent: (Event) -> Unit,
  onDismissRequest: () -> Unit,
  getSetHistory: suspend (Exercise) -> List<Pair<SessionWrapper, ExerciseWrapper>>,
  exercise: Exercise,
) {

  var setHistory by remember {
    mutableStateOf<List<Pair<SessionWrapper, ExerciseWrapper>>>(emptyList())
  }

  LaunchedEffect(Unit, exercise) {
    setHistory = getSetHistory(exercise)
  }

  ModalBottomSheet(
    modifier = modifier,
    onDismissRequest = onDismissRequest,
    sheetState = sheetState,
    contentWindowInsets = { WindowInsets(0, 8, 0, 8) },
    dragHandle = {}
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .requiredHeight(54.dp),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = exercise.title,
        style = MaterialTheme.typography.titleLarge,
        maxLines = 1,
        autoSize = TextAutoSize.StepBased(
          maxFontSize = MaterialTheme.typography.titleLarge.fontSize,
          minFontSize = 10.sp,
        ),
        modifier = Modifier.padding(horizontal = 46.dp).padding(top = 6.dp),
      )
    }
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(4.dp),
      modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
    ) {
      HorizontalDivider(modifier = Modifier.weight(1f))
      exercise.getPrimaryMuscleGroups().forEach {
        SmallPill(it)
      }
      exercise.getSecondaryMuscleGroups().forEach {
        OutlinedSmallPill(it)
      }
      HorizontalDivider(modifier = Modifier.weight(1f))
    }
    SetHistory(
      setHistory = setHistory,
      modifier = Modifier.padding(vertical = 8.dp)
    )
    Row(
      horizontalArrangement = Arrangement.spacedBy(4.dp),
      modifier = Modifier.padding(bottom = 16.dp)
    ) {
      Spacer(modifier = Modifier.width(8.dp))
      ElevatedAssistChip(
        onClick = { onEvent(SessionEvent.SearchForExercise(exercise, "! \"exrx.net\"")) },
        label = { Text("exrx.net") },
        leadingIcon = Icons.Default.Search,
        leadingIconDescription = "Search for exercise on exrx.net"
      )
      ElevatedAssistChip(
        onClick = { onEvent(SessionEvent.SearchForExercise(exercise)) },
        label = { Text("DuckDuckGo") },
        leadingIcon = Icons.Default.Search,
        leadingIconDescription = "Search for exercise on DuckDuckGo."
      )
      ElevatedAssistChip(
        onClick = { onEvent(SessionEvent.SearchForExercise(exercise, "!yt")) },
        label = { Text("YouTube") },
        leadingIcon = Icons.Default.Videocam,
        leadingIconDescription = "Search for exercise on YouTube.com"
      )
    }
    Spacer(modifier = Modifier.navigationBarsPadding())
  }
}