package com.example.android.january2022.ui.session

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.android.january2022.db.entities.GymSet
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun SetCard(
    set: GymSet,
    onEvent: (SessionEvent) -> Unit
) {
    val reps: Int = set.reps
    val weight: Float = set.weight
    val mood: Int = set.mood
    var dismissed by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    val surfaceColor: Color by animateColorAsState(
        targetValue =
        if (offsetX.value > 100f && offsetX.value < 1100f) {
            MaterialTheme.colors.error
        } else Color.Transparent,
        animationSpec = tween(
            durationMillis = if (offsetX.value > 1000f || offsetX.value < 100f) 500 else 200,
            delayMillis = 5,
            easing = LinearOutSlowInEasing
        )
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .animateContentSize()
            .draggable(
                state = rememberDraggableState { delta ->
                    coroutineScope.launch {
                        offsetX.snapTo(offsetX.value + delta)
                    }
                },
                orientation = Orientation.Horizontal,
                onDragStopped = {
                    var toValue = 0f
                    if (offsetX.value > 300f) {
                        toValue = 1500f
                        dismissed = true
                    }
                    coroutineScope.launch {
                        offsetX.animateTo(
                            targetValue = toValue,
                            animationSpec = tween(
                                durationMillis = 300,
                                delayMillis = 25,
                                easing = LinearOutSlowInEasing
                            )
                        )
                        if (dismissed) {
                            onEvent(SessionEvent.RemoveSelectedSet(set))
                        }
                    }
                }
            )
            .offset { IntOffset(offsetX.value.roundToInt(), 0) }
            .background(color = surfaceColor)
    ) {
        MoodIcons(set, mood, onEvent)
        Spacer(modifier = Modifier.weight(1f))
        SetWeightRepsInputFields(
            set = set,
            reps = reps,
            weight = weight,
            onEvent
        )

    }
}