package com.alveteg.simon.workouts.ui.session.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alveteg.simon.workouts.db.entities.Exercise
import com.alveteg.simon.workouts.ui.ExerciseWrapper
import com.alveteg.simon.workouts.ui.SessionWrapper
import com.alveteg.simon.workouts.ui.SetWrapper
import com.alveteg.simon.workouts.ui.session.SessionEvent
import com.alveteg.simon.workouts.utils.FloatInputTransformation
import com.alveteg.simon.workouts.utils.IntegerInputTransformation
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@OptIn(
  ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class,
  ExperimentalLayoutApi::class
)
@Composable
fun SetBottomSheet(
  setWrapper: SetWrapper,
  exerciseWrapper: ExerciseWrapper,
  sessionWrapper: SessionWrapper,
  sheetState: SheetState,
  getSetHistory: suspend (Exercise) -> List<Pair<SessionWrapper, ExerciseWrapper>>,
  onDeleteSet: () -> Unit,
  onEvent: (SessionEvent) -> Unit,
  onDismissRequest: () -> Unit
) {

  val exerciseName = setWrapper.exerciseWrapper.exercise.title
  val setNumber = setWrapper.exerciseWrapper.sets.indexOf(setWrapper.set).let { index ->
    if (index == -1) {
      setWrapper.exerciseWrapper.sets.size
    } else {
      index
    }
  } + 1

  val repsTextFieldState =
    rememberTextFieldState(initialText = setWrapper.set.reps?.toString() ?: "")
  val weightTextFieldState =
    rememberTextFieldState(initialText = setWrapper.set.weight?.let { float ->
      if (float == 0f) ""
      else if (float % 1.0f == 0.0f) float.toInt().toString()
      else float.toString()
    } ?: "")

  LaunchedEffect(repsTextFieldState, weightTextFieldState, setWrapper) {
    snapshotFlow { repsTextFieldState.text.toString() to weightTextFieldState.text.toString() }
      .collectLatest { (repsText, weightText) ->
        val reps = repsText.toIntOrNull()
        val weight = weightText.toFloatOrNull()
        Timber.d("Reps: $reps, Weight: $weight")

        var updatedSet = setWrapper.set

        reps?.let { updatedSet = updatedSet.copy(reps = it) }
        weight?.let { updatedSet = updatedSet.copy(weight = it) }

        if (updatedSet != setWrapper.set) {
          onEvent(SessionEvent.ChangeSet(updatedSet))
        }
      }
  }

  ModalBottomSheet(
    onDismissRequest = onDismissRequest,
    sheetState = sheetState,
    contentWindowInsets = { WindowInsets(0, 8, 0, 8) },
    dragHandle = {}
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .requiredHeight(60.dp)
        .padding(vertical = 8.dp),
    ) {
      IconButton(
        onClick = onDeleteSet,
        modifier = Modifier.align(Alignment.CenterStart)
      ) {
        Icon(
          imageVector = Icons.Outlined.Delete,
          contentDescription = "Delete Set."
        )
      }
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.align(Alignment.Center)
      ) {
        Text(
          text = "SET $setNumber",
          style = MaterialTheme.typography.titleLarge,
          maxLines = 1,
          autoSize = TextAutoSize.StepBased(
            maxFontSize = MaterialTheme.typography.titleLarge.fontSize,
            minFontSize = 10.sp,
          ),
          modifier = Modifier.padding(horizontal = 46.dp),
        )
        Text(
          text = exerciseName, style = MaterialTheme.typography.titleSmall
        )
      }
    }
    HorizontalDivider()
    Column(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.padding(vertical = 16.dp)
    ) {
      RpeInput(
        setWrapper = setWrapper,
        onEvent = onEvent,
        modifier = Modifier
      )
      Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
          .padding(horizontal = 16.dp)
          .fillMaxWidth()
      ) {
        InputField(
          textFieldState = repsTextFieldState,
          inputTransformation = IntegerInputTransformation(),
          isValid = repsTextFieldState.text.toString()
            .let { it.isNotEmpty() && it.toIntOrNull() == null },
          imeAction = ImeAction.Next,
          labelText = "Reps",
          modifier = Modifier
            .weight(1f)
            .padding(horizontal = 4.dp)
        )
        InputField(
          textFieldState = weightTextFieldState,
          inputTransformation = FloatInputTransformation(),
          isValid = weightTextFieldState.text.toString()
            .let { it.isNotEmpty() && it.toFloatOrNull() == null },
          imeAction = ImeAction.Done,
          labelText = "Weight",
          modifier = Modifier
            .weight(1f)
            .padding(horizontal = 4.dp)
        )
      }
    }
  }
}
