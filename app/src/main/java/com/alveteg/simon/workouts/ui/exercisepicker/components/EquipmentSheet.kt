package com.alveteg.simon.workouts.ui.exercisepicker.components

import androidx.compose.runtime.Composable
import com.alveteg.simon.workouts.db.Equipment
import com.alveteg.simon.workouts.ui.exercisepicker.PickerEvent
import com.alveteg.simon.workouts.utils.Event

@Composable
fun EquipmentSheet(
  selectedEquipment: List<String>,
  onEvent: (Event) -> Unit
) {
  Sheet(
    items = Equipment.getAllEquipment().sorted(),
    selectedItems = selectedEquipment,
    title = "Filter by Equipment",
    onSelect = { onEvent(PickerEvent.SelectEquipment(it)) }
  ) {
    onEvent(PickerEvent.DeselectEquipment)
  }
}