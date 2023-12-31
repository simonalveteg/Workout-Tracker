package com.example.android.january2022.ui.exercisepicker.components

import androidx.compose.runtime.Composable
import com.example.android.january2022.db.MuscleGroup
import com.example.android.january2022.ui.exercisepicker.PickerEvent
import com.example.android.january2022.utils.Event

@Composable
fun MuscleSheet(
    selectedMusclegroups: List<String>,
    onEvent: (Event) -> Unit,
) {
    Sheet(
        items = MuscleGroup.getAllMuscleGroups().sorted(),
        selectedItems = selectedMusclegroups,
        title = "Filter by Body-part",
        onSelect = { onEvent(PickerEvent.SelectMuscle(it)) },
    ) {
        onEvent(PickerEvent.DeselectMuscles)
    }
}
