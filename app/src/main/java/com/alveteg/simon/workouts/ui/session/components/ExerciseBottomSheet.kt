package com.alveteg.simon.workouts.ui.session.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alveteg.simon.workouts.db.entities.Exercise
import com.alveteg.simon.workouts.ui.ExerciseWrapper
import com.alveteg.simon.workouts.ui.SessionWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseBottomSheet(
  modifier: Modifier = Modifier,
  sessionWrapper: SessionWrapper? = null,
  onDelete: () -> Unit = {},
  sheetState: SheetState,
  onDismissRequest: () -> Unit,
  getSetHistory: suspend (Exercise) -> List<Pair<SessionWrapper, ExerciseWrapper>>,
  exercise: Exercise,
  ) {

  SessionBottomSheet(
    onDismissRequest = onDismissRequest,
    sessionWrapper = sessionWrapper,
    title = exercise.title,
    sheetState = sheetState,
    onDelete = onDelete,
    exercise = exercise,
    getSetHistory = getSetHistory,
    onDeleteDescription = "Delete Exercise from Session.",
    modifier = modifier
  ) {
    Row(
      horizontalArrangement = Arrangement.SpaceEvenly,
      modifier = Modifier.fillMaxWidth()
    ) {
      ExerciseDetail(
        text = exercise.equipment.joinToString(", "),
        icon = Icons.Default.FitnessCenter
      )
      ExerciseDetail(
        text = exercise.force.joinToString(", "),
        icon = Icons.Default.Height
      )
    }
    MuscleList(
      label = "Primary Muscles",
      items = exercise.targets.filterNot { it.isBlank() }
    )
    MuscleList(
      label = "Secondary Muscles",
      items = exercise.synergists.filterNot { it.isBlank() }
    )
    MuscleList(
      label = "Stabilizing Muscles",
      items = exercise.stabilizers.filterNot { it.isBlank() }
    )
  }
}