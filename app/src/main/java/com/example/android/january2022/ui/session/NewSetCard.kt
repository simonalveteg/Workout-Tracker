package com.example.android.january2022.ui.session

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.january2022.db.entities.GymSet
import kotlinx.coroutines.launch
import com.example.android.january2022.db.SetType

@Composable
fun NewSetCard(
    set: GymSet,
    isSelected: Boolean,
    onEvent: (SessionEvent) -> Unit,
    editable: Boolean = true
) {
    val colors = MaterialTheme.colorScheme
    val weight = set.weight
    val reps = set.reps
    val coroutineScope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf((reps == -1 && weight == -1F)) }
    val expandedWidth = 80f
    val expandedHeight = 34f
    val collapsedWidth = 2f
    val collapsedHeight = 34f
    val moodWidth = remember { Animatable(if (expanded) expandedWidth else collapsedWidth) }
    val moodHeight = remember { Animatable(if (expanded) expandedHeight else collapsedHeight) }

    val setTypeColor by animateColorAsState(
        targetValue = when (set.setType) {
            SetType.WARMUP -> Color(0xFF6B6B65)
            SetType.EASY -> Color(0xFF638D46)
            SetType.NORMAL -> Color(0xFFCAA42D)
            SetType.HARD -> Color(0xFFA73430)
            SetType.DROP -> Color(0xFF965874)
            else -> colors.primary
        }
    )

    Row(
        Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable {
                if (editable) {
                    expanded = !expanded
                    coroutineScope.launch {
                        moodWidth.animateTo(if (expanded) expandedWidth else collapsedWidth)
                        moodHeight.animateTo(if (expanded) expandedHeight else collapsedHeight)
                    }
                }
            }
            .requiredHeight(48.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Surface(color = setTypeColor) {
            Box(
                Modifier
                    .height(moodHeight.value.dp)
                    .width(moodWidth.value.dp)
                    .clickable(enabled = expanded) {
                        onEvent(SessionEvent.SetTypeChanged(set))
                    },
                contentAlignment = Alignment.Center
            ) {
                // bug in kotlin makes the fully qualified name necessary
                androidx.compose.animation.AnimatedVisibility(visible = expanded) {
                    Text(set.setType)
                }
            }
        }

        AnimatedVisibility(visible = isSelected) {
            IconButton(
                onClick = { onEvent(SessionEvent.RemoveSelectedSet(set)) },
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Remove Set from Session"
                )
            }
        }
        AnimatedVisibility(visible = expanded && !isSelected) {
            Box(modifier = Modifier.fillMaxWidth()) {
                ExpandedSetCard(set, onEvent, {
                    expanded = false
                    coroutineScope.launch {
                        moodWidth.animateTo(if (expanded) expandedWidth else collapsedWidth)
                    }
                })
            }
        }
        AnimatedVisibility(visible = !expanded && !isSelected) {
            CompactSetCard(reps, weight)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
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
    val requester = FocusRequester()
    var repsText by remember { mutableStateOf(reps.toString()) }
    var weightText by remember { mutableStateOf(weight.toString()) }
    LaunchedEffect(Unit) {
        requester.requestFocus()
    }
    LaunchedEffect(weightText) {
        try {
            val newWeight = weightText.trim().toFloat()
            onEvent(SessionEvent.WeightChanged(set, newWeight))
        } catch (e: Exception) {
        }
    }
    LaunchedEffect(repsText) {
        try {
            val newReps = repsText.trim().toInt()
            onEvent(SessionEvent.RepsChanged(set, newReps))
        } catch (e: Exception) {
        }
    }

    Row {
        BasicTextField(
            value = if (repsText != "-1") repsText else " ",
            onValueChange = {
                repsText = it
            },
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
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
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .padding(start = 6.dp)
                .padding(horizontal = 8.dp)
                .focusRequester(requester)
        )
        Text(
            text = "reps",
            fontSize = 14.sp,
            color = LocalContentColor.current.copy(alpha = 0.9f)
        )
        BasicTextField(
            value = if (weightText != "-1.0") weightText else " ",
            onValueChange = {
                weightText = it
            },
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    localFocusManager.moveFocus(FocusDirection.Next)
                    localFocusManager.clearFocus()
                    onDone()
                }
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .padding(start = 6.dp)
                .padding(horizontal = 8.dp)
        )
        Text(
            text = "kg", fontSize = 14.sp, color = LocalContentColor.current.copy(alpha = 0.9f)
        )
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
            Text(
                text = "reps",
                fontSize = 10.sp,
                color = LocalContentColor.current.copy(alpha = 0.85f)
            )
        }
        Row() {
            Text(text = if (weight > -1) weight.toString() else "0", fontWeight = FontWeight.Bold)
            Text(
                text = "kg", fontSize = 10.sp, color = LocalContentColor.current.copy(alpha = 0.85f)
            )
        }
    }
}
