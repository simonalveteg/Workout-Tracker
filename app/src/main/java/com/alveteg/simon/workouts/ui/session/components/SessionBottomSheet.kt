package com.alveteg.simon.workouts.ui.session.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionBottomSheet(
  modifier: Modifier = Modifier,
  sessionWrapper: SessionWrapper? = null,
  exercise: Exercise,
  title: String,
  subtitle: String? = null,
  sheetState: SheetState,
  getSetHistory: suspend (Exercise) -> List<Pair<SessionWrapper, ExerciseWrapper>>,
  onDismissRequest: () -> Unit,
  onDelete: () -> Unit = {},
  onDeleteDescription: String = "",
  content: @Composable () -> Unit
) {

  var setHistory by remember {
    mutableStateOf<List<Pair<SessionWrapper, ExerciseWrapper>>>(emptyList())
  }

  LaunchedEffect(Unit, exercise) {
    setHistory = getSetHistory(exercise)
      .filter { it.first.session.sessionId != sessionWrapper?.session?.sessionId }
  }

  ModalBottomSheet(
    modifier = modifier,
    onDismissRequest = onDismissRequest,
    sheetState = sheetState,
    contentWindowInsets = { WindowInsets(0, 8, 0, 8) },
    dragHandle = {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(60.dp)
            .padding(vertical = 8.dp),
        ) {
          if (onDeleteDescription.isNotEmpty()) {
            IconButton(
              onClick = onDelete,
              modifier = Modifier.align(Alignment.CenterStart)
            ) {
              Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = onDeleteDescription
              )
            }
          }
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
          ) {
            Text(
              text = title,
              style = MaterialTheme.typography.titleLarge,
              maxLines = 1,
              autoSize = TextAutoSize.StepBased(
                maxFontSize = MaterialTheme.typography.titleLarge.fontSize,
                minFontSize = 10.sp,
              ),
              modifier = Modifier.padding(horizontal = 46.dp),
            )
            if (subtitle != null) {
              Text(
                text = subtitle, style = MaterialTheme.typography.titleSmall
              )
            }
          }
        }
        HorizontalDivider()
      }
    }
  ) {
    Column {

      content()
      HorizontalDivider()
      SetHistory(
        setHistory = setHistory,
        modifier = Modifier.padding(vertical = 8.dp)
      )
    }
  }
}