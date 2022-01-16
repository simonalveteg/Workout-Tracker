package com.example.android.january2022.ui.session

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SentimentNeutral
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material.icons.filled.SentimentVerySatisfied
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.january2022.db.entities.GymSet

@ExperimentalAnimationApi
@Composable
fun MoodIcons(
    set: GymSet,
    mood: Int,
    onEvent: (SessionEvent) -> Unit,
) {
    // TODO: Decide to fix or remove AnimatedVisibility
    Row {
        AnimatedVisibility(visible = true) {
            IconToggleButton(
                checked = mood == 1, onCheckedChange = {
                    onEvent(SessionEvent.MoodChanged(set, 1))
                }
            ) {
                Icon(
                    imageVector = Icons.Default.SentimentVeryDissatisfied,
                    contentDescription = "Bad",
                    tint = if (mood == 1) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
                )
            }
        }
        AnimatedVisibility(visible = true) {
            IconToggleButton(
                checked = mood == 2, onCheckedChange = {
                    onEvent(SessionEvent.MoodChanged(set, 2))
                }
            ) {
                Icon(
                    imageVector = Icons.Default.SentimentNeutral,
                    contentDescription = "Neutral",
                    tint = if (mood == 2) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
                )
            }
        }
        AnimatedVisibility(visible = true) {
            IconToggleButton(
                checked = mood == 3, onCheckedChange = {
                    onEvent(SessionEvent.MoodChanged(set, 3))
                }
            ) {
                Icon(
                    imageVector = Icons.Default.SentimentVerySatisfied,
                    contentDescription = "Good",
                    tint = if (mood == 3) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
                )
            }
        }
    }
}