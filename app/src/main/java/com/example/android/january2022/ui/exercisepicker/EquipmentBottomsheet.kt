package com.example.android.january2022.ui.exercisepicker

import androidx.compose.runtime.Composable
import com.example.android.january2022.db.Equipment
import com.example.android.january2022.utils.Event

@Composable
fun EquipmentSheet(
  selectedEquipment: List<String>,
  onEvent: (Event) -> Unit
) {
  Sheet(
    items = Equipment.getAllEquipment().sorted(),
    selectedItems = selectedEquipment,
    onSelect = { onEvent(PickerEvent.SelectMuscle(it)) }
  ) {
    onEvent(PickerEvent.DeselectMuscles)
  }
}