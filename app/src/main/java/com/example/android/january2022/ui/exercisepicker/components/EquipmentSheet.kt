package com.example.android.january2022.ui.exercisepicker.components

import androidx.compose.runtime.Composable
import com.example.android.january2022.db.Equipment
import com.example.android.january2022.ui.exercisepicker.PickerEvent
import com.example.android.january2022.utils.Event

@Composable
fun EquipmentSheet(
  selectedEquipment: List<String>,
  onEvent: (Event) -> Unit
) {
  Sheet(
    items = Equipment.getAllEquipment().sorted(),
    selectedItems = selectedEquipment,
    onSelect = { onEvent(PickerEvent.SelectEquipment(it)) }
  ) {
    onEvent(PickerEvent.DeselectEquipment)
  }
}