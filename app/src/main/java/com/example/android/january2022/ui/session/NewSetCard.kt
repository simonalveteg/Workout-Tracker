package com.example.android.january2022.ui.session

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
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
    Row(
        Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Surface(color = MaterialTheme.colors.primary) {
            Box(Modifier.width(2.dp).height(34.dp))
        }
        Column(Modifier.padding(start = 4.dp)) {
            Row() {
                Text(text = reps.toString(), fontWeight = FontWeight.Bold)
                Text(text = "reps", fontSize = 10.sp)
            }
            Row() {
                Text(text = weight.toString(), fontWeight = FontWeight.Bold)
                Text(text = "kg", fontSize = 10.sp)
            }
        }
    }
}

@Preview
@Composable
fun PreviewSetCard() {
    NewSetCard(GymSet(0,0,12,33F,3),{})
}