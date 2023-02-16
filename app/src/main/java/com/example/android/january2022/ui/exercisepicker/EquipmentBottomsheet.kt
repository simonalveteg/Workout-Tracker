package com.example.android.january2022.ui.exercisepicker

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.january2022.utils.Event

@Composable
fun EquipmentBottomsheet(
  selectedEquipment: List<String>,
  onEvent: (Event) -> Unit
) {
  Spacer(modifier = Modifier.height(100.dp))
}