package com.example.android.january2022.ui.session

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.january2022.db.entities.GymSet

@Composable
fun NewSetCard(
    set: GymSet,
    onEvent: (SessionEvent) -> Unit
) {
    val weight = set.weight
    val reps = set.reps
    var expanded by remember { mutableStateOf(false) }

    Row(
        Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable {
                expanded = !expanded
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Surface(color = MaterialTheme.colors.primary) {
            Box(
                Modifier
                    .width(2.dp)
                    .height(34.dp)
            )
        }
        if (!expanded) CompactSetCard(reps, weight)
        else ExpandedSetCard(set, onEvent, { expanded = false })
    }
}

@Composable
fun ExpandedSetCard(
    set: GymSet,
    onEvent: (SessionEvent) -> Unit,
    onDone: () -> Unit,
    keyboardType: KeyboardType = KeyboardType.Number
) {
    val localFocusManager = LocalFocusManager.current
    val reps: Int = set.reps
    val weight: Float = set.weight


    Row {
        BasicTextField(
            value = if (reps > -1) reps.toString() else " ",
            onValueChange = {
                try {
                    val newValue = it.trim().toInt()
                    onEvent(SessionEvent.RepsChanged(set, newValue))
                } catch (e: Exception) {
                    onEvent(SessionEvent.RepsChanged(set, -1))
                }
            },
            textStyle = TextStyle(
                color = MaterialTheme.colors.onSurface,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { localFocusManager.moveFocus(FocusDirection.Right) }
            ),
            cursorBrush = SolidColor(MaterialTheme.colors.onSurface),
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .padding(start = 6.dp)
                .padding(horizontal = 8.dp)
        )
        Text(
            text = "reps",
            fontSize = 14.sp
        )
        BasicTextField(
            value = if (weight > -1) weight.toString() else " ",
            onValueChange = {
                try {
                    val newValue = it.trim().toFloat()
                    onEvent(SessionEvent.WeightChanged(set, newValue))
                } catch (e: Exception) {
                }
            },
            textStyle = TextStyle(
                color = MaterialTheme.colors.onSurface,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    localFocusManager.clearFocus()
                    onDone()
                }
            ),
            cursorBrush = SolidColor(MaterialTheme.colors.onSurface),
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .padding(start = 6.dp)
                .padding(horizontal = 8.dp)
        )
        Text(text = "kg", fontSize = 14.sp)
    }
}

@Composable
fun CompactSetCard(
    reps: Int,
    weight: Float
) {
    Column(Modifier.padding(start = 4.dp)) {
        Row() {
            Text(text = if (reps > -1) reps.toString() else "0", fontWeight = FontWeight.Bold)
            Text(text = "reps", fontSize = 10.sp)
        }
        Row() {
            Text(text = if(weight > -1) weight.toString() else "0", fontWeight = FontWeight.Bold)
            Text(text = "kg", fontSize = 10.sp)
        }
    }
}

@Preview
@Composable
fun PreviewCompact() {
    CompactSetCard(reps = 12, weight = 30F)
}

@Preview
@Composable
fun PreviewExpanded() {
    ExpandedSetCard(
        set = GymSet(0, 0, 12, 30f, 2, false),
        onEvent = {},
        onDone = {}
    )
}