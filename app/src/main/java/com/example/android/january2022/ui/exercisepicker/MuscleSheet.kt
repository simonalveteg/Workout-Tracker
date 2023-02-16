package com.example.android.january2022.ui.exercisepicker

import androidx.compose.runtime.Composable
import com.example.android.january2022.db.MuscleGroup
import com.example.android.january2022.utils.Event

@Composable
fun MuscleSheet(
  selectedMusclegroups: List<String>,
  onEvent: (Event) -> Unit
) {
  Sheet(
    items = MuscleGroup.getAllMuscleGroups().sorted(),
    selectedItems = selectedMusclegroups,
    onSelect = { onEvent(PickerEvent.SelectMuscle(it)) }
  ) {
    onEvent(PickerEvent.DeselectMuscles)
  }
}