package com.example.android.january2022.ui.session

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.android.january2022.db.entities.GymSet


@Composable
fun SetWeightRepsInputFields(
    set: GymSet,
    reps: Int = -1,
    weight: Float = -1F,
    onEvent: (SessionEvent) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Number,
) {
    Row {
        TextField(
            value = if (reps > -1) reps.toString() else "",
            onValueChange = {
                try {
                    val newValue = it.trim().toInt()
                    onEvent(SessionEvent.RepsChanged(set, newValue))
                } catch (e: Exception) {
                    onEvent(SessionEvent.RepsChanged(set, -1))
                }
            },
            placeholder = { Text("reps") },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier.width(100.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            trailingIcon = { Icon(Icons.Default.UnfoldMore, "Number of reps") },
            maxLines = 1

        )
        TextField(
            value = if (weight > -1) weight.toString() else "",
            onValueChange = {
                try {
                    val newValue = it.trim().toFloat()
                    onEvent(SessionEvent.WeightChanged(set, newValue))
                } catch (e: Exception) {
                }
            },
            placeholder = { Text("weight") },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier.width(120.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            trailingIcon = { Icon(Icons.Filled.FitnessCenter, "weight") },
        )
    }
}