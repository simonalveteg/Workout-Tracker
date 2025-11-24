package com.alveteg.simon.workouts.ui.exercisepicker.components

import androidx.compose.runtime.Composable
import com.alveteg.simon.workouts.db.MuscleGroup
import com.alveteg.simon.workouts.ui.exercisepicker.PickerEvent
import com.alveteg.simon.workouts.utils.Event

@Composable
fun MuscleSheet(
  selectedMusclegroups: List<String>,
  onEvent: (Event) -> Unit
) {
  Sheet(
    items = MuscleGroup.getAllMuscleGroups().sorted(),
    selectedItems = selectedMusclegroups,
    title = "Filter by Body-part",
    onSelect = { onEvent(PickerEvent.SelectMuscle(it)) }
  ) {
    onEvent(PickerEvent.DeselectMuscles)
  }
}