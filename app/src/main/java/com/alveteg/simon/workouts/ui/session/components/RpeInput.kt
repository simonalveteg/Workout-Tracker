package com.alveteg.simon.workouts.ui.session.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alveteg.simon.workouts.db.entities.Rpe
import com.alveteg.simon.workouts.ui.SetWrapper
import com.alveteg.simon.workouts.ui.session.SessionEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun RpeInput(
  setWrapper: SetWrapper,
  onEvent: (SessionEvent) -> Unit,
  modifier: Modifier = Modifier
) {
  BottomSheetDetailsContainer(
    text = "RPE",
    modifier = modifier.animateContentSize(),
    titleInset = 16.dp
  ) {
    val rpeLevels = Rpe.getRpeLevels()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
      coroutineScope.launch {
        listState.animateScrollToItem(index = rpeLevels.lastIndex)
      }
    }

    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(2.dp),
      state = listState,
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)
    ) {
      itemsIndexed(rpeLevels) { index, label ->
        val selected = remember(setWrapper) { setWrapper.set.rpe == label }
        val colors = getSetColorFromRPE(label, MaterialTheme.colorScheme)
        ToggleButton(
          checked = selected,
          colors = ToggleButtonDefaults.tonalToggleButtonColors(
            checkedContainerColor = colors.containerColor,
            checkedContentColor = colors.onContainerColor,
            containerColor = colors.containerColor,
            contentColor = colors.onContainerColor,
          ),
          onCheckedChange = {
            val newRpe = if (selected) null else label
            onEvent(
              SessionEvent.ChangeSet(
                setWrapper.set.copy(rpe = newRpe)
              )
            )
          },
          shapes =
            when (index) {
              0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
              rpeLevels.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
              else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
            },
        ) {
          Text(
            text = label.value.toString(),
            modifier = Modifier.padding(4.dp)
          )
        }
      }
    }
    if (setWrapper.set.rpe != null) {
      val text = when (setWrapper.set.rpe) {
        Rpe.Level10 -> "You have 0 reps in reserve. This is maximum effort and you reached failure on the last rep."
        Rpe.Level9 -> "You have 1 rep in reserve. You could have done one more if pushed."
        Rpe.Level8 -> "You have 2 reps in reserve. The set is hard but still controlled."
        Rpe.Level7 -> "You have 3 reps in reserve. The weight feels challenging but manageable."
        Rpe.Level6 -> "You have 4 reps in reserve. The effort is moderate and well within your limits."
        Rpe.Level5 -> "You have 5 or more reps in reserve. The set feels easy and your form stays relaxed."
        Rpe.Level4 -> "You have many reps in reserve. The weight feels light and you feel no strain yet."
        Rpe.Level3 -> "You have plenty left in the tank. The set feels like early warm-up work with almost no challenge."
        Rpe.Level2 -> "You are applying very little effort. The movement is light but still intentional."
        Rpe.Level1 -> "You are exerting almost no effort. The movement feels effortless and barely counts as work."
      }
      Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier.padding(horizontal = 16.dp)
      )
    }
  }
}